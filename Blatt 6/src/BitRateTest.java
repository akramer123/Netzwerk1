

import java.io.IOException;



/**this class allows automated testing on localhost**/
public class BitRateTest {
    private static final String UDP = "udp";
    private static final String TCP = "tcp";
    private static final int testRepeats = Constants.TEST_REPEATS;
    private  static double[] sendDataRate=  Client.getSendDataRate();
    private static  double[] receiveDataRate =  Server.getReceiveDataRate();
    private int i = 0;
    double averageSend;
    double averageReceive;

    // console input: adress protocol  n k
    public static void main(String[] args) throws IOException, InterruptedException {

        BitRateTest bitRateTest = new BitRateTest();
        bitRateTest.testDataRate("udp", 0, 0, "localhost");
    }


    private void printReceiveDataRate() {
        System.out.println("erhalten: ");
        for (int i = 0; i < testRepeats; i++) {
            System.out.print(receiveDataRate[i] + " ");
        }
    }

    private void printSendDataRate() {
        System.out.println("gesendet:");
        for (int i = 0; i < testRepeats; i++) {
            System.out.print(sendDataRate[i] + " ");
        }
    }


    private double calculateAverageSendDataRate() {
        double datarateSum = 0;
        System.out.println(sendDataRate.length);
        for (int i = 0; i < Constants.TEST_REPEATS; i++) {
            datarateSum = datarateSum + sendDataRate[i];
        }
        averageSend= datarateSum / testRepeats;
        System.out.println("Durchschnitt gesendet: " + averageSend);
        return averageSend;
    }

    private double calculateAverageReceiveDataRate() {
        double datarateSum = 0;
        for (int i = 0; i < Constants.TEST_REPEATS; i++) {
            datarateSum = datarateSum + receiveDataRate[i];
        }
        averageReceive= datarateSum / testRepeats;
        System.out.println("Durchschnitt erhalten: " + averageReceive);
        return averageReceive;

    }


    private  long calculateStandardAviationSendDataRate() {
        long datarateSum = 0;
        for (int i = 0; i < testRepeats; i++) {
            datarateSum = datarateSum + (long)Math.pow(sendDataRate[i] - averageSend, 2);
        }
        long aviation =(long) Math.sqrt(datarateSum / testRepeats);
        System.out.println("Standardabweichung gesendet : " + aviation);
        return aviation;
    }

    private long calculateStandardAviationReceiveDataRate() {
        long datarateSum = 0;
        for (int i = 0; i < testRepeats; i++) {
            datarateSum = datarateSum +(long) Math.pow(receiveDataRate[i] - averageReceive, 2);
        }
        long aviation =(long)Math.sqrt ((double)datarateSum / testRepeats)
                ;
        System.out.println("Standardabweichung erhalten : " + aviation);
        return  aviation;

    }

    private void testDataRate(String protocol, int n, int delay, String adress) throws IOException, InterruptedException {
        System.out.println();
        System.out.println("protocol = " + protocol + " n= " +n+ " k= "+delay);
        Client.calculateExpectedSendRate();
        for (i = 0; i < testRepeats; i++){
            Server server = new Server(this, protocol);
            server.start();
            Client client = new Client( n, delay, this, protocol, adress);
            client.start();
            client.join();
            server.join();
         }

         calculateAverageSendDataRate();
         calculateAverageReceiveDataRate();
         calculateStandardAviationSendDataRate();
         calculateStandardAviationReceiveDataRate();
         printSendDataRate();
         printReceiveDataRate();
         Client.resetIndex();
         Server.resetIndex();
    }



}
