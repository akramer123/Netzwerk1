package transfer_protocol.src;

import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.CRC32;

public class TestReceiver {
    private final CRC32 crc = new CRC32();
  //  private DatagramSocket serverSocket = new DatagramSocket(90);
    private SocketFilter serverSocket = new SocketFilter(90);
    private static final int BUFFER_LENGTH = 1024;
    private int read;
    private State currentState;
    private final Transition[][] transition;
    private String fileName;
    private String receiveFileName;
    private static FileOutputStream fileDataWriter;
    private String path;
    private static long start;
    private static long end;
    private static long dataLength;



    public TestReceiver() throws SocketException {
        path =  System.getProperty("user.dir");
        String[] pathElements = path.split("\\\\");
        path = "";
        for (int i = 0; i < pathElements.length; i++) {
            System.out.println(pathElements[i]);
            path = path +pathElements[i] +  "\\\\";
        }
        System.out.println(path);

        currentState = State.WAIT_FOR_NEW_PACKET_0;

        transition = new Transition[State.values().length][Message.values().length];
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_0.ordinal()][Message.GOT_CALL_FROM_ABOVE_0.ordinal()] = new SendAck();
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_1.ordinal()][Message.GOT_CALL_FROM_ABOVE_1.ordinal()] = new SendAck();
        transition[State.WAIT_FOR_NEW_PACKET_0.ordinal()][Message.RECEIVED_DUPLICATE.ordinal()] = new ResendAck();
        transition[State.WAIT_FOR_NEW_PACKET_0.ordinal()][Message.RECEIVED_PACKET_0.ordinal()] = new ReceivePacket0();
        transition[State.WAIT_FOR_NEW_PACKET_1.ordinal()][Message.RECEIVED_DUPLICATE.ordinal()] = new ResendAck();
        transition[State.WAIT_FOR_NEW_PACKET_1.ordinal()][Message.RECEIVED_PACKET_1.ordinal()] = new ReceivePacket1();
        transition[State.WAIT_FOR_START_CALL.ordinal()][Message.GOT_START_CALL_FROM_ABOVE.ordinal()] = new ReceivePacket1();
        System.out.println("INFO Receiver constructed, current state: " + currentState);
    }

    public static void main(String[] args) throws IOException {

        start = System.currentTimeMillis();

        TestReceiver testReceiver = new TestReceiver();
        testReceiver.processMessage(Message.GOT_START_CALL_FROM_ABOVE);

        while (testReceiver.getRead() != -1) {
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
        //System.out.println("Waiting for new packet");
        byte[] data = new byte[BUFFER_LENGTH];
        boolean outOfTime = false;
        boolean received = false;
        while (!outOfTime && !received && read != -1) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                serverSocket.receive(receivePacket);
                int alternatingBit = data[1015];
                Map<String, Boolean> header = readHeader(data, alternatingBit);
                Boolean correctState = header.get("correctState");
                Boolean correctChecksum = header.get("crc");

                int count = (int) Stream.iterate(0, i -> i + 1).limit(1014).filter(i -> data[i] != 0).count();
                //System.out.println("count: " + count + "    correctState: " + correctState);
                if (count == 0) {
                   read = -1;
                  //  if (correctChecksum) {
                        System.out.println("Send fin ack");
                  //  }
            /*        else {
                        System.out.println("fin ack packet failure");
                    }*/
                    sendAck(alternatingBit);
                }


/*                if (new String(data).contains("closeConnection")){
                    System.out.println("closeConnection");
                    read = -1;
                }*/

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
                        String[] splitFileName = fileName.split("\\.");
                         receiveFileName = splitFileName[0] + "2." + splitFileName[1];
                        System.out.println(receiveFileName);
                        fileDataWriter = new FileOutputStream(path + receiveFileName);
                    } else {
                        fileDataWriter.write(fileData);
/*                        for (byte b : fileData) {
                            System.out.print(b);

                        }*/
                        System.out.println(new String(fileData));
                        System.out.println("-------------------------------------------------------------------------");
                    }
                    //System.out.println("received packet " + new String(fileData));

                    received = true;
                } else if (count != 0) {
                    processMessage(Message.RECEIVED_DUPLICATE);
                }

            } catch (SocketTimeoutException exception) {
                outOfTime = true;
                System.out.println("timeout");
                processMessage(Message.TIMEOUT);
                read = -1;
            }
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
        //System.out.println("INFO Received " + input + " in state " + currentState);
        Transition trans = transition[currentState.ordinal()][input.ordinal()];
        if (trans != null) {
            currentState = trans.execute(input);
        }
        //System.out.println("INFO State: " + currentState);
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

        long dataLengthInBytes = new File(path + receiveFileName).length();
        System.out.println(path + receiveFileName);
        System.out.println("file size" + dataLengthInBytes);
        double goodPut = ((double )dataLengthInBytes) / (timeInMilliseconds);
        System.out.println("goodPut" + goodPut);

        return goodPut;
    }

}
