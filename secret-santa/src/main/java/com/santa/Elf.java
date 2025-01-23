package com.santa;

import java.util.ArrayList;

public class Elf extends Thread {

    @Override public void run() {

            //Purge old events
            ArrayList<String> events = DBManager.housekeepEvents();

            for (String event : events) {
                DBManager.DeleteEvent(event);
            }

            if (!events.isEmpty()) {
                System.out.println("Purged " + events.size() + " events");
            }

            //Purge old auth tokens
            ArrayList<String> tokens = DBManager.housekeepTokens();
            
            if (!tokens.isEmpty()) {
                System.out.println("Purged " + tokens.size() + " tokens");
            }
    
            for (String token : tokens) {
                DBManager.Deauthenticate(token);
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                System.err.println(e);
            }

            Thread housekeeper = new Elf();
            housekeeper.start();
    }
}
