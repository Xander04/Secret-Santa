package com.santa;

import java.sql.DriverManager;
import java.sql.SQLException;
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

            System.out.printf("""
                    EventID: %d
                    Creation Date: %s
                    Event name: %s
                    Event Description: %s
                    """, rs.getInt("EventID"), rs.getString("CreationDate"), rs.getString("EventName"), rs.getString("EventDetails"));

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
            var rs = stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
