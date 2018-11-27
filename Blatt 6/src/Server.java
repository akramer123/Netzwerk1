import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class Server extends Thread {
        private  static final int PACKET_LENGTH = 1_400;
        private  static final int PORT = 90;
        private  static final int TIMEOUT = 10_000;
        private int dataLength = 0;
        boolean outOfTime = false;
     //   private   BitRateTest bitRateTest;
        private   String protocol;
        private  static double[] receiveDataRate = new double[Constants.TEST_REPEATS];
        private static int i = 0;

        public static void main(String[] args) throws IOException {
            Server server = new Server(Constants.UDP);
            server.start();

        }

        public Server(String protocol) {
            this.protocol = protocol;
        }
/*
        public Server(BitRateTest bitRateTest, String protocol) {
             this.bitRateTest = bitRateTest;
             this.protocol = protocol;
        }
*/


        public long receivePackets(String protocol) throws IOException {
            byte[] receiveData = new byte[PACKET_LENGTH];
            long start = System.currentTimeMillis();
            long stop = protocol.equals(Constants.UDP) ? receivePacketsUDP(start, receiveData) : receivePacketsTCP(start, receiveData);
            long dataRate = calculateDataRate(start, stop);
            System.out.println("Receive Data Rate: " + dataRate);
            System.out.println();
            return dataRate ;
        }

        private  long receivePacketsUDP(long start, byte[] receiveData) throws IOException {
        outOfTime = false;
        dataLength = 0;
              try(DatagramSocket serverSocket = new DatagramSocket(PORT);) {
                  serverSocket.setSoTimeout(TIMEOUT);
                  while (!outOfTime) {
                      try {
                          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                          serverSocket.receive(receivePacket);
                          dataLength = dataLength + receivePacket.getLength();
                      } catch (SocketTimeoutException exception) {
                          outOfTime = true;
                      }
                  }
              }
        return System.currentTimeMillis();
       }

    private  long receivePacketsTCP(long start, byte[] receiveData) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        serverSocket.setSoTimeout(TIMEOUT);
        boolean isConnected = true;
        while (! outOfTime && isConnected) {
            try {
                InputStream inputStream = socket.getInputStream();
                try {
                    inputStream.read(receiveData);
                }
                catch(SocketException socketException) {
                    isConnected = false;
                }
                dataLength = dataLength + receiveData.length;
            }
            catch(SocketTimeoutException exception) {
                outOfTime = true;
            }
        }
        System.out.println("log" + socket.getInputStream().read());
        return System.currentTimeMillis();
    }

        public long calculateDataRate(long start, long stop) {
            long timeInSeconds = (stop - start) / Constants.FACTOR_KILO;
            int dataLengthInKBit = (dataLength * Constants.FACTOR_BYTES_TO_BITS) / Constants.FACTOR_KILO;
            long dataRate = outOfTime && dataLength == 0 || timeInSeconds == 0? 0 : dataLengthInKBit / timeInSeconds;
            return dataRate;
        }




    @Override
    public void run() {
        try {
            long received = receivePackets(protocol);
            putReceiveDataRate(received);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  void putReceiveDataRate(double dataRate) {
        receiveDataRate[i] = dataRate;
         i = i +1 ;
    }


    public static void printReceiveDataRate() {
        System.out.println("Receive Data Rate");
        for (int i = 0; i < Constants.TEST_REPEATS; i++) {
            System.out.print(receiveDataRate[i] + " ");
        }

    }


}
