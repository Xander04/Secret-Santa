package com.santa;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

    private static final String URL = "jdbc:sqlite:secret-santa/src/main/resources/db/secret-santa.db";
    
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
    public static void Deauthenticate(String tkn) {
        String query = String.format("DELETE FROM Tokens WHERE \"Token\" = \"%s\"", tkn);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void InsertAuth(String id, String hash) {
        String query = String.format("INSERT INTO Auth (EventId, PwHash) VALUES (\"%s\", \"%s\")", id, hash);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void InsertToken(String id, String token) {
        String query = String.format("INSERT INTO Tokens (Token, EventId, Created) VALUES (\"%s\", \"%s\", \"%s\")", token, id, Instant.now().toString());
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String AuthVerify(String tkn) {
        String query = String.format("SELECT * FROM Tokens WHERE Token = \"%s\"", tkn);
        String events = null;
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            events = rs.getString("EventId");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return events;
    }

    public static HashMap<String, String> getEventSummary(String EventId) {
        HashMap<String, String> results = new HashMap<>();

        HashMap<String, String> EventInfo = readEvent(EventId);
        results.put("EventName", EventInfo.get("EventName"));
        results.put("EventDescription", EventInfo.get("EventDescription"));

        String query = String.format("SELECT COUNT(*) FROM Gifts WHERE EventID = \"%s\"", EventId);
        int count = 0;
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            count = rs.getInt("COUNT(*)");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        results.put("GiftCount", Integer.toString(count));

        return results;
    }

    public static ArrayList<String> housekeepEvents() {
        ArrayList<String> toPurge = new ArrayList<>();
        String query = "SELECT EventID, CreationDate FROM Events";
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            while (rs.next()) {
                if (ChronoUnit.DAYS.between(Instant.parse(rs.getString("CreationDate")),Instant.now()) > 14) {
                    toPurge.add(rs.getString("EventID"));
                }
            }

            for (String event : toPurge) {
                DBManager.DeleteEvent(event);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return toPurge;
    }

    public static ArrayList<String> housekeepTokens() {
        ArrayList<String> toPurge = new ArrayList<>();
        String query = "SELECT Token, Created FROM Tokens";
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            while (rs.next()) {
                if (ChronoUnit.DAYS.between(Instant.parse(rs.getString("Created")),Instant.now()) > 1) {
                    toPurge.add(rs.getString("Token"));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return toPurge;
    }

    public static void DeleteEvent(String id) {
        String query1 = String.format("DELETE FROM Gifts WHERE \"EventID\" = \"%s\"", id);
        String query2 = String.format("DELETE FROM Auth WHERE \"EventId\" = %s", id);
        String query3 = String.format("DELETE FROM Tokens WHERE \"EventId\" = \"%s\"", id);
        String query4 = String.format("DELETE FROM Events WHERE \"EventID\" = %s", id);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query2);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query3);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query4);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteGift(String giftId) {
        String query = String.format("DELETE FROM Gifts WHERE \"GiftID\" = \"%s\"", giftId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeQuery(query);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        
        }
    }

}
