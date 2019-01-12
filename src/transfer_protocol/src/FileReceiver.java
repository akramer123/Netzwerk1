package transfer_protocol.src;

import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.CRC32;

public class FileReceiver {
    private final CRC32 crc = new CRC32();
    private DatagramSocket serverSocket = new SocketFilter(90);
    private static final int BUFFER_LENGTH = 1024;
    private int read;
    private State currentState;
    private final Transition[][] transition;
    private String fileName;
    private static FileOutputStream fileDataWriter;
    private String path;
    private static long start;
    private static long end;
    private static int finCounter;
    private static boolean waitForFinPacket = false;
    private static boolean close = false;
    private String pathWithFilename;



    public FileReceiver() throws SocketException {
        serverSocket.setSoTimeout(10000);
        currentState = State.WAIT_FOR_NEW_PACKET_0;

        transition = new Transition[State.values().length][Message.values().length];
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_0.ordinal()][Message.GOT_CALL_FROM_ABOVE_0.ordinal()] = new SendAck();
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_1.ordinal()][Message.GOT_CALL_FROM_ABOVE_1.ordinal()] = new SendAck();
        transition[State.WAIT_FOR_NEW_PACKET_0.ordinal()][Message.RECEIVED_DUPLICATE.ordinal()] = new ResendAck();
        transition[State.WAIT_FOR_NEW_PACKET_0.ordinal()][Message.RECEIVED_PACKET_0.ordinal()] = new ReceivePacket0();
        transition[State.WAIT_FOR_NEW_PACKET_1.ordinal()][Message.RECEIVED_DUPLICATE.ordinal()] = new ResendAck();
        transition[State.WAIT_FOR_NEW_PACKET_1.ordinal()][Message.RECEIVED_PACKET_1.ordinal()] = new ReceivePacket1();
        transition[State.WAIT_FOR_START_CALL.ordinal()][Message.GOT_START_CALL_FROM_ABOVE.ordinal()] = new ReceivePacket1();
    }

    public static void main(String[] args) throws IOException {

        start = System.currentTimeMillis();

        FileReceiver testReceiver = new FileReceiver();
        testReceiver.processMessage(Message.GOT_START_CALL_FROM_ABOVE);

        while (testReceiver.getRead() != -1  || (waitForFinPacket  && !close)) {
            if (testReceiver.getCurrentState() == State.WAIT_FOR_CALL_FROM_ABOVE_0) {
                testReceiver.processMessage(Message.GOT_CALL_FROM_ABOVE_0);
            } else if (testReceiver.getCurrentState() == State.WAIT_FOR_CALL_FROM_ABOVE_1) {
                testReceiver.processMessage(Message.GOT_CALL_FROM_ABOVE_1);
            } else if (testReceiver.getCurrentState() == State.WAIT_FOR_NEW_PACKET_0) {
                testReceiver.waitForNewPacket();
            } else if (testReceiver.getCurrentState() == State.WAIT_FOR_NEW_PACKET_1) {
                testReceiver.waitForNewPacket();
            }
        }
        fileDataWriter.close();
        end = System.currentTimeMillis();
        testReceiver.calculateGoodput();
        System.out.println("END!!");
    }

    private void waitForNewPacket() throws IOException {
        if (waitForFinPacket) {
            read = 1;
        }
        byte[] data = new byte[BUFFER_LENGTH];
        boolean outOfTime = false;
        boolean received = false;
        while (!outOfTime && !received && read != -1){
            try {
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                serverSocket.receive(receivePacket);
                int alternatingBit = data[1015];
                Map<String, Boolean> header = readHeader(data, alternatingBit);
                Boolean correctState = header.get("correctState");
                Boolean correctChecksum = header.get("crc");

                int count = (int) Stream.iterate(0, i -> i + 1).limit(1014).filter(i -> data[i] != 0).count();
                if (count == 0) {
                    if (finCounter == 1) {
                        waitForFinPacket = false;
                    }
                    else {
                        sendAck(alternatingBit);
                        waitForFinPacket = true;
                    }
                    finCounter++;
                }


                if (correctState & correctChecksum) {
                    currentState = alternatingBit == 1 ? State.WAIT_FOR_CALL_FROM_ABOVE_0 : State.WAIT_FOR_CALL_FROM_ABOVE_1;
                    int endIndex = 1015;
                    if (StringUtils.isEmpty(fileName)) {
                        for (int i = 0; i < 1015; i++) {
                            if (data[i] == 0) {
                                endIndex = i;
                                break;
                            }
                        }
                    }
                    byte[] fileData = Arrays.copyOfRange(data, 0, endIndex);
                    if (StringUtils.isEmpty(fileName)) {
                        fileName = new String(fileData);
                        String[] path;
                        String splitChar;
                        String dividePath;
                        if (fileName.contains("/")) {
                            splitChar = "/";
                            dividePath = "/";
                        } else {
                            // Hier musste ich zwei verschiedene Variablen nehmen, weil ich bei Windows  ueber Java doppelte Backslashes
                            // reinbekomme, aber wenn ich auf das File zugreifen will (brauche ich fuer den Goodput)
                            //muss ich den Pfad mit einzelnen Backslashes zusammen bauen.
                            //splitChar ist zum Trennen dividePath zum zusammen bauen
                            splitChar = "\\\\";
                            dividePath = "\\";
                        }
                        path = fileName.split(splitChar);
                        String[] fileType = path[0].split("\\.");
                        fileName = fileType[0] + "2." + fileType[1];
                        pathWithFilename = System.getProperty("user.dir") + dividePath + fileName;
                        fileDataWriter = new FileOutputStream(pathWithFilename);
                        System.out.println("pathWithFilename" + pathWithFilename);


                    } else {
                        if (fileName.split("\\.")[1].contains("txt")) {
                            final String converted = new String(fileData, StandardCharsets.ISO_8859_1);
                            final byte[] outputBytes = converted.trim().getBytes(StandardCharsets.ISO_8859_1);
                            fileDataWriter.write(outputBytes);
                        } else {
                            fileDataWriter.write(fileData);
                        }
                    }


                    received = true;
                } else if (count != 0) {
                    processMessage(Message.RECEIVED_DUPLICATE);
                }

            } catch (SocketTimeoutException exception) {
                if (waitForFinPacket) {


                    close = true;
                    finCounter = 0;
                    read = -1;
                }
                System.out.println("timeout");
                processMessage(Message.TIMEOUT);
                outOfTime = true;
            }
            System.out.println("end of wait for new packet currentState" + currentState);
            System.out.println("outOfTime" + outOfTime);
            System.out.println("received" + received);
            System.out.println("read" + read);
        }
    }


    private Map<String, Boolean> readHeader(byte[] data, int alternatingBit) {
        Map<String, Boolean> header = new HashMap<>();
        crc.reset();
        crc.update(data, 0, 1015);
        byte[] crcBytes = new byte[8];
        Stream.iterate(0, i -> i + 1).limit(8).forEach(i -> crcBytes[i] = data[1016 + i]);

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(crcBytes);
        buffer.flip();

        long receivedCrc = buffer.getLong();

        System.out.println("received crc: " + receivedCrc + " === crc:" + crc.getValue());
        boolean isRightChecksum = receivedCrc == crc.getValue();
        System.out.println("CRC is " + isRightChecksum);

        System.out.println("alternating bit" + alternatingBit);
        boolean correctState = !currentState.name().contains(String.valueOf(alternatingBit));
        header.put("correctState", correctState);
        header.put("crc", isRightChecksum);


        return header;
    }


    public void sendAck(final int alternatingBit) throws IOException {
        try (SocketFilter sendSocket = new SocketFilter()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            String ack = "ACK";

            byte[] fileData = new byte[BUFFER_LENGTH];
            byte[] startMessage = ack.getBytes();
            Stream.iterate(0, i -> i + 1).limit(startMessage.length).forEach(i -> fileData[i] = startMessage[i]);
            fileData[1015] = (byte) alternatingBit;

            DatagramPacket datagramPacket = new DatagramPacket(fileData, fileData.length, IPAddress, 100);
            sendSocket.send(datagramPacket);
            System.out.println("send Ack" + alternatingBit);
        }
    }


    public void processMessage(Message input) {
        System.out.println("INFO Received " + input + " in state " + currentState);
        Transition trans = transition[currentState.ordinal()][input.ordinal()];
        if (trans != null) {
            currentState = trans.execute(input);
        }
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public int getRead() {
        return read;
    }

    enum Message {
        GOT_CALL_FROM_ABOVE_0, GOT_CALL_FROM_ABOVE_1, RECEIVED_PACKET_0, RECEIVED_PACKET_1, RECEIVED_DUPLICATE, TIMEOUT, GOT_START_CALL_FROM_ABOVE
    }

    enum State {
        WAIT_FOR_NEW_PACKET_0, WAIT_FOR_NEW_PACKET_1, WAIT_FOR_CALL_FROM_ABOVE_0, WAIT_FOR_CALL_FROM_ABOVE_1, WAIT_FOR_START_CALL
    }


    abstract class Transition {
        public abstract State execute(Message input);
    }

    class ReceivePacket0 extends Transition {
        @Override
        public State execute(Message input) {
            return State.WAIT_FOR_CALL_FROM_ABOVE_1;
        }
    }

    class ReceivePacket1 extends Transition {
        @Override
        public State execute(Message input) {
            return State.WAIT_FOR_CALL_FROM_ABOVE_0;
        }
    }

    class SendAck extends Transition {
        @Override
        public State execute(Message input) {
            int alternatingBit = input == Message.GOT_CALL_FROM_ABOVE_0 ? 1 : 0;
            System.out.println("execute send ack " + alternatingBit);
            try {
                sendAck(alternatingBit);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return alternatingBit == 0 ? State.WAIT_FOR_NEW_PACKET_0 : State.WAIT_FOR_NEW_PACKET_1;
        }
    }

    class ResendAck extends Transition {
        @Override
        public State execute(Message input) {
            int alternatingBit = currentState == State.WAIT_FOR_NEW_PACKET_0 ? 0 : 1;
            System.out.println("execute resend ack " + alternatingBit);
            try {
                sendAck(alternatingBit);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return alternatingBit == 0 ? State.WAIT_FOR_NEW_PACKET_0 : State.WAIT_FOR_NEW_PACKET_1;
        }
    }



    private double calculateGoodput() {
        long timeInMilliseconds = end - start;
        long dataLengthInBytes = new File(pathWithFilename).length();
        double goodPut = ((double )dataLengthInBytes) / (timeInMilliseconds);
        System.out.println("goodPut" + goodPut);

        return goodPut;
    }

}
