package com.taxitool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.facade.LightFacade;
import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.model.light.State;
import com.taxitool.utils.SessionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightService {
    @Resource
    private LightFacade lightFacade;

    private static final String BASEURL_LIGHTS = "http://";
    private static final String BRIDGE_IP = "localhost:8000";
    private static final String AUTHORIZED_USER = "newdeveloper";
    private static boolean updateLights = true;
    private Blinker blinker;
    private static final Map<String, Integer> colorValues;

    static {
        colorValues = new HashMap<>();
        colorValues.put("orange", 4444);
        colorValues.put("green", 25500);
        colorValues.put("red", 65136);
    }


    public void stopUpdatingLights() {
        this.updateLights = false;
        HttpSession session = SessionUtils.session();
        session.setAttribute("updateLights", false);
    }


    public void updateLights(List<TaxiModel> taxiModels) {
        HttpSession session = SessionUtils.session();
        session.setAttribute("updateLights", true);
        while (this.updateLights) {
            for (TaxiModel taxi : taxiModels) {
                try {
                    if (taxi.getStatus().equals(TaxiStatus.FREE)) {
                        changeColor(taxi.getId() + "", "green");
                    } else if (taxi.getStatus().equals(TaxiStatus.DELAY)) {
                        blink(Integer.toString(taxi.getId()));
                    } else if (taxi.getStatus().equals(TaxiStatus.ONTIME)) {
                        changeColor(taxi.getId() + "", "orange");
                    } else {
                        turnLightOff(taxi.getId() + "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        //TODO: Muss man hier ueber die Facade-Methoden gehen oder passt das so?
        state.setHue(colorValue);
        state.setSat(254);
        String jsonChangeColor = objectMapper.writeValueAsString(state);
        connectToLights(id, jsonChangeColor);
    }

    public void blink(String id) throws IOException, InterruptedException {
        blinker = new Blinker(this, "1");
        blinker.start();
        Thread.sleep(10000);
        blinker.interrupt();
    }


    void connectToLights(String id, String command) throws IOException {
        URL url;
        url = new URL(BASEURL_LIGHTS + BRIDGE_IP + "/api/" + AUTHORIZED_USER + "/" + id + "/state/");
        URLConnection con = (URLConnection) url.openConnection();
        ((sun.net.www.protocol.http.HttpURLConnection) con).setRequestMethod("PUT");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(10000);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(command);
        out.flush();
        out.close();

        BufferedReader in;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLightFacade(LightFacade lightFacade) {
        this.lightFacade = lightFacade;
    }

}