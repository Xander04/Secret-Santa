package com.santa;

import java.util.ArrayList;

public class Elf extends Thread {

    @Override public void run() {

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
                Thread.sleep(9999);
            } catch (InterruptedException e) {
                System.err.println(e);
            }

            Thread housekeeper = new Elf();
            housekeeper.start();
    }
}
