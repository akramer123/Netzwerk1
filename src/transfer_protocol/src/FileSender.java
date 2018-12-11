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
    private static final String START_TRANSFER = "START";
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
    boolean streamClosed = false;
    boolean finishedSending = false;
    boolean receivedLastAck;


    public FileSender(String filename, String adress) throws FileNotFoundException, SocketException {
        this.filename = filename;
        this.adress = adress;

        currentState = State.WAIT_FOR_START_CALL;

        transition = new Transition[State.values().length][Message.values().length];
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_0.ordinal()][Message.GOT_CALL_FROM_ABOVE_0.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_1.ordinal()][Message.GOT_CALL_FROM_ABOVE_1.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_ACK_0.ordinal()][Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK_0.ordinal()][Message.RECEIVED_ACK_0.ordinal()] = new ReceiveAck0();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.RECEIVED_ACK_1.ordinal()] = new ReceiveAck1();
        transition[State.WAIT_FOR_START_CALL.ordinal()][Message.GOT_START_CALL_FROM_ABOVE.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_ACK_START.ordinal()][Message.RECEIVED_ACK_START.ordinal()] = new ReceiveAck1();

        fileInputStream = new FileInputStream(filename);
    }

    public static void main(String[] args) throws IOException {
        FileSender fileSender = new FileSender(args[0], args[1]);
        fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE_0);

        while (fileSender.getRead() != -1) {
            if (fileSender.getCurrentState() == State.WAIT_FOR_CALL_FROM_ABOVE_0) {
                fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE_0);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_CALL_FROM_ABOVE_1) {
                fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE_1);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_ACK_0) {
                fileSender.waitForAck(0);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_ACK_1) {
                fileSender.waitForAck(1);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_START_CALL) {
                fileSender.processMessage(Message.GOT_START_CALL_FROM_ABOVE);
            } else if (fileSender.getCurrentState() == State.WAIT_FOR_ACK_START) {
                fileSender.waitForAck(1);
            }


        }
    }


    public void sendPacket(boolean packetIsNew, int alternatingBit) throws IOException {
        InetAddress IPAddress = InetAddress.getByName(adress);
        if (!finishedSending) {
            /**send new data, if ack for old data was received**/
            if (packetIsNew) {
                System.out.println("new packet");
                if (currentState == State.WAIT_FOR_START_CALL) {
                    fileData = new byte[BUFFER_LENGTH];
                    byte[] startMessage = filename.getBytes();
                    Stream.iterate(0, i -> i + 1).limit(startMessage.length).forEach(i -> fileData[i] = startMessage[i]);
                    System.out.println("started connection");
                } else {
                    try {

                        fileData = new byte[BUFFER_LENGTH];
                        read = fileInputStream.read(fileData, 0, 1015);
                        System.out.println(new String(fileData));
                        //  System.out.println("read length"+ read);
                    } catch (IOException ioException) {
                        streamClosed = true;
                    }
                    for (int i = 0; i < fileData.length; i++) {
                        System.out.print(fileData[i] + " ");

                    }
                    crc.update(fileData);
                }
                /**add alternating bit to data
                 * **/
                fileData[1015] = (byte) alternatingBit;
                /**put crc long in an byte array with length 8 and send the checksum with data as the last 8 bytes (1016 - 1032) in the array file data**/
                byte[] crcBytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(crc.getValue()).array();
                Stream.iterate(0, i -> i + 1).limit(8).forEach(i -> fileData[1016 + i] = crcBytes[i]);
            }

            /**send original packet or packet with a bit failure**/
            DatagramPacket datagramPacket;
            if (!decideToSendPacketFailure()) {
                datagramPacket = new DatagramPacket(fileData, BUFFER_LENGTH, IPAddress, PORT);
            } else {
                datagramPacket = new DatagramPacket(generatePacketFailure(fileData), BUFFER_LENGTH, IPAddress, PORT);
                System.out.println("generated packet failure");
            }

            if (!streamClosed && !finishedSending) {
                System.out.println("send packet");
                sendSocket.send(datagramPacket);

            }

            crc.reset();

            System.out.println("read" + read);
            if (read < 1015 && currentState != State.WAIT_FOR_START_CALL) {
                finishedSending = true;
                System.out.println("Finished Sending");
                //TODO Verbindung schliessen und  an Receiver senden
                System.exit(1);
            }
            if (finishedSending) {
                System.out.println("closed input stream");
                fileInputStream.close();
                sendSocket.close();

            }

            currentState = alternatingBit == 0 ? State.WAIT_FOR_ACK_0 : (alternatingBit == 1 ? State.WAIT_FOR_ACK_1 : State.WAIT_FOR_ACK_START);
        }
    }


    public void waitForAck(int alternatingBit) throws IOException {
        System.out.println("Waiting for ACK");
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
                    if (alternatingBit == 1 && answer.contains("ACK") && receivedBit == 1 || alternatingBit == 0 && answer.contains("ACK") && receivedBit == 0) {
                        received = true;
                        System.out.println("received ack" + answer);
                        currentState = alternatingBit == 1 ? State.WAIT_FOR_CALL_FROM_ABOVE_0 : State.WAIT_FOR_CALL_FROM_ABOVE_1;
                        if (finishedSending) {
                            receivedLastAck = true;
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


    /**
     * decides if a bit failure should be generated using a given probability
     **/
    public boolean decideToSendPacketFailure() {
        return Math.random() <= PROBABILITY_PACKET_FAILURE;
    }


    /**
     * generates a bit failure
     **/
    public byte[] generatePacketFailure(byte[] originalPacket) {
        //TODO Bitfehler an einer zufaelligen Stelle erzeugen
        final int failureBytePosition = 2;
        final int flipPosition = 5;
        byte[] failurePacket = originalPacket.clone();
        byte failureByte = originalPacket[failureBytePosition];
        failureByte = (byte) (failureByte ^ (1 << flipPosition));
        failurePacket[failureByte] = failureByte;
        return failurePacket;
    }


    public void flipAlternatingBit() {
        alternatingBit = alternatingBit == 1 ? 0 : 1;
    }

    enum State {
        WAIT_FOR_CALL_FROM_ABOVE_0, WAIT_FOR_CALL_FROM_ABOVE_1, WAIT_FOR_ACK_0, WAIT_FOR_ACK_1, WAIT_FOR_START_CALL, WAIT_FOR_ACK_START
    }

    ;

    enum Message {
        GOT_CALL_FROM_ABOVE_0, GOT_CALL_FROM_ABOVE_1, TIMEOUT, RECEIVED_ACK_0, RECEIVED_ACK_1, GOT_START_CALL_FROM_ABOVE, RECEIVED_ACK_START
    }

    /**
     * Process a message (a condition has occurred).
     *
     * @param input Message or condition that has occurred.
     */
    public void processMessage(Message input) throws IOException {
        System.out.println("INFO Received " + input + " in state " + currentState);
        Transition trans = transition[currentState.ordinal()][input.ordinal()];
        if (trans != null) {
            currentState = trans.execute(input);
        }
        System.out.println("INFO State: " + currentState);
    }

    /**
     * Abstract base class for all transitions.
     * Derived classes need to override execute thereby defining the action
     * to be performed whenever this transition occurs.
     */
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
