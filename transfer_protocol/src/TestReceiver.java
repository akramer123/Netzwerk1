import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class TestReceiver {
    public static void main(String[] args) throws IOException {
        TestReceiver testReceiver = new TestReceiver();
       testReceiver.receivePacketsUDP();

       testReceiver.sendPacket();
        ;
    }

    private  long receivePacketsUDP() throws IOException {
        System.out.println("receive packets udp");
        boolean outOfTime = false;
        byte[] receiveData = new byte[1024];
        try(DatagramSocket serverSocket = new DatagramSocket(90);) {
            serverSocket.setSoTimeout(10000);
            while (!outOfTime ) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    System.out.println(new String(receiveData));
                } catch (SocketTimeoutException exception) {
                    outOfTime = true;
                }
            }
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
