package transfer_protocol.src;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.stream.Stream;
import java.util.zip.CRC32;

public class FileSender {
    private static final int PORT = 90;
    private static final int BUFFER_LENGTH = 1024;
    private static final int TIMEOUT = 15000;
    private static final double PROBABILITY_PACKET_FAILURE = 0.05;
    private final String filename;
    private final String adress;
    private final CRC32 crc = new CRC32();
    private final Transition[][] transition;
    private final FileInputStream fileInputStream;
    private final DatagramSocket sendSocket = new DatagramSocket();
    private byte[] fileData = new byte[1024];
    private State currentState;
    private int read;
    private int alternatingBit = 0;
    private int lastTransmittedBit;
    boolean streamClosed = false;
    boolean finishedSending = false;
    boolean receivedLastAck = false;
    boolean finWasSent = false;
    static boolean processFinished = false;


    public FileSender(String filename, String adress) throws FileNotFoundException, SocketException {
        this.filename = filename;
        this.adress = adress;

        currentState = State.WAIT_FOR_START_CALL;

        transition = new Transition[State.values().length][Message.values().length];
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_0.ordinal()][Message.GOT_CALL_FROM_ABOVE_0.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_1.ordinal()][Message.GOT_CALL_FROM_ABOVE_1.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_0.ordinal()][Message.LAST_PACKET_WAS_TRANSMITTED.ordinal()] = new SendFinPackage();
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_1.ordinal()][Message.LAST_PACKET_WAS_TRANSMITTED.ordinal()] = new SendFinPackage();
        transition[State.WAIT_FOR_ACK_0.ordinal()][Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK_0.ordinal()][Message.RECEIVED_ACK_0.ordinal()] = new ReceiveAck0();
        transition[State.WAIT_FOR_ACK_0.ordinal()][Message.LAST_PACKET_WAS_TRANSMITTED.ordinal()] = new SendFinPackage();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.RECEIVED_ACK_1.ordinal()] = new ReceiveAck1();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.LAST_PACKET_WAS_TRANSMITTED.ordinal()] = new SendFinPackage();
        transition[State.WAIT_FOR_START_CALL.ordinal()][Message.GOT_START_CALL_FROM_ABOVE.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_ACK_START.ordinal()][Message.RECEIVED_ACK_START.ordinal()] = new ReceiveAck1();

        fileInputStream = new FileInputStream(filename);
    }

    public static void main(String[] args) throws IOException {
        FileSender fileSender = new FileSender(args[0], args[1]);
        fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE_0);

        while (!processFinished) {
            if (fileSender.getCurrentState() == State.WAIT_FOR_CALL_FROM_ABOVE_0) {
                fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE_0);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_CALL_FROM_ABOVE_1) {
                fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE_1);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_ACK_0) {
                fileSender.waitForAck(0);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_ACK_1) {
                fileSender.waitForAck(1);
            } else if (fileSender.currentState == State.WAIT_FOR_ACK_FIN) {
                fileSender.waitForAck(0);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_START_CALL) {
                fileSender.processMessage(Message.GOT_START_CALL_FROM_ABOVE);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_ACK_START) {
                fileSender.waitForAck(1);
            }
        }
    }


    public void sendPacket(boolean packetIsNew, int alternatingBit) throws IOException {
        InetAddress IPAddress = InetAddress.getByName(adress);

        if (finishedSending || currentState == State.WAIT_FOR_ACK_FIN) {
            generateFinPacket();
        } else if (packetIsNew) {
            if (currentState == State.WAIT_FOR_START_CALL) {
                generateStartPacket();
            } else {
                generateNewDataPacket();
            }

        }
        addHeaderToPacket((byte) alternatingBit);

        DatagramPacket datagramPacket = new DatagramPacket(fileData, BUFFER_LENGTH, IPAddress, PORT);

        if (!streamClosed) {
            sendSocket.send(datagramPacket);
        }
        crc.reset();

        if (read < 1015 && currentState != State.WAIT_FOR_START_CALL && !finishedSending) {
            finishedSending = true;
            lastTransmittedBit = alternatingBit;
        }


        if (currentState != State.WAIT_FOR_ACK_FIN && !finWasSent) {
            currentState = alternatingBit == 0 ? State.WAIT_FOR_ACK_0 : (alternatingBit == 1 ? State.WAIT_FOR_ACK_1 : State.WAIT_FOR_ACK_START);
        }
        if (finWasSent) {
            currentState = State.WAIT_FOR_ACK_FIN;
        }

    }

    private void generateFinPacket() {
        fileData = new byte[BUFFER_LENGTH];
        int bit = lastTransmittedBit == 0 ? 1 : 0;
        System.out.println("send fin with bit" + bit);
        // fileData[1015] = (byte) (lastTransmittedBit == 0 ? 1 : 0);
        finWasSent = true;
        currentState = State.WAIT_FOR_ACK_FIN;
        crc.update(fileData);
    }

    /**
     * put crc long in an byte array with length 8 and send the checksum with data as the last 8 bytes (1016 - 1032) in the array fi le data
     **/
    private void addHeaderToPacket(byte alternatingBit) {
        fileData[1015] = alternatingBit;

        byte[] crcBytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(crc.getValue()).array();
        Stream.iterate(0, i -> i + 1).limit(8).forEach(i -> fileData[1016 + i] = crcBytes[i]);
    }

    private void generateNewDataPacket() {
        try {
            fileData = new byte[BUFFER_LENGTH];
            read = fileInputStream.read(fileData, 0, 1015);
        } catch (IOException ioException) {
            streamClosed = true;
        }

        crc.update(fileData);
    }

    private void generateStartPacket() {
        fileData = new byte[BUFFER_LENGTH];
        byte[] startMessage = filename.getBytes();
        Stream.iterate(0, i -> i + 1).limit(startMessage.length).forEach(i -> fileData[i] = startMessage[i]);
    }


    public void waitForAck(int alternatingBit) throws IOException {
        int n = lastTransmittedBit == 1 ? 0 : 1;
        try (DatagramSocket receiveSocket = new DatagramSocket(100);) {

            byte[] ackData = new byte[BUFFER_LENGTH];
            boolean outOfTime = false;
            boolean received = false;
            receiveSocket.setSoTimeout(TIMEOUT);
            while (!outOfTime && !received) {
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(ackData, BUFFER_LENGTH);
                    receiveSocket.receive(datagramPacket);
                    String answer = new String(ackData);

                    int receivedBit = (int) ackData[1015];
                    if (alternatingBit == 1 && answer.contains("ACK") && receivedBit == 1 || alternatingBit == 0 && answer.contains("ACK") && receivedBit == 0 || currentState == State.WAIT_FOR_ACK_FIN && lastTransmittedBit == 0 && receivedBit == 0 || currentState == State.WAIT_FOR_ACK_FIN || lastTransmittedBit == 1 && receivedBit == 1) {
                        System.out.println("test");
                        received = true;
                        System.out.println("received bit" + receivedBit);
                        System.out.println("lastTransmittedBit" + lastTransmittedBit);
                        if (currentState != State.WAIT_FOR_ACK_FIN) {
                            currentState = alternatingBit == 1 ? State.WAIT_FOR_CALL_FROM_ABOVE_0 : State.WAIT_FOR_CALL_FROM_ABOVE_1;
                        }
                        if (currentState == State.WAIT_FOR_ACK_FIN) {
                            System.out.println("Received Ack Fin " + receivedBit);
                            sendSocket.close();
                            processFinished = true;
                            // System.exit(1);
                            //System.out.println("close send socket");
                        } else if (finishedSending && currentState != State.WAIT_FOR_ACK_FIN) {
                            receivedLastAck = true;
                            processMessage(Message.LAST_PACKET_WAS_TRANSMITTED);
                            System.out.println("received last ack");
                        }
                    }
                } catch (SocketTimeoutException exception) {
                    outOfTime = true;
                    System.out.println("timeout");
                    processMessage(Message.TIMEOUT);
                }
            }
        }
    }


    enum State {
        WAIT_FOR_CALL_FROM_ABOVE_0, WAIT_FOR_CALL_FROM_ABOVE_1, WAIT_FOR_ACK_0, WAIT_FOR_ACK_1, WAIT_FOR_START_CALL, WAIT_FOR_ACK_START, WAIT_FOR_ACK_FIN
    }


    enum Message {
        GOT_CALL_FROM_ABOVE_0, GOT_CALL_FROM_ABOVE_1, TIMEOUT, RECEIVED_ACK_0, RECEIVED_ACK_1, GOT_START_CALL_FROM_ABOVE, RECEIVED_ACK_START,
        LAST_PACKET_WAS_TRANSMITTED
    }


    public void processMessage(Message input) throws IOException {
        System.out.println("INFO Received " + input + " in state " + currentState);
        Transition trans = transition[currentState.ordinal()][input.ordinal()];
        if (trans != null) {
            currentState = trans.execute(input);
        }
    }


    abstract class Transition {
        abstract public State execute(Message input) throws IOException;
    }

    class SendNewPackage extends Transition {
        @Override
        public State execute(Message input) throws IOException {
            int alternatingBit = input == Message.GOT_CALL_FROM_ABOVE_0 ? 0 : 1;
            System.out.println("execute send packet " + alternatingBit);
            sendPacket(true, alternatingBit);
            State returnState = alternatingBit == 0 ? State.WAIT_FOR_ACK_0 : State.WAIT_FOR_ACK_1;
            return returnState;
        }
    }


    class SendFinPackage extends Transition {

        @Override
        public State execute(Message input) throws IOException {
            System.out.println("send fin package");
            int alternatingBit = input == Message.GOT_CALL_FROM_ABOVE_0 ? 0 : 1;
            sendPacket(true, alternatingBit);
            State returnState = State.WAIT_FOR_ACK_FIN;
            return returnState;
        }
    }

    class ResendPackage extends Transition {
        @Override
        public State execute(Message input) throws IOException {
            int alternatingBit = input == Message.GOT_CALL_FROM_ABOVE_0 ? 0 : 1;
            System.out.println("execute resend package " + alternatingBit);
            sendPacket(false, alternatingBit);
            final State returnState = alternatingBit == 0 ? State.WAIT_FOR_ACK_0 : State.WAIT_FOR_ACK_1;
            return State.WAIT_FOR_ACK_0;
        }
    }

    class ReceiveAck0 extends Transition {
        @Override
        public State execute(Message input) {
            System.out.println("Wait for call 1 from above");
            return State.WAIT_FOR_CALL_FROM_ABOVE_1;
        }
    }

    class ReceiveAck1 extends Transition {
        @Override
        public State execute(Message input) {
            System.out.println("Wait for call 0 from above");
            return State.WAIT_FOR_CALL_FROM_ABOVE_0;
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public int getRead() {
        return read;
    }

    public CRC32 getCrc() {
        return crc;
    }
}
