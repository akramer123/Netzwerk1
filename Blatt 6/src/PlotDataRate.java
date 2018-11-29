import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**with this class you can plot given data rate**/
public class PlotDataRate {


    public static void main(String[] args) throws IOException {
        double[] sendDataRate = new double[3];
        double[] receiveDataRate = new double[3];
        PlotDataRate plotDataRateFromLogFiles = new PlotDataRate();
        int i = 0;
        try(
        FileReader fileReaderServer = new FileReader("C:\\Users\\Kristina\\Desktop\\Studium\\Netzwerke\\Blatt 6\\PlotsDurchsatz\\server.txt");
        FileReader fileReaderClient = new FileReader("C:\\Users\\Kristina\\Desktop\\Studium\\Netzwerke\\Blatt 6\\PlotsDurchsatz\\client.txt");
        BufferedReader bufferedReaderServer = new BufferedReader(fileReaderServer);
        BufferedReader bufferedReaderClient = new BufferedReader(fileReaderClient))
        {
            String lineServer = bufferedReaderServer.readLine();
            String lineClient = bufferedReaderClient.readLine();
            String plotName ="";
            while(lineServer != null) {
                System.out.println(lineServer);
               if (lineServer.startsWith("k")) {
                   plotName += "k_" + bufferedReaderServer.readLine();
                   bufferedReaderClient.readLine();
               }
               else if(lineServer.startsWith("n")) {
                   plotName += "_n_" + bufferedReaderServer.readLine();
                   bufferedReaderClient.readLine();

               }
               else if (! lineServer.isEmpty()) {
                   sendDataRate[i] = Double.parseDouble(lineServer);
                   receiveDataRate[i] = Double.parseDouble(lineClient);
                   i++;

               }
               else {
                   plotDataRateFromLogFiles.plotData(plotName, sendDataRate, receiveDataRate);
                   i = 0;
                   sendDataRate = new double[3];
                   receiveDataRate = new double[3];
                   plotName = "";
               }
               lineServer = bufferedReaderServer.readLine();
               lineClient = bufferedReaderClient.readLine();
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void plotData(String plotName, double[] sendDataRate, double[] receiveDataRate) throws IOException {
        XYChart chart =new XYChartBuilder().width(800).height(600).build();
        chart.addSeries(plotName, sendDataRate, receiveDataRate);
        BitmapEncoder.saveBitmap(chart, "C:\\Users\\Kristina\\Desktop\\Studium\\Netzwerke\\Blatt 6\\PlotsDurchsatz\\" + plotName, BitmapEncoder.BitmapFormat.PNG);
        new SwingWrapper(chart).displayChart();
    }
}
