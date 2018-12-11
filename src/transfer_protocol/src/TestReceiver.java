package transfer_protocol.src;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class TestReceiver {
    private final CRC32 crc = new CRC32();
    DatagramSocket serverSocket = new DatagramSocket(90);
    private int read;
    private State currentState;
    private final Transition[][] transition;

    public TestReceiver() throws SocketException {

        currentState = State.WAIT_FOR_NEW_PACKET_0;

        transition = new Transition[State.values().length][Message.values().length];
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_0.ordinal()][Message.GOT_CALL_FROM_ABOVE_0.ordinal()] = new SendAck();
        transition[State.WAIT_FOR_CALL_FROM_ABOVE_1.ordinal()][Message.GOT_CALL_FROM_ABOVE_1.ordinal()] = new SendAck();
        transition[State.WAIT_FOR_NEW_PACKET_0.ordinal()][Message.RECEIVED_DUPLICATE.ordinal()] = new ResendAck();
        transition[State.WAIT_FOR_NEW_PACKET_0.ordinal()][Message.RECEIVED_PACKET_0.ordinal()] = new ReceivePacket0();
        transition[State.WAIT_FOR_NEW_PACKET_1.ordinal()][Message.RECEIVED_DUPLICATE.ordinal()] = new ResendAck();
        transition[State.WAIT_FOR_NEW_PACKET_1.ordinal()][Message.RECEIVED_PACKET_1.ordinal()] = new ReceivePacket1();
        System.out.println("INFO Receiver constructed, current state: " + currentState);


    }

    public static void main(String[] args) throws IOException {
        TestReceiver testReceiver = new TestReceiver();
        testReceiver.processMessage(Message.GOT_CALL_FROM_ABOVE_0);

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
        System.out.println("END!!");
    }

    private void waitForNewPacket() throws IOException {
        System.out.println("Waiting for new packet");
        byte[] data = new byte[1024];
        boolean outOfTime = false;
        boolean received = false;
        while (!outOfTime && !received) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                serverSocket.receive(receivePacket);
                crc.update(data, 0, 1016);
                byte[] crcBytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(crc.getValue()).array();
                ByteBuffer buffer = ByteBuffer.wrap(crcBytes, 0, Long.SIZE / Byte.SIZE);
                buffer.put(data, 1016, 8);
                buffer.rewind();
                long receivedCrc = buffer.getLong();

                System.out.println("received crc" + receivedCrc);
                boolean isRightChecksum = receivedCrc == crc.getValue();
                System.out.println("CRC is" + isRightChecksum);
                int alternatingBit = data[1015];
                System.out.println("alternating bit" + alternatingBit);

                currentState = alternatingBit == 1 ? State.WAIT_FOR_CALL_FROM_ABOVE_0 : State.WAIT_FOR_CALL_FROM_ABOVE_1;

                System.out.println("received packet " + new String(data));
                crc.reset();
                received = true;

            } catch (SocketTimeoutException exception) {
                outOfTime = true;
                System.out.println("timeout");
                processMessage(Message.TIMEOUT);
                read = -1;
            }
        }
    }


    public void sendAck(final int alternatingBit) throws IOException {
        try (DatagramSocket sendSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            String ack = "ACK";

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(alternatingBit);
            outputStream.write(ack.getBytes());

            byte fileData[] = outputStream.toByteArray();


            DatagramPacket datagramPacket = new DatagramPacket(fileData, fileData.length, IPAddress, 100);
            sendSocket.send(datagramPacket);
            System.out.println("send");

        }
    }


    public void processMessage(Message input) {
        System.out.println("INFO Received " + input + " in state " + currentState);
        Transition trans = transition[currentState.ordinal()][input.ordinal()];
        if (trans != null) {
            currentState = trans.execute(input);
        }
        System.out.println("INFO State: " + currentState);
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public int getRead() {
        return read;
    }

    enum Message {
        GOT_CALL_FROM_ABOVE_0, GOT_CALL_FROM_ABOVE_1, RECEIVED_PACKET_0, RECEIVED_PACKET_1, RECEIVED_DUPLICATE, TIMEOUT
    }

    enum State {
        WAIT_FOR_NEW_PACKET_0, WAIT_FOR_NEW_PACKET_1, WAIT_FOR_RESENT_PACKET, WAIT_FOR_CALL_FROM_ABOVE_0, WAIT_FOR_CALL_FROM_ABOVE_1
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
            int alternatingBit = input == Message.GOT_CALL_FROM_ABOVE_0 ? 0 : 1;
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

}
