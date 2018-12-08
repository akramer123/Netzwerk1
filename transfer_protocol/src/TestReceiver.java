import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class TestReceiver {
    private final CRC32 crc = new CRC32();
    DatagramSocket serverSocket = new DatagramSocket(90);
    static boolean outOfTime = false;

    public TestReceiver() throws SocketException {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TestReceiver testReceiver = new TestReceiver();
        int i = 0;
        while(! outOfTime) {
      /*      if (i % 100 == 0) {
            Thread.sleep(20000);
            }*/
            testReceiver.receivePacketsUDP();
            testReceiver.sendPacket();
            i++;
        }
        System.out.println("crc" + testReceiver.crc.getValue());
    }

    private  long receivePacketsUDP() throws IOException {

        byte[] receiveData = new byte[1024];

            serverSocket.setSoTimeout(10000);

                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    crc.update(receiveData, 0 , 1016);
                    byte[] crcBytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(crc.getValue()).array();
                    ByteBuffer buffer = ByteBuffer.wrap(crcBytes, 0,Long.SIZE / Byte.SIZE);
                    buffer.put(receiveData, 1016, 8);
                    buffer.rewind();
                    long receivedCrc = buffer.getLong();

                    System.out.println("received crc" + receivedCrc);
                    boolean isRightChecksum = receivedCrc == crc.getValue();
                    System.out.println("CRC is" + isRightChecksum);
                    System.out.println(new String(receiveData));
                    crc.reset();
                } catch (SocketTimeoutException exception) {
                    outOfTime = true;
                }


        System.out.println("ended receive packets");
        return System.currentTimeMillis();
    }



    public void sendPacket() throws IOException {
        try(DatagramSocket sendSocket = new DatagramSocket();) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            String ack = "ACK";
            byte[] fileData =ack.getBytes();

                DatagramPacket datagramPacket = new DatagramPacket(fileData, fileData.length, IPAddress, 100);
                sendSocket.send(datagramPacket);
                System.out.println("send");

        }
    }


}
