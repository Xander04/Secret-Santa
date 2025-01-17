package com.santa;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

    public static HashMap<String, String> readEvent(String id) {
        var url = "jdbc:sqlite:secret-santa\\src\\main\\resources\\db\\secret-santa.db";
        HashMap<String, String> data = new HashMap<>();
        String query = String.format("SELECT * FROM Events WHERE EventID = %s", id);

        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            data.put("EventID", Integer.toString(rs.getInt("EventID")));
            data.put("CreationDate", rs.getString("CreationDate"));
            data.put("EventName", rs.getString("EventName"));
            data.put("EventDescription", rs.getString("EventDetails"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    public static void InsertEvent(HashMap<String, String> EventData) {
        var url = "jdbc:sqlite:secret-santa\\src\\main\\resources\\db\\secret-santa.db";
        String query = String.format("INSERT INTO Events (EventID, CreationDate, EventName, EventDescription) VALUES (%s,\"%s\",\"%s\",\"%s\")", EventData.get("EventID"), EventData.get("CreationDate"), EventData.get("EventName"), EventData.get("EventDescription"));

        System.out.println(query);
        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<String, String> readGift(String id) {
        var url = "jdbc:sqlite:secret-santa\\src\\main\\resources\\db\\secret-santa.db";
        HashMap<String, String> data = new HashMap<>();
        String query = String.format("SELECT * FROM Gifts WHERE GiftID = %s", id);

        try (var conn = DriverManager.getConnection(url)) {
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
        var url = "jdbc:sqlite:secret-santa\\src\\main\\resources\\db\\secret-santa.db";
        String query = String.format("SELECT * FROM Gifts WHERE EventID = %s", eventId);

        ArrayList<Integer> gifts = new ArrayList<>();
        try (var conn = DriverManager.getConnection(url)) {
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
        var url = "jdbc:sqlite:secret-santa\\src\\main\\resources\\db\\secret-santa.db";
        String query = String.format("INSERT INTO Gifts (GiftID, EventID, SenderName, RecieverName, GiftDescription) VALUES (%s,\"%s\",\"%s\",\"%s\")", GiftData.get("GiftID"), GiftData.get("EventID"), GiftData.get("SenderName"), GiftData.get("RecieverName"), GiftData.get("GiftDescription"));

        System.out.println(query);
        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
