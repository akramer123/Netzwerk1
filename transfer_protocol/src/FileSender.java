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
    private final byte[] fileData = new byte[1024];
    private State currentState;
    private int read;
    private byte alternatingBit;



    public FileSender(String filename, String adress) throws FileNotFoundException, SocketException {
        this.filename = filename;
        this.adress = adress;

        currentState = State.WAIT_FOR_CALL_FROM_ABOVE;

        transition = new Transition[State.values().length][Message.values().length];
        transition[State.WAIT_FOR_CALL_FROM_ABOVE.ordinal()][Message.GOT_CALL_FROM_ABOVE.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_ACK_0.ordinal()][Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK_0.ordinal()][Message.RECEIVED_ACK_0.ordinal()] = new ReceiveAck();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK_1.ordinal()][Message.RECEIVED_ACK_1.ordinal()] = new ReceiveAck();
        System.out.println("INFO Sender constructed, current state: " + currentState);

        fileInputStream = new FileInputStream(filename);
    }

    public static void main(String[] args) throws IOException {
        FileSender fileSender = new FileSender(args[0], args[1]);
        fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE);

        while (fileSender.getRead() != -1) {
            if (fileSender.getCurrentState() == State.WAIT_FOR_CALL_FROM_ABOVE) {
                fileSender.processMessage(Message.GOT_CALL_FROM_ABOVE);
            }
            else if (fileSender.getCurrentState() == State.WAIT_FOR_ACK_0) {
                    fileSender.waitForAck((byte) 0);
            }

        }
        System.out.println("CRC" + fileSender.getCrc().getValue());
    }


    public void sendPacket(boolean packetIsNew) throws IOException {
        InetAddress IPAddress = InetAddress.getByName(adress);

        /**send new data, if ack for old data was received**/
        if (packetIsNew) {
            read = fileInputStream.read(fileData,0, 1016);
            crc.update(fileData);

            /**put crc long in an byte array with length 8 and send the checksum with data as the last 8 bytes (1016 - 1032) in the array file data**/
            byte[] crcBytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(crc.getValue()).array();
            Stream.iterate(0,i-> i+1).limit(8).forEach(i -> fileData[1016+i] = crcBytes[i]);
            System.out.println("crc single packets" + crc.getValue());
            System.out.println("crcBytes.length" + crcBytes.length);



        }

        /**send original packet or packet with a bit failure**/
        DatagramPacket datagramPacket;
        if (!decideToSendPacketFailure()) {
             datagramPacket = new DatagramPacket(fileData, BUFFER_LENGTH, IPAddress, PORT);
        }
        else {
             datagramPacket = new DatagramPacket(generatePacketFailure(fileData), BUFFER_LENGTH, IPAddress, PORT);
            System.out.println("generated packet failure");
        }
        sendSocket.send(datagramPacket);
        crc.reset();
        //read = fileInputStream.read(fileData);



    currentState =State.WAIT_FOR_ACK_0;
}


    public void waitForAck(byte alternatingBit) throws IOException {
        System.out.println("Waiting for ACK");
        try(DatagramSocket receiveSocket = new DatagramSocket(100);) {
            byte[] ackData = new byte[1024];
            boolean outOfTime = false;
            boolean received = false;
            receiveSocket.setSoTimeout(TIMEOUT);
            while(!outOfTime  && !received) {
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(ackData, BUFFER_LENGTH);
                    receiveSocket.receive(datagramPacket);
                    String answer = new String(ackData);

                        if (alternatingBit == 1 && answer.contains("ACK1") || alternatingBit== 0 && answer.contains("ACK0")) {
                        received = true;
                        System.out.println("received ack" + answer);
                        currentState = State.WAIT_FOR_CALL_FROM_ABOVE;

                    }
                } catch (SocketTimeoutException exception) {
                    outOfTime = true;
                    System.out.println("timeout");
                    processMessage(Message.TIMEOUT);
                }
            }
            }
        }


        /**decides if a bit failure should be generated using a given probability**/
        public boolean decideToSendPacketFailure() {
            return Math.random() <= PROBABILITY_PACKET_FAILURE;
        }


        /**generates a bit failure**/
        public byte[] generatePacketFailure(byte[] originalPacket) {
        //TODO Bitfehler an einer zufaelligen Stelle erzeugen
        final int failureBytePosition = 2;
        final int flipPosition = 5;
        byte[] failurePacket = originalPacket.clone();
        byte failureByte = originalPacket[failureBytePosition];
            failureByte =  (byte) (failureByte ^ (1 << flipPosition));
            failurePacket[failureByte] = failureByte;
        return failurePacket;
        }

   enum State {
       WAIT_FOR_CALL_FROM_ABOVE, WAIT_FOR_ACK_0, WAIT_FOR_ACK_1
   };

    enum Message {
        GOT_CALL_FROM_ABOVE, TIMEOUT, RECEIVED_ACK_0, RECEIVED_ACK_1
    }

    /**
     * Process a message (a condition has occurred).
     * @param input Message or condition that has occurred.
     */
    public void processMessage(Message input) throws IOException {
        System.out.println("INFO Received "+input+" in state "+currentState);
        Transition trans = transition[currentState.ordinal()][input.ordinal()];
        if(trans != null){
            currentState = trans.execute(input);
        }
        System.out.println("INFO State: "+currentState);
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
            System.out.println("execute send packet");
            sendPacket(true);
            return State.WAIT_FOR_ACK_0;
        }
    }

    class ResendPackage extends Transition {
        @Override
        public State execute(Message input) throws IOException {
            System.out.println("execute resend package");
            sendPacket(false);
            return State.WAIT_FOR_ACK_0;
        }
    }

    class ReceiveAck extends Transition {
        @Override
        public State execute(Message input) {
            System.out.println("Wait for call from above again.");
            return State.WAIT_FOR_CALL_FROM_ABOVE;
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
