import java.io.*;
import java.net.*;

public class FileSender {
    private  final String filename;
    private  final String adress;
    private static final int PORT = 90;
    private static final int BUFFER_LENGTH = 1024;
    private static final int TIMEOUT = 30000;


    public FileSender(String filename, String adress){
        this.filename =filename ;
        this.adress = adress;

        currentState = State.WAIT_FOR_CALL_FROM_ABOVE;
        // define all valid state transitions for our state machine
        // (undefined transitions will be ignored)
        transition = new Transition[State.values().length] [Message.values().length];
        transition[State.WAIT_FOR_CALL_FROM_ABOVE.ordinal()] [Message.GOT_CALL_FROM_ABOVE.ordinal()] = new SendNewPackage();
        transition[State.WAIT_FOR_ACK.ordinal()] [Message.TIMEOUT.ordinal()] = new ResendPackage();
        transition[State.WAIT_FOR_ACK.ordinal()] [Message.RECEIVED_ACK.ordinal()] = new ReceiveAck();
        System.out.println("INFO Sender constructed, current state: "+currentState);
    }

    public static void main(String[] args) throws IOException {
        FileSender fileSender = new FileSender(args[0],args[1]);
        fileSender.processMsg(Message.GOT_CALL_FROM_ABOVE);
    }


    // all states for this FSM
    enum State {
        WAIT_FOR_CALL_FROM_ABOVE, WAIT_FOR_ACK
    };
    // all messages/conditions which can occur
    enum Message {
        GOT_CALL_FROM_ABOVE, TIMEOUT, RECEIVED_ACK
    }
    // current state of the FSM
    private State currentState;
    // 2D array defining all transitions that can occur
    private Transition[][] transition;


    public void sendPacket() throws IOException {
        try(FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Kristina\\Desktop\\Studium\\Netzwerke\\Netzwerk1\\transfer_protocol\\FileSenderTest.txt");
            DatagramSocket sendSocket = new DatagramSocket();) {
            InetAddress IPAddress = InetAddress.getByName(adress);
            byte[] fileData = new byte[1024];
            int read = fileInputStream.read(fileData);


            while (read != -1) {
                DatagramPacket datagramPacket = new DatagramPacket(fileData, BUFFER_LENGTH, IPAddress, PORT);
                sendSocket.send(datagramPacket);
                read = fileInputStream.read(fileData);
                System.out.println("read" + read);
            }
        }
        processMsg(Message.RECEIVED_ACK);
    }


    public void waitForAck() throws IOException {
        System.out.println("Waiting for ACK");
        try(DatagramSocket receiveSocket = new DatagramSocket(100);) {
            byte[] ackData = new byte[1024];
            boolean outOfTime = false;
            boolean received = false;
            receiveSocket.setSoTimeout(TIMEOUT);
            while(!outOfTime  && !received) {
                try {
                    System.out.println("received");
                    DatagramPacket datagramPacket = new DatagramPacket(ackData, BUFFER_LENGTH);
                    receiveSocket.receive(datagramPacket);
                    String answer = new String(ackData);
                    System.out.println("answer" + answer);
                    if (answer.equals("ACK")) {
                        received = true;
                        processMsg(Message.RECEIVED_ACK);


                    }
                } catch (SocketTimeoutException exception) {
                    outOfTime = true;
                    System.out.println("Timeout");
                    processMsg(Message.TIMEOUT);
                }
            }
            }
        }
   // }

    /**
     * Process a message (a condition has occurred).
     * @param input Message or condition that has occurred.
     */
    public void processMsg(Message input) throws IOException {
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
            sendPacket();
            waitForAck();
            return State.WAIT_FOR_ACK;
        }
    }


    class ResendPackage extends Transition {
        @Override
        public State execute(Message input) throws IOException {
            sendPacket();
            return State.WAIT_FOR_ACK;
        }
    }


    class ReceiveAck extends Transition {
        @Override
        public State execute(Message input) {
            return State.WAIT_FOR_CALL_FROM_ABOVE;
        }
    }

}
