

import java.io.IOException;



/**this class allows automated testing on localhost**/
public class BitRateTest {
    private static final String UDP = "udp";
    private static final String TCP = "tcp";
    private static final int testRepeats = Constants.TEST_REPEATS;
    private  static double[] sendDataRate = Client.getSendDataRate();
    private static  double[] receiveDataRate = Server.getReceiveDataRate();
    private int i = 0;
    double averageSend;
    double averageReceive;

    // console input: adress protocol  n k
    public static void main(String[] args) throws IOException, InterruptedException {

        BitRateTest bitRateTest = new BitRateTest();
        bitRateTest.testDataRate("udp", 0, 0, "localhost");
       // bitRateTest.testDataRate("tcp", 0, 0, "localhost");
      //  bitRateTest.testDataRate("udp", 10, 1, "localhost");
      //  bitRateTest.testDataRate("tcp", 10, 1, "localhost");
        //bitRateTest.testDataRate("udp", 1, 10, "localhost");
       //bitRateTest.testDataRate("tcp", 1, 10, "localhost");





    }

    private static void test1() {
        BitRateTest bitRateTest = new BitRateTest();
        double[][] data = new double[][] {
                {30800.0,   //Test 1 UDP Localhost send
                31832.6,
                31812.5,
                31165.1,
                31188.05,
                },
                {29427,   //Test 1 UDP Localhost receive
                23177,
                24272,
                22315,
                22323,
                },
                {
                39200.0,   //Test 1 TCP Localhost send
                36818.3,
                39200.0,
                39200.0,
                37333.33,

                },
                {
                28615,      //Test 1 TCP Localhost receive
                26699,
                27524,
                27349,
                26848,
                },
                {
                29333.35,     //Test 1 UDP  Rechner send
                29383,
                28630,
                29093.65,
                30800,

                },
                {
                11298,     //Test 1 UDP  Rechner receive
                11036,
                9470,
                10587,
                10890,
                }
                ,{             //Test 1 TCP  Rechner send
                14000,
                12504,
                12562.54,
                14000,
                13990.45,
                  },
                {
                13294,    //Test 1 TCP  Rechner receive
                12043,
                10763,
                12100,
                12700
                }


        };

        for (int i = 0; i < data.length; i = i + 2) {
            sendDataRate = data[i];
            receiveDataRate = data[i+1];
            bitRateTest.calculateAverageSendDataRate();
            bitRateTest.calculateAverageReceiveDataRate();
            bitRateTest.calculateStandardAviationSendDataRate();
            bitRateTest.calculateStandardAviationReceiveDataRate();
            System.out.println();
        }
    }



    private static void test2() {
        BitRateTest bitRateTest = new BitRateTest();
        double[][] data = new double[][] {
                { //Test 2 UDP Localhost send
                9694.15,
                9520.0,
                9977.5,
                10080.0,
                10060.4



                },
                {   //Test 2 UDP Localhost receive
                7042,
                7290,
                7082,
                7380,
                7262,
                },
                {//Test 2 TCP Localhost send
                9520.0,
                10080.0,
                10080.0,
                10090,
                10080.0
                },
                {
                //Test 2 TCP Localhost receive
                7314,
                7336,
                7352,
                6905,
                7108
                },
                {
                //Test 2 UDP  Rechner send
                10080,
                9520,
                9811.75,
                9520,
                9520
                },
                {
                //Test 2 UDP  Rechner receive
                2305,
                3533,
                2091,
                4082,
                3383
                }
                ,{
                //Test 2 TCP  Rechner send
                7584,
                6652,
                8960,
                6448.4,
                8960
        },
                {
                //Test 2 TCP  Rechner receive
                5208,
                5968,
                8768,
                6229,
                8728
                }
        };

        for (int i = 0; i < data.length; i = i + 2) {
            sendDataRate = data[i];
            receiveDataRate = data[i+1];
            bitRateTest.calculateAverageSendDataRate();
            bitRateTest.calculateAverageReceiveDataRate();
            bitRateTest.calculateStandardAviationSendDataRate();
            bitRateTest.calculateStandardAviationReceiveDataRate();
            System.out.println();
        }
    }


    private static void test3() {
        BitRateTest bitRateTest = new BitRateTest();
        double[][] data = new double[][] {
                { //Test 3 UDP Localhost send
                19600.0,
                21288.95,
                20605.75,
                21148.95,
                19600.0
                },
                {   //Test 3 UDP Localhost receive
                15168,
                15624,
                14661,
                15437,
                15236
                },
                {//Test 3 TCP Localhost send
                21333.33,
                22400.0,
                21333.33,
                22400.0,
                21333.33
                },
                {
                //Test 3 TCP Localhost receive
                14568,
                15960,
                15545,
                15748,
                14484
                },
                {
                //Test 3 UDP  Rechner send
                18666.66,
                19600,
                19600,
                19536,
                18666.66,
                },
                {
                //Test 3 UDP  Rechner receive
                6511,
                6490,
                4524,
                6224,
                6550
                }
                ,{
                //Test 3 TCP  Rechner send
                9734,
                11088,
                10166,
                10934,
                9945
        },
                {
                //Test 3 TCP  Rechner receive
                9463,
                9184,
                9575,
                10127,
                9009
                }
        };

        for (int i = 0; i < data.length; i = i + 2) {
            sendDataRate = data[i];
            receiveDataRate = data[i+1];
            bitRateTest.calculateAverageSendDataRate();
            bitRateTest.calculateAverageReceiveDataRate();
            bitRateTest.calculateStandardAviationSendDataRate();
            bitRateTest.calculateStandardAviationReceiveDataRate();
            System.out.println();
        }
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
        for (int i = 0; i < testRepeats; i++) {
            datarateSum = datarateSum + sendDataRate[i];
        }
        averageSend= datarateSum / testRepeats;
        System.out.println("Durchschnitt gesendet: " + averageSend);
        return averageSend;
    }

    private double calculateAverageReceiveDataRate() {
        double datarateSum = 0;
        for (int i = 0; i < testRepeats; i++) {
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
