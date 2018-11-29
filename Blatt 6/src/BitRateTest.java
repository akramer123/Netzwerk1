import org.knowm.xchart.*;

import java.io.IOException;



/**this class allows automated testing on localhost**/
public class BitRateTest {
    private static final String UDP = "udp";
    private static final String TCP = "tcp";
    private static final int testRepeats = Constants.TEST_REPEATS;
    private final double[] sendDataRate = new double[testRepeats];
    private final double[] receiveDataRate = new double[testRepeats];
    private int i = 0;


    public static void main(String[] args) throws IOException, InterruptedException {
        BitRateTest bitRateTest = new BitRateTest();
        bitRateTest.testDataRate(UDP, 200,500,"localhost");
/*        Client.printSendDataRate();
        Server.printReceiveDataRate();
        bitRateTest.plotData("n=200_k=50_udp");*/
    }


    private void printReceiveDataRate() {
        System.out.println("Receive Data Rate");
        for (int i = 0; i < testRepeats; i++) {
            System.out.print(receiveDataRate[i] + " ");
        }
    }


    private double calculateAverageSendDataRate() {
        double datarateSum = 0;
        for (int i = 0; i < testRepeats; i++) {
            datarateSum = datarateSum + sendDataRate[i];
        }
        return datarateSum / testRepeats;
    }

    private double calculateAverageReceiveDataRate() {
        double datarateSum = 0;
        for (int i = 0; i < testRepeats; i++) {
            datarateSum = datarateSum + receiveDataRate[i];
        }
        return datarateSum / testRepeats;

    }


    private double calculateStandardAviationSendDataRate() {
        double datarateSum = 0;
        double average = calculateAverageSendDataRate();
        for (int i = 0; i < testRepeats; i++) {
            datarateSum = datarateSum + (sendDataRate[i] - average) * (sendDataRate[i] - average);
        }
        return datarateSum / testRepeats;
    }

    private double calculateStandardAviationReceiveDataRate() {
        double datarateSum = 0;
        double average = calculateAverageReceiveDataRate();
        for (int i = 0; i < testRepeats; i++) {
            datarateSum = datarateSum + (receiveDataRate[i] - average) * (receiveDataRate[i] - average);
        }
        return datarateSum / testRepeats;

    }

    private void testDataRate(String protocol, int n, int delay, String adress) throws IOException, InterruptedException {

        for (i = 0; i < testRepeats; i++){
            Server server = new Server(this, protocol);

            Client client = new Client( n, delay, this, protocol, adress);
            client.sendPacket(protocol);
            server.receivePackets(protocol);

         }
    }


    private void plotData(String plotName) throws IOException {
        XYChart chart =new XYChartBuilder().width(800).height(600).build();
        chart.addSeries(plotName, sendDataRate, receiveDataRate);
        BitmapEncoder.saveBitmap(chart, ""+ plotName, BitmapEncoder.BitmapFormat.PNG);
        new SwingWrapper(chart).displayChart();
    }
}
