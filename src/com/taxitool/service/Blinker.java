package com.taxitool.service;

import java.io.IOException;


//Thread class makes it possible to blink the light and interrupt it.
public class Blinker extends Thread {
   private final LightService lightService;
   private final String id;
   private final static String BLINK_COLOR = "red";
    public Blinker(LightService lightService,  String id) {
        this.lightService = lightService;
        this.id = id;
    }

   // Blinker Thread runs until it is interrupted
    @Override
    public void run() {
        try {
            lightService.changeColor(id,BLINK_COLOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(! isInterrupted()) {
            try {
                lightService.turnLightOff(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
            try {
                lightService.turnLightOn(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}




