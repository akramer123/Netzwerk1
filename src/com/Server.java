package com;

import com.sun.deploy.util.URLUtil;
import com.sun.jndi.toolkit.url.UrlUtil;
import com.sun.xml.internal.fastinfoset.EncodingConstants;
import sun.awt.CharsetString;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static final int PORT = 8082;
    private final String host;
    private String path = "";

    Set<String> yeahKeyWords = new HashSet<>(Arrays.asList("Java", "Computer", "RISC", "CISC", "Debugger"
            , "Informatik", "Student", "Studentin", "Studierende", "Windows", "Linux", "Software", "InformatikerInnen",
            "Informatiker", "Informatikerin"));

    public Server(String host) {

        this.host = host;
    }

    public static void main(String[] args) {
        Server server = new Server(args[0]);
        server.startServer();
    }

    private void startServer() {

        try (ServerSocket servSock = new ServerSocket(PORT)) {
            //Server gets request from client.
            System.out.println("Server started, waiting for clients...");
            while (true) {
                try (Socket s = servSock.accept();
                     BufferedReader fromClient =
                             new BufferedReader(
                                     new InputStreamReader(s.getInputStream()))) {

                    getPath(fromClient);


                    InputStream inputStream = null;
                    URLConnection connection;
                    // Server requests the page from the target host.
                    URL url = new URL("https://" + host + "/" + path);
                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    connection = con;
                    try {
                        inputStream = con.getInputStream();
                    } catch (ConnectException conEx) {
                        url = new URL("http://" + host + "/" + path);
                        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                        httpCon.setRequestProperty("RequestMethod", "GET");
                        connection = httpCon;
                        if (httpCon.getResponseCode() == 200) {
                            inputStream = httpCon.getInputStream();
                        }
                    }
                    String contentEncoding = connection.getContentEncoding();
                    if (contentEncoding == null) {
                        contentEncoding = StandardCharsets.ISO_8859_1.name();
                    }
                    if (inputStream != null) {

                        BufferedReader inputFromTargetHost = new BufferedReader(
                                new InputStreamReader(inputStream, contentEncoding));

                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = inputFromTargetHost.readLine()) != null) {
                            content.append(inputLine);
                        }
                        inputFromTargetHost.close();

                        String positive = changeContent(content.toString());

                        BufferedWriter toClient =
                                new BufferedWriter(
                                        new OutputStreamWriter(s.getOutputStream(), contentEncoding));
                        //Server writes the changed page back to client.
                        toClient.write(ServerConstants.HTTP10OK);
                        toClient.write("Content-length: " + positive.length() + "\r\n");
                        toClient.write("\r\n");
                        toClient.write(positive);
                        toClient.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String changeContent(final String content) {
        //content is changed
        String positive = content;
        positive = positive.replaceAll("<img[^>]*>", ServerConstants.IMG_SMILEY);
        ArrayList<String> yeahWordsList = new ArrayList<>(yeahKeyWords);

        for (String word : yeahWordsList) {
            positive = positive.replaceAll(word, word + " (yeah!)");
        }
        return positive;
    }

    private String getPath(BufferedReader fromClient) throws IOException {

        String textFromClient = "";
        for (String line = fromClient.readLine(); line != null && line.length() > 0; line = fromClient.readLine()) {
            textFromClient = textFromClient + line + "\r" + "\n";
            //path wird aus Anfrage von client herausgelesen.
            if (line.startsWith("GET")) {
                path = line.replace("GET /", "");
                path = path.replace("HTTP/1.1", "");
            }
        }

        System.out.println(textFromClient);
        return textFromClient;
    }

}