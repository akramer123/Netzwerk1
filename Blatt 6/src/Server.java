import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class Server extends Thread {
        private  static final int PACKET_LENGTH = Constants.PACKET_LENGTH;
        private  static final int PORT = Constants.PORT;
        private  static final int TIMEOUT = 10_000;
        private  int dataLength = 0;
        boolean  outOfTime = false;
        private  BitRateTest bitRateTest;
        private  String protocol;
        private  static double[] receiveDataRate = new double[Constants.TEST_REPEATS];
        private  static int i = 0;
        private  int packetCounter = 0;

        public static void main(String[] args) throws IOException {
            Server server = new Server(args[0]);
            long received = server.receivePackets(server.getProtocol());
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
        outOfTime = false;
        dataLength = 0;
              try(DatagramSocket serverSocket = new DatagramSocket(PORT);) {
                  serverSocket.setSoTimeout(TIMEOUT);
                  while (!outOfTime) {
                      try {
                          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                          serverSocket.receive(receivePacket);
                          packetCounter  = packetCounter + 1;
                      } catch (SocketTimeoutException exception) {
                          outOfTime = true;

                      }
                  }
              }
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
                            packetCounter = packetCounter + 1;
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
            double timeInSeconds = protocol.equals(Constants.TCP) ? (stop - start) : stop -start - TIMEOUT;
            timeInSeconds = timeInSeconds / Constants.FACTOR_KILO;
            long dataLengthInKBit = (long) ((double) (packetCounter / Constants.FACTOR_KILO)) * PACKET_LENGTH * Constants.FACTOR_BYTES_TO_BITS;
            long dataRate = outOfTime && packetCounter == 0 || timeInSeconds == 0? 0 : dataLengthInKBit / (long)timeInSeconds;
            receiveDataRate[i] = dataRate;
            i++;
            System.out.println("i" + i);
            return dataRate;
        }



    public static void printReceiveDataRate() {
        System.out.println("Receive Data Rate");
        for (int j = 0; j < Constants.TEST_REPEATS; j++) {
            System.out.print(receiveDataRate[j] + " ");
            System.out.println();
        }
    }


    private String getProtocol() {
            return protocol;
    }


    public static double[] getReceiveDataRate() {
            return receiveDataRate;
    }


    public static void resetIndex() {
        i = 0;
    }

    public  void putReceiveDataRate(double dataRate) {
        receiveDataRate[i] = dataRate;
        i++;
    }


    public void run(){
        try {
            receivePackets(protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
