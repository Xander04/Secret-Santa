package com.santa;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

    private static final String URL = "jdbc:sqlite:secret-santa\\src\\main\\resources\\db\\secret-santa.db";
    
    public static HashMap<String, String> readEvent(String id) {
        HashMap<String, String> data = new HashMap<>();
        String query = String.format("SELECT * FROM Events WHERE EventID = %s", id);

        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            data.put("EventID", Integer.toString(rs.getInt("EventID")));
            data.put("CreationDate", rs.getString("CreationDate"));
            data.put("EventName", rs.getString("EventName"));
            data.put("EventDescription", rs.getString("EventDescription"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    public static void InsertEvent(HashMap<String, String> EventData) {
        String query = String.format("INSERT INTO Events (EventID, CreationDate, EventName, EventDescription) VALUES (%s,\"%s\",\"%s\",\"%s\")", EventData.get("EventID"), EventData.get("CreationDate"), EventData.get("EventName"), EventData.get("EventDescription"));

        System.out.println(query);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<String, String> readGift(String id) {
        HashMap<String, String> data = new HashMap<>();
        String query = String.format("SELECT * FROM Gifts WHERE GiftID = %s", id);

        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            data.put("GiftID", Integer.toString(rs.getInt("GiftID")));
            data.put("EventID", Integer.toString(rs.getInt("EventID")));
            data.put("SenderName", rs.getString("SenderName"));
            data.put("RecieverName", rs.getString("RecieverName"));
            data.put("GiftDescription", rs.getString("GiftDescription"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    public static ArrayList<Integer> GiftsFromEvent(String eventId) {
        String query = String.format("SELECT * FROM Gifts WHERE EventID = %s", eventId);

        ArrayList<Integer> gifts = new ArrayList<>();
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            while (rs.next()) {
                gifts.add(rs.getInt("giftID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return gifts;
    }   

    public static void InsertGift(HashMap<String, String> GiftData) {
        
        String query = String.format("INSERT INTO Gifts (EventID, SenderName, RecieverName, GiftDescription) VALUES (\"%s\",\"%s\",\"%s\",\"%s\")", GiftData.get("EventID"), GiftData.get("SenderName"), GiftData.get("RecieverName"), GiftData.get("GiftDescription"));

        System.out.println(query);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean ValidateEventID(String id) {
        String query = String.format("SELECT * FROM Events WHERE EventID = %s", id);

        ArrayList<Integer> events = new ArrayList<>();
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            while (rs.next()) {
                events.add(rs.getInt("EventID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return !events.isEmpty();
                  
    }

    public static boolean Authenticate(String id, String hash) {
        String query = String.format("SELECT * FROM Auth WHERE EventID = \"%s\" AND PwHash = \"%s\"", id, hash);
        ArrayList<Integer> events = new ArrayList<>();
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            while (rs.next()) {
                events.add(rs.getInt("EventID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return !events.isEmpty();
    }


}
