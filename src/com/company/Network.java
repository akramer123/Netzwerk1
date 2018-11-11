package com.company;

import java.io.*;
import java.text.DateFormat;
import java.util.GregorianCalendar;


//This class reads an input line for line, adds line numbers und stores the text to a file. 
public class Network {
    public static void main(String[] args) throws IOException {


        System.out.println("Please enter a text");
        try (
        Reader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        Writer fileWriter = new FileWriter("logfile.txt");
        ) {
            boolean stop = false;
            int line = 1;
            String text = bufferedReader.readLine();
            fileWriter.write("[" + "0" + line +  "] " +text + '\r');

            while (!stop) {
                text = bufferedReader.readLine();
                if (text.isEmpty()) {
                    stop = true;
                } else {
                    line ++;
                    if (line < 10) {
                    fileWriter.write("[" + "0" + line +  "] " +text + '\r');
                    }
                    else {
                    fileWriter.write("[" + line +  "] " +text + '\r');
                    }
                 }
            }

            GregorianCalendar now = new GregorianCalendar();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            String date = df.format(now.getTime());
            df = DateFormat.getTimeInstance(DateFormat.SHORT);
            String time = df.format(now.getTime());

            fileWriter.write("Zugriff aufgezeichnet am [" +  date + " " + time + "]!");
        }
    }
    public Network() throws IOException {
    }
}

