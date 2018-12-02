//Kristina
package com.taxitool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.model.light.State;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightService {

    private static final String BASEURL_LIGHTS = "http://";
    private static final String BRIDGE_IP = "10.28.209.13:9001";

    //    private static final String BRIDGE_IP = "localhost:8000";
    //private static final String AUTHORIZED_USER = "newdeveloper";
    private static final String AUTHORIZED_USER = "3dc1d8f23e55321f3c049c03ac88dff";
    private Blinker blinker;
    private static final Map<String, Integer> colorValues;

    static {
        colorValues = new HashMap<>();
        colorValues.put("orange", 4444);
        colorValues.put("green", 25500);
        colorValues.put("red", 65136);
    }


    public void updateLights(List<TaxiModel> taxiModels) {

        for (TaxiModel taxi : taxiModels) {
            try {
                if (taxi.getStatus().equals(TaxiStatus.FREE)) {
                    changeColor(taxi.getId() + "", "green");
                } else if (taxi.getStatus().equals(TaxiStatus.DELAY)) {
                    blink(Integer.toString(taxi.getId()));
                    changeColor(taxi.getId() + "", "red");
                } else if (taxi.getStatus().equals(TaxiStatus.ONTIME)) {
                    changeColor(taxi.getId() + "", "orange");
                } else {
                    turnLightOff(taxi.getId() + "");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void turnLightOff(String id) throws IOException {
        URL url = null;
        ObjectMapper objectMapper = new ObjectMapper();
        State state = new State();
        state.setOn(false);
        String jsonTurnOffLights = objectMapper.writeValueAsString(state);
        connectToLights(id, jsonTurnOffLights);
    }

    public void turnLightOn(String id) throws IOException {
        URL url = null;
        ObjectMapper objectMapper = new ObjectMapper();
        State state = new State();
        state.setOn(true);
        String jsonTurnOnLights = objectMapper.writeValueAsString(state);
        connectToLights(id, jsonTurnOnLights);
    }

    public void changeColor(String id, String color) throws IOException {
        turnLightOn(id);
        URL url = null;
        ObjectMapper objectMapper = new ObjectMapper();
        State state = new State();
        int colorValue = colorValues.get(color);
        state.setHue(colorValue);
        state.setSat(254);
        String jsonChangeColor = objectMapper.writeValueAsString(state);
        connectToLights(id, jsonChangeColor);
    }

    public void blink(String id) throws IOException, InterruptedException {
        blinker = new Blinker(this, id);
        blinker.start();
        Thread.sleep(10000);
        blinker.interrupt();
    }


    private void connectToLights(String id, String command) throws IOException {
        URL url;
        url = new URL(BASEURL_LIGHTS + BRIDGE_IP + "/api/" + AUTHORIZED_USER + "/lights/" + id + "/state/");
        URLConnection con = url.openConnection();
        ((HttpURLConnection) con).setRequestMethod("PUT");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(7000);
        con.setReadTimeout(10000);

        try {
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(command);
            out.flush();
            out.close();

            BufferedReader in;

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (SocketTimeoutException ex) {
            System.out.println("Need proxy...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}