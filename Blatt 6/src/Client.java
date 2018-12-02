import java.io.*;
import java.net.*;

public class Client extends Thread {
        private static final int RUNNING_TIME = 25_000;
        private static int DELAY;
        private static int N;
        private static final int PACKET_LENGTH = Constants.PACKET_LENGTH;
        private static final int PORT = Constants.PORT;
        private static  String ADRESS;
        private  boolean isUDP;
        private int dataLength;
        private int packetCounter = 0;
        private  String protocol;
        private  BitRateTest bitRateTest;
        private static double[] sendDataRate = new double[Constants.TEST_REPEATS];
        private static int i = 0;


        public Client(int n, int delay, String protocol, String adress){
            this.N = n;
            this.DELAY = delay;
            this.protocol = protocol;
            this.ADRESS = adress;
        };

        public Client(int n, int delay, BitRateTest bitRateTest,String protocol, String adress){
            this.N = n;
            this.DELAY = delay;
            this.protocol = protocol;
            this.ADRESS = adress;
            this.bitRateTest = bitRateTest;

        };


    // console input: adress protocol  n k
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client(Integer.parseInt(args[2]),Integer.parseInt(args[3]),args[1], args[0]);
        System.out.println("k = " + DELAY + " n = " + N);
        client.calculateExpectedSendRate();
        double sent = client.sendPacket(args[1]);
        client.putSendDataRate(sent);
    }



    public double sendPacket(String protocol) throws IOException, InterruptedException {
        isUDP = protocol.equals(Constants.UDP);
        long start = System.currentTimeMillis();
        long expectedStop = start + RUNNING_TIME;
        long realStop;
        byte[] sendData = new byte[PACKET_LENGTH];
        realStop =  isUDP ? sendPacketsUDP(start, expectedStop, sendData) : sendPacketsTCP(start, expectedStop) ;
        return calculateRealSendRate(start, realStop);
    }

    private long sendPacketsUDP(long start, long expectedStop, byte[] sendData) throws IOException, InterruptedException {
             dataLength = 0;
             packetCounter = 0;
             for (int i = 0; i < Constants.TEST_REPEATS; i++) {
                 try (DatagramSocket clientSocket = new DatagramSocket();) {
                     InetAddress IPAddress = InetAddress.getByName(ADRESS);
                     while (System.currentTimeMillis() < expectedStop) {
                         DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
                         clientSocket.send(sendPacket);
                         packetCounter = packetCounter + 1;
                         if (N != 0 && packetCounter % N == 0) {
                             Thread.sleep(DELAY);
                         }
                     }
                 }
             }
        long realStop = System.currentTimeMillis();
        return realStop;
        }


        private long sendPacketsTCP (long start, long expectedStop) throws IOException, InterruptedException {
            try( Socket clientSocket = new Socket(ADRESS, 90);
                OutputStream outputStream = clientSocket.getOutputStream()) { ;
                byte[] sendData = new byte[PACKET_LENGTH];
                InetAddress IPAddress = InetAddress.getByName(ADRESS);

                while (System.currentTimeMillis() < expectedStop) {
                    outputStream.write(sendData);
                    outputStream.flush();
                    packetCounter = packetCounter + 1;
                    if (N != 0 && packetCounter % N == 0) {
                        Thread.sleep(DELAY);
                    }
                }
            }
            long realStop = System.currentTimeMillis();
            System.out.println(packetCounter);
            return realStop;
        }



    private double calculateRealSendRate(long start, long stop) {
        double timeInSeconds = (stop - start) / Constants.FACTOR_KILO;
        long dataLengthInKBit = (packetCounter * PACKET_LENGTH);
        dataLengthInKBit = (dataLengthInKBit* Constants.FACTOR_BYTES_TO_BITS) / Constants.FACTOR_KILO;
        double realSendRate = dataLengthInKBit / timeInSeconds;
        sendDataRate[i] = realSendRate;
        i++;
        System.out.println("Real Send Rate  is: " + realSendRate);
        return realSendRate ;

    }


    public static double calculateExpectedSendRate() {
        double delayInSeconds = DELAY  / (double )Constants.FACTOR_KILO;
        System.out.println(DELAY);
        int dataLengthInKBit = (PACKET_LENGTH * N * Constants.FACTOR_BYTES_TO_BITS) / Constants.FACTOR_KILO;
        double calculatedSendRate = delayInSeconds == 0 ? 0 :  dataLengthInKBit / delayInSeconds;
        System.out.println("Expected Send Rate  is: " + calculatedSendRate);
        return calculatedSendRate;
    }


    public  void putSendDataRate(double dataRate) {
        sendDataRate[i] = dataRate;
        i++;
    }


    public static void printSendDataRate() {
        System.out.println("Send Data Rate");
        for (int j = 0; j < Constants.TEST_REPEATS; j++) {
            System.out.print(sendDataRate[i] + " ");
        }
        System.out.println();
    }

    public static double[] getSendDataRate() {
        return sendDataRate;
    }

    public static void resetIndex() {
        i = 0;
    }


    public void run() {
        try {
            sendPacket(protocol);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}


