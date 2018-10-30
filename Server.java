

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Server {
    public static final int PORT = 8082;
    private final String host;
    private  String path = "";
    private final String smiley = "" +
            "<svg\n" +
            "   xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
            "   xmlns:cc=\"http://web.resource.org/cc/\"\n" +
            "   xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
            "   xmlns:svg=\"http://www.w3.org/2000/svg\"\n" +
            "   xmlns=\"http://www.w3.org/2000/svg\"\n" +
            "   xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" +
            "   xmlns:sodipodi=\"http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd\"\n" +
            "   xmlns:inkscape=\"http://www.inkscape.org/namespaces/inkscape\"\n" +
            "   version=\"1.0\"\n" +
            "   width=\"152\"\n" +
            "   height=\"151\"\n" +
            "   id=\"svg2310\"\n" +
            "   sodipodi:version=\"0.32\"\n" +
            "   inkscape:version=\"0.44.1\"\n" +
            "   sodipodi:docname=\"Smiley_head_happy_RedElf.svg\"\n" +
            "   sodipodi:docbase=\"f:\\incoming\">\n" +
            "  <metadata\n" +
            "     id=\"metadata1982\">\n" +
            "    <rdf:RDF>\n" +
            "      <cc:Work\n" +
            "         rdf:about=\"\">\n" +
            "        <dc:format>image/svg+xml</dc:format>\n" +
            "        <dc:type\n" +
            "           rdf:resource=\"http://purl.org/dc/dcmitype/StillImage\" />\n" +
            "      </cc:Work>\n" +
            "    </rdf:RDF>\n" +
            "  </metadata>\n" +
            "  <sodipodi:namedview\n" +
            "     inkscape:window-height=\"667\"\n" +
            "     inkscape:window-width=\"1024\"\n" +
            "     inkscape:pageshadow=\"2\"\n" +
            "     inkscape:pageopacity=\"0.0\"\n" +
            "     guidetolerance=\"10.0\"\n" +
            "     gridtolerance=\"10.0\"\n" +
            "     objecttolerance=\"10.0\"\n" +
            "     borderopacity=\"1.0\"\n" +
            "     bordercolor=\"#666666\"\n" +
            "     pagecolor=\"#ffffff\"\n" +
            "     id=\"base\"\n" +
            "     inkscape:zoom=\"2.4966887\"\n" +
            "     inkscape:cx=\"76\"\n" +
            "     inkscape:cy=\"86.577529\"\n" +
            "     inkscape:window-x=\"-4\"\n" +
            "     inkscape:window-y=\"26\"\n" +
            "     inkscape:current-layer=\"svg2310\" />\n" +
            "  <defs\n" +
            "     id=\"defs2313\">\n" +
            "    <linearGradient\n" +
            "       id=\"linearGradient6509\">\n" +
            "      <stop\n" +
            "         id=\"stop6511\"\n" +
            "         offset=\"0\"\n" +
            "         style=\"stop-color:#ffff84;stop-opacity:1;\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:#ffff84;stop-opacity:1;\"\n" +
            "         offset=\"0.59649122\"\n" +
            "         id=\"stop6519\" />\n" +
            "      <stop\n" +
            "         id=\"stop6513\"\n" +
            "         offset=\"1\"\n" +
            "         style=\"stop-color:#dddd71;stop-opacity:1;\" />\n" +
            "    </linearGradient>\n" +
            "    <linearGradient\n" +
            "       id=\"linearGradient8253\">\n" +
            "      <stop\n" +
            "         style=\"stop-color:#ffff84;stop-opacity:1;\"\n" +
            "         offset=\"0\"\n" +
            "         id=\"stop8255\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:#fefe84;stop-opacity:0.81818181;\"\n" +
            "         offset=\"0.3508772\"\n" +
            "         id=\"stop8261\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:#fefe84;stop-opacity:0\"\n" +
            "         offset=\"1\"\n" +
            "         id=\"stop8257\" />\n" +
            "    </linearGradient>\n" +
            "    <linearGradient\n" +
            "       id=\"linearGradient7268\">\n" +
            "      <stop\n" +
            "         style=\"stop-color:#fdfe65;stop-opacity:1\"\n" +
            "         offset=\"0\"\n" +
            "         id=\"stop7276\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:#fefe84;stop-opacity:0\"\n" +
            "         offset=\"1\"\n" +
            "         id=\"stop7272\" />\n" +
            "    </linearGradient>\n" +
            "    <linearGradient\n" +
            "       id=\"linearGradient7240\">\n" +
            "      <stop\n" +
            "         style=\"stop-color:#ffff83;stop-opacity:0.93181819\"\n" +
            "         offset=\"0\"\n" +
            "         id=\"stop7242\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:#e5e5a7;stop-opacity:0.46666667\"\n" +
            "         offset=\"0.82456142\"\n" +
            "         id=\"stop7254\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:#cccccc;stop-opacity:0\"\n" +
            "         offset=\"1\"\n" +
            "         id=\"stop7244\" />\n" +
            "    </linearGradient>\n" +
            "    <linearGradient\n" +
            "       id=\"linearGradient7178\">\n" +
            "      <stop\n" +
            "         style=\"stop-color:#fdfe84;stop-opacity:1\"\n" +
            "         offset=\"0\"\n" +
            "         id=\"stop7180\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:#cccccc;stop-opacity:0\"\n" +
            "         offset=\"1\"\n" +
            "         id=\"stop7182\" />\n" +
            "    </linearGradient>\n" +
            "    <linearGradient\n" +
            "       id=\"linearGradient5218\">\n" +
            "      <stop\n" +
            "         style=\"stop-color:#ffffff;stop-opacity:1\"\n" +
            "         offset=\"0\"\n" +
            "         id=\"stop5220\" />\n" +
            "      <stop\n" +
            "         style=\"stop-color:white;stop-opacity:0;\"\n" +
            "         offset=\"1\"\n" +
            "         id=\"stop5222\" />\n" +
            "    </linearGradient>\n" +
            "    <radialGradient\n" +
            "       cx=\"34.724926\"\n" +
            "       cy=\"27.775087\"\n" +
            "       r=\"69.591515\"\n" +
            "       fx=\"34.724926\"\n" +
            "       fy=\"27.775087\"\n" +
            "       id=\"radialGradient5224\"\n" +
            "       xlink:href=\"#linearGradient5218\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(0.5093955,0.5227994,-0.7761319,0.7823967,38.602835,-11.958416)\" />\n" +
            "    <radialGradient\n" +
            "       cx=\"34.724926\"\n" +
            "       cy=\"27.775087\"\n" +
            "       r=\"69.591515\"\n" +
            "       fx=\"34.724926\"\n" +
            "       fy=\"27.775087\"\n" +
            "       id=\"radialGradient5233\"\n" +
            "       xlink:href=\"#linearGradient5218\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(0.509395,0.522799,-0.776132,0.782397,38.60284,-11.95842)\" />\n" +
            "    <radialGradient\n" +
            "       cx=\"68.605415\"\n" +
            "       cy=\"77.893295\"\n" +
            "       r=\"69.591515\"\n" +
            "       fx=\"68.605415\"\n" +
            "       fy=\"77.893295\"\n" +
            "       id=\"radialGradient8259\"\n" +
            "       xlink:href=\"#linearGradient8253\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(1.0937675e-2,1.1569368,-1.0659803,1.0426422e-2,156.73962,-15.06552)\" />\n" +
            "    <radialGradient\n" +
            "       cx=\"40.860455\"\n" +
            "       cy=\"30.631889\"\n" +
            "       r=\"69.591515\"\n" +
            "       fx=\"40.860455\"\n" +
            "       fy=\"30.631889\"\n" +
            "       id=\"radialGradient8315\"\n" +
            "       xlink:href=\"#linearGradient5218\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(9.201524e-2,0.11266,-0.461914,0.390321,51.24997,13.70394)\" />\n" +
            "    <radialGradient\n" +
            "       cx=\"70.916199\"\n" +
            "       cy=\"82.945503\"\n" +
            "       r=\"69.591515\"\n" +
            "       fx=\"70.916199\"\n" +
            "       fy=\"82.945503\"\n" +
            "       id=\"radialGradient8395\"\n" +
            "       xlink:href=\"#linearGradient8253\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(1.061959e-2,1.041469,-1.201447,1.225085e-2,165.6893,-4.980603)\" />\n" +
            "    <radialGradient\n" +
            "       inkscape:collect=\"always\"\n" +
            "       xlink:href=\"#linearGradient5218\"\n" +
            "       id=\"radialGradient2943\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(9.625779e-2,0.115867,-0.483211,0.401433,48.1179,14.21033)\"\n" +
            "       cx=\"40.860455\"\n" +
            "       cy=\"30.631889\"\n" +
            "       fx=\"40.860455\"\n" +
            "       fy=\"30.631889\"\n" +
            "       r=\"69.591515\" />\n" +
            "    <radialGradient\n" +
            "       inkscape:collect=\"always\"\n" +
            "       xlink:href=\"#linearGradient8253\"\n" +
            "       id=\"radialGradient2946\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(1.061959e-2,1.041469,-1.201447,1.225085e-2,165.6893,-4.9806)\"\n" +
            "       cx=\"70.916199\"\n" +
            "       cy=\"82.945503\"\n" +
            "       fx=\"70.916199\"\n" +
            "       fy=\"82.945503\"\n" +
            "       r=\"69.591515\" />\n" +
            "    <radialGradient\n" +
            "       inkscape:collect=\"always\"\n" +
            "       xlink:href=\"#linearGradient5218\"\n" +
            "       id=\"radialGradient2949\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       gradientTransform=\"matrix(0.400608,0.642323,-0.969928,0.604933,55.32175,-19.10905)\"\n" +
            "       cx=\"34.724926\"\n" +
            "       cy=\"27.775087\"\n" +
            "       fx=\"34.724926\"\n" +
            "       fy=\"27.775087\"\n" +
            "       r=\"69.591515\" />\n" +
            "    <radialGradient\n" +
            "       inkscape:collect=\"always\"\n" +
            "       xlink:href=\"#linearGradient6509\"\n" +
            "       id=\"radialGradient6521\"\n" +
            "       cx=\"63.18301\"\n" +
            "       cy=\"63.383327\"\n" +
            "       fx=\"63.18301\"\n" +
            "       fy=\"63.383327\"\n" +
            "       r=\"72.277115\"\n" +
            "       gradientTransform=\"matrix(1.177331,0,0,1.202268,-11.20431,-12.7197)\"\n" +
            "       gradientUnits=\"userSpaceOnUse\"\n" +
            "       spreadMethod=\"pad\" />\n" +
            "  </defs>\n" +
            "  <path\n" +
            "     id=\"path3299\"\n" +
            "     style=\"opacity:1;fill:url(#radialGradient6521);fill-opacity:1.0;stroke:none;stroke-opacity:1\"\n" +
            "     d=\"M 148.2771,75.49997 C 148.2771,115.0857 115.91753,147.17628 75.999985,147.17628 C 36.082435,147.17628 3.722867,115.0857 3.722867,75.49997 C 3.722867,35.914238 36.082435,3.823662 75.999985,3.823662 C 115.91753,3.823662 148.2771,35.914238 148.2771,75.49997 z \" />\n" +
            "  <path\n" +
            "     id=\"path5216\"\n" +
            "     style=\"opacity:1;fill:url(#radialGradient2949);fill-opacity:1;stroke:none;stroke-opacity:1\"\n" +
            "     d=\"M 146.48327,91.50286 C 137.7186,130.10614 99.056977,154.23556 60.130133,145.39744 C 21.203284,136.55931 -3.247991,98.10046 5.516682,59.49719 C 14.281356,20.89391 52.942973,-3.235512 91.869822,5.602612 C 130.79667,14.440734 155.24795,52.89959 146.48327,91.50286 z \" />\n" +
            "  <path\n" +
            "     id=\"path8251\"\n" +
            "     style=\"opacity:1;fill:url(#radialGradient2946);fill-opacity:1;stroke:none;stroke-opacity:1\"\n" +
            "     d=\"M 148.27713,75.49999 C 131.85538,118.26958 104.28159,141.16838 76,147.17634 C 36.103023,147.17634 3.722873,115.06533 3.722873,75.49999 C 3.722873,35.93466 36.103023,3.82366 76,3.82366 C 115.89697,3.82366 148.27713,35.93466 148.27713,75.49999 z \" />\n" +
            "  <path\n" +
            "     id=\"path8265\"\n" +
            "     style=\"opacity:1;fill:url(#radialGradient2943);fill-opacity:1;stroke:none;stroke-opacity:1\"\n" +
            "     d=\"M 148.2771,75.49997 C 148.2771,115.0857 115.91753,147.17628 75.99999,147.17628 C 36.082435,147.17628 3.722867,115.0857 3.722867,75.49997 C 3.722867,35.914238 36.082435,3.823662 75.99999,3.823662 C 115.91753,3.823662 148.2771,35.914238 148.2771,75.49997 z \" />\n" +
            "  <g\n" +
            "     style=\"opacity:1;display:inline\"\n" +
            "     id=\"layer1\">\n" +
            "    <path\n" +
            "       d=\"M 60.480106 46.862068 A 6.6087532 12.015915 0 1 1  47.2626,46.862068 A 6.6087532 12.015915 0 1 1  60.480106 46.862068 z\"\n" +
            "       transform=\"matrix(0.882698,0,0,1.0188172,5.5181587,1.3211043)\"\n" +
            "       style=\"opacity:1;fill:#000000;fill-opacity:1;stroke:#000000;stroke-opacity:1\"\n" +
            "       id=\"path3293\" />\n" +
            "    <path\n" +
            "       d=\"M 60.480106 46.862068 A 6.6087532 12.015915 0 1 1  47.2626,46.862068 A 6.6087532 12.015915 0 1 1  60.480106 46.862068 z\"\n" +
            "       transform=\"matrix(0.882698,0,0,1.0188172,49.977045,1.3211043)\"\n" +
            "       style=\"opacity:1;fill:#000000;fill-opacity:1;stroke:#000000;stroke-opacity:1\"\n" +
            "       id=\"path3297\" />\n" +
            "    <path\n" +
            "       d=\"M 26.034482,97.328912 C 36.848806,92.923077 39.65252,85.312997 39.65252,85.312997\"\n" +
            "       style=\"fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" +
            "       id=\"path2178\" />\n" +
            "    <path\n" +
            "       d=\"M 126.96817,96.527846 C 116.15385,92.122016 113.35013,84.511936 113.35013,84.511936\"\n" +
            "       style=\"opacity:1;fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1;display:inline\"\n" +
            "       id=\"path3271\" />\n" +
            "    <path\n" +
            "       d=\"M 34.846154,93.323607 C 53.913652,126.06944 99.295768,126.86136 118.55703,92.122016\"\n" +
            "       style=\"fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" +
            "       id=\"path3273\" />\n" +
            "    <path\n" +
            "       d=\"M 35.046418,92.385537 C 54.113916,130.02537 99.496032,129.53251 118.75729,91.183946\"\n" +
            "       style=\"opacity:1;fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1;display:inline\"\n" +
            "       id=\"path4245\" />\n" +
            "  </g>\n" +
            "  <g\n" +
            "     style=\"fill:none;display:inline;stroke:black;stroke-opacity:1;stroke-width:2;stroke-miterlimit:4;stroke-dasharray:none\"\n" +
            "     id=\"layer4\">\n" +
            "    <path\n" +
            "       d=\"M 146.9947,73.297081 C 146.9947,111.78708 116.06137,142.98939 77.903184,142.98939 C 39.744994,142.98939 8.8116684,111.78708 8.8116684,73.297081 C 8.8116684,34.807082 39.744994,3.6047732 77.903184,3.6047732 C 116.06137,3.6047732 146.9947,34.807082 146.9947,73.297081 z \"\n" +
            "       transform=\"matrix(1.046107,0,0,1.028468,-5.495081,0.116268)\"\n" +
            "       style=\"opacity:1;fill:none;fill-opacity:1;stroke:black;stroke-opacity:1;display:inline;stroke-width:1.92817545;stroke-miterlimit:4;stroke-dasharray:none\"\n" +
            "       id=\"path8320\" />\n" +
            "  </g>\n" +
            "</svg>";




    public Server(String host) {

        this.host = host;
    }
    public static void main(String[] args) {
        Server server = new Server(args[0]);
        server.startServer();

    }

    private void startServer() {

        try(ServerSocket servSock = new ServerSocket(PORT)) {
            //Server gets request from client.
            System.out.println("Server started, waiting for clients...");
            while (true) {
                try (Socket s = servSock.accept();
                     BufferedReader fromClient =
                             new BufferedReader(
                                     new InputStreamReader(s.getInputStream()));

                     BufferedWriter toClient =
                             new BufferedWriter(
                                     new OutputStreamWriter(s.getOutputStream()))) {


                    String textFromClient = "";
                    for(String line = fromClient.readLine(); line != null && line.length()>0;line = fromClient.readLine()){
                        textFromClient = textFromClient + line + "\r" +  "\n";
                        //path wird aus Anfrage von client herausgelesen.
                        if (line.startsWith("GET")) {
                            path = line.replace("GET /", "") ;
                            path = path.replace("HTTP/1.1", "");
                        }
                    }

                    System.out.println(textFromClient);


                    // Server requests the page from the target host.
                    URL url = new URL("https://"+ host + "/" + path);
                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    BufferedReader inputFromTargetHost = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = inputFromTargetHost.readLine()) != null) {
                        content.append(inputLine);
                    }
                    inputFromTargetHost.close();

                    //content is changed
                    String positive = content.toString();
                    positive = positive.replaceAll("<img[^>]*>", smiley);
                    ArrayList<String> addYeahWords = new ArrayList<>();
                    addYeahWords.add("MMIX");
                    addYeahWords.add("Java");
                    addYeahWords.add("Computer");
                    addYeahWords.add("RISC");
                    addYeahWords.add("CISC");
                    addYeahWords.add("Debugger");
                    addYeahWords.add("Informatik");
                    addYeahWords.add("Student");
                    addYeahWords.add("Studentin");
                    addYeahWords.add("Studierende");
                    addYeahWords.add("Windows");
                    addYeahWords.add("Linux");
                    addYeahWords.add("Software");
                    addYeahWords.add("InformatikerInnen");
                    addYeahWords.add("Informatiker");
                    addYeahWords.add("Informatikerin");

                    for (String word : addYeahWords) {
                        positive = positive.replaceAll(word, word + " (yeah!)");
                    }

                    //Server writes the changed page back to client.
                    toClient.write("HTTP/1.0 200 OK\r\n");
                    toClient.write("Content-length: " + positive.length() + "\r\n");
                    toClient.write("\r\n");
                    toClient.write(positive);
                    toClient.flush();
                }
            }
            } catch(IOException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }

}