package com.company;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int counter = 1;
        String line;
        FileOutputStream file = new FileOutputStream("test.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(file))) {
            while ((line = reader.readLine()) != null && !line.equals("")) {
                bufferedWriter.write("[" + counter + "] " + line);
                bufferedWriter.newLine();
                counter++;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        System.out.println("Zugriff aufgezeichnet am " + format.format(new Date()));
    }
}

