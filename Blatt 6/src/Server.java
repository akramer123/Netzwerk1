import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class Server extends Thread {
        private  static final int PACKET_LENGTH = 1_400;
        private  static final int PORT = 90;
        private  static final int TIMEOUT = 20_000;
        private int dataLength = 0;
        boolean outOfTime = false;
        private   BitRateTest bitRateTest;
        private   String protocol;
        private  static double[] receiveDataRate = new double[Constants.TEST_REPEATS];
        private static int i = 0;

        public static void main(String[] args) throws IOException {
            Server server = new Server(args[0]);
            server.start();
        }

        public Server(String protocol) {
            this.protocol = protocol;
        }
        public Server(BitRateTest bitRateTest, String protocol) {
             this.bitRateTest = bitRateTest;
             this.protocol = protocol;
        }

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
            System.out.println("receive packets udp");
        outOfTime = false;
        dataLength = 0;
              try(DatagramSocket serverSocket = new DatagramSocket(PORT);) {
                  serverSocket.setSoTimeout(TIMEOUT);
                  while (!outOfTime  && !isInterrupted()) {
                      try {
                          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                          serverSocket.receive(receivePacket);
                          dataLength = dataLength + receivePacket.getLength();
                      } catch (SocketTimeoutException exception) {
                          outOfTime = true;
                      }
                  }
              }
            System.out.println("ended receive packets");
        return System.currentTimeMillis();
       }

    private  long receivePacketsTCP(long start, byte[] receiveData) throws IOException {
      try(ServerSocket serverSocket = new ServerSocket(PORT);) {
          Socket socket;
          serverSocket.setSoTimeout(TIMEOUT);
          boolean isConnected = true;
          socket = serverSocket.accept();
          socket.setSoLinger(true,0);
          InputStream inputStream = socket.getInputStream();
          int read = inputStream.read(receiveData);
            while (!outOfTime && isConnected  && read != -1) {
                    try {
                        try {
                            read = inputStream.read(receiveData);
                            dataLength = dataLength + read;
                        } catch (SocketException socketException) {
                            isConnected = false;
                        }
                    }
                    catch (SocketTimeoutException exception) {
                        outOfTime = true;
                    }
                }
            }
      catch (SocketTimeoutException exception) {
          outOfTime = true;
      }
        return System.currentTimeMillis();
    }

        public long calculateDataRate(long start, long stop) {
            long timeInSeconds = protocol.equals(Constants.TCP) ? (stop - start) : stop -start - TIMEOUT;
            int dataLengthInKBit = (dataLength * Constants.FACTOR_BYTES_TO_BITS);
            long dataRate = outOfTime && dataLength == 0 || timeInSeconds == 0? 0 : dataLengthInKBit / timeInSeconds;
            return dataRate;
        }



    @Override
    public void run() {
        try {
            System.out.println("started server");
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
