package com.taxitool;

import com.taxitool.service.LightService;

import java.io.IOException;

public class TestLights {

    //test class for lights
    //use Phillips Hue Emulator on port 8000
    public static void main(String[] args) {
            LightService lightService = new LightService();
            try {
                lightService.blink("1");
                lightService.changeColor("1","green");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}

