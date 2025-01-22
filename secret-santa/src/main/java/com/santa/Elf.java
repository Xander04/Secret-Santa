package com.santa;

import java.util.ArrayList;

public class Elf extends Thread {

    @Override public void run() {

            while (true){
            //Purge old events
            ArrayList<String> events = DBManager.housekeepEvents();

            for (String event : events) {
                DBManager.DeleteEvent(event);
            }

            System.out.println(events);

            //Purge old auth tokens
            ArrayList<String> tokens = DBManager.housekeepTokens();
            
            System.out.println(tokens);
    
            for (String token : tokens) {
                DBManager.Deauthenticate(token);
            }
            
            try {
                for (int i=0;i < 30; i++) {
                Thread.sleep(9999);
                Thread.sleep(9999);
                Thread.sleep(9999);
                Thread.sleep(9999);
                Thread.sleep(9999);
                Thread.sleep(9999);
                }
            } catch (Exception e) {}
        }
    }
}
