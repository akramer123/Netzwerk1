package com.company;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int counter = 1;
        String line;
        while ((line = reader.readLine()) != null && !line.equals("")) {
            System.out.println("[" + counter + "] " + line);
            counter++;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy hh:mm");
        System.out.println("Zugriff aufgezeichnet am " + format.format(new Date()));
    }
}

