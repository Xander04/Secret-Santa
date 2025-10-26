package com.santa;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public static HashMap<String, String> readUser(String id) {
        HashMap<String, String> data = new HashMap<>();
        String query = String.format("SELECT * FROM User WHERE UserID = %s", id);

        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            if (rs.next()) {
                data.put("UserID", Integer.toString(rs.getInt("UserID")));
                data.put("EventID", Integer.toString(rs.getInt("EventID")));
                data.put("Email", rs.getString("Email "));
                data.put("PasswordHash", rs.getString("PasswordHash"));
                data.put("Name", rs.getString("Name"));
                if (rs.getObject("RecieverID") != null) {
                    data.put("RecieverID", Integer.toString(rs.getInt("RecieverID")));
                }
                data.put("GiftDescription", rs.getString("GiftDescription"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    public static ArrayList<Integer> getUsersFromEvent(String eventId) {
        String query = String.format("SELECT * FROM User WHERE EventID = %s", eventId);

        ArrayList<Integer> users = new ArrayList<>();
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            while (rs.next()) {
                users.add(rs.getInt("UserID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    public static void InsertUser(HashMap<String, String> UserData) {
        String query = String.format("INSERT INTO User (EventID, [Email ], PasswordHash, Name, GiftDescription) VALUES (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", 
            UserData.get("EventID"), UserData.get("Email"), UserData.get("PasswordHash"), UserData.get("Name"), UserData.get("GiftDescription"));

        System.out.println(query);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);

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

    public static String AuthenticateUser(String email, String passwordHash) {
        String query = String.format("SELECT UserID FROM User WHERE [Email ] = \"%s\" AND PasswordHash = \"%s\"", email, passwordHash);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            if (rs.next()) {
                return Integer.toString(rs.getInt("UserID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Authentication failed
    }

    public static boolean AuthenticateEvent(String eventId, String passwordHash) {
        String query = String.format("SELECT EventId FROM Auth WHERE EventId = \"%s\" AND PwHash = \"%s\"", eventId, passwordHash);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Authentication failed
    }

    public static void Deauthenticate(String tkn) {
        String query1 = String.format("DELETE FROM Tokens WHERE Token = \"%s\"", tkn);
        String query2 = String.format("DELETE FROM UserToken WHERE Token = \"%s\"", tkn);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void InsertEventAuth(String eventId, String passwordHash) {
        String query = String.format("INSERT INTO Auth (EventId, PwHash) VALUES (\"%s\", \"%s\")", eventId, passwordHash);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void InsertToken(String eventId, String token) {
        String query = String.format("INSERT INTO Tokens (Token, EventId, Created) VALUES (\"%s\", \"%s\", \"%s\")", token, eventId, Instant.now().toString());
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void InsertUserToken(String userId, String token) {
        String query = String.format("INSERT INTO UserToken (UserId, Token) VALUES (\"%s\", \"%s\")", userId, token);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String AuthVerifyEventToken(String token) {
        String query = String.format("SELECT EventId FROM Tokens WHERE Token = \"%s\"", token);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getString("EventId");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Token not found or expired
    }

    public static String AuthVerifyUserToken(String token) {
        String query = String.format("SELECT UserId FROM UserToken WHERE Token = \"%s\"", token);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            if (rs.next()) {
                return Integer.toString(rs.getInt("UserId"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Token not found
    }

    public static HashMap<String, String> getEventSummary(String EventId) {
        HashMap<String, String> results = new HashMap<>();

        HashMap<String, String> EventInfo = readEvent(EventId);
        results.put("EventName", EventInfo.get("EventName"));
        results.put("EventDescription", EventInfo.get("EventDescription"));

        String query = String.format("SELECT COUNT(*) FROM User WHERE EventID = \"%s\"", EventId);
        int count = 0;
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);

            if (rs.next()) {
                count = rs.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        results.put("UserCount", Integer.toString(count));

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
                if (ChronoUnit.DAYS.between(Instant.parse(rs.getString("CreationDate")), Instant.now()) > 14) {
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
                if (ChronoUnit.DAYS.between(Instant.parse(rs.getString("Created")), Instant.now()) > 1) {
                    toPurge.add(rs.getString("Token"));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return toPurge;
    }

    public static void DeleteEvent(String id) {
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            
            // Delete user tokens for users in this event
            stmt.executeUpdate(String.format("DELETE FROM UserToken WHERE UserId IN (SELECT UserID FROM User WHERE EventID = %s)", id));
            
            // Delete users from this event
            stmt.executeUpdate(String.format("DELETE FROM User WHERE EventID = %s", id));
            
            // Delete auth for this event
            stmt.executeUpdate(String.format("DELETE FROM Auth WHERE EventId = '%s'", id));
            
            // Delete tokens for this event
            stmt.executeUpdate(String.format("DELETE FROM Tokens WHERE EventId = '%s'", id));
            
            // Delete the event itself
            stmt.executeUpdate(String.format("DELETE FROM Events WHERE EventID = %s", id));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUser(String userId) {
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            
            // Delete user tokens
            stmt.executeUpdate(String.format("DELETE FROM UserToken WHERE UserId = %s", userId));
            
            // Delete the user
            stmt.executeUpdate(String.format("DELETE FROM User WHERE UserID = %s", userId));
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getPresJson(String EventId) {
        JSONArray assignments = new JSONArray();
        String query = String.format("SELECT u1.UserID, u1.Name as SenderName, u2.Name as RecipientName, u1.GiftDescription FROM User u1 LEFT JOIN User u2 ON u1.RecieverID = u2.UserID WHERE u1.EventID = '%s'", EventId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            int id = 0;

            while (rs.next()) {
                JSONObject assignment = new JSONObject();
                assignment.put("UserId", rs.getString("UserID"));
                assignment.put("SenderName", rs.getString("SenderName"));
                assignment.put("RecipientName", rs.getString("RecipientName"));
                assignment.put("GiftDescription", rs.getString("GiftDescription"));

                assignments.put(id, assignment);
                id++;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return assignments.toString();
    }

    // Additional helper methods for the new schema
    
    public static String getUserIdByEmail(String email) {
        String query = String.format("SELECT UserID FROM User WHERE [Email ] = '%s'", email);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return Integer.toString(rs.getInt("UserID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public static void assignSecretSanta(String giverId, String receiverId) {
        String query = String.format("UPDATE User SET RecieverID = %s WHERE UserID = %s", receiverId, giverId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void updateGiftDescription(String userId, String giftDescription) {
        String query = String.format("UPDATE User SET GiftDescription = '%s' WHERE UserID = %s", giftDescription, userId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getSecretSanta(String userId) {
        String query = String.format("SELECT RecieverID FROM User WHERE UserID = %s", userId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return rs.getString("ReceiverID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "-1"; // No receiver assigned
    }
    
    public static String getUserName (String userId) {
        String query = String.format("SELECT Name FROM User WHERE UserID = %s", userId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return rs.getString("Name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean isEmailTaken(String email, String eventId) {
        String query = String.format("SELECT COUNT(*) FROM User WHERE [Email ] = '%s' AND EventID = %s", email, eventId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return rs.getInt("COUNT(*)") > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    public static String getUserEventId(String userId) {
        String query = String.format("SELECT EventID FROM User WHERE UserID = %s", userId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return Integer.toString(rs.getInt("EventID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    // Additional authentication helper methods
    
    public static void DeauthenticateUser(String userId) {
        String query = String.format("DELETE FROM UserToken WHERE UserId = %s", userId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void DeauthenticateEvent(String eventId) {
        String query = String.format("DELETE FROM Tokens WHERE EventId = '%s'", eventId);
        try (var conn = DriverManager.getConnection(URL)) {
            System.out.println("Connection to SQLite has been established.");
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static boolean isValidEventToken(String token) {
        return AuthVerifyEventToken(token) != null;
    }
    
    public static boolean isValidUserToken(String token) {
        return AuthVerifyUserToken(token) != null;
    }
    
    public static String getUserByToken(String token) {
        return AuthVerifyUserToken(token);
    }
    
    public static String getEventByToken(String token) {
        return AuthVerifyEventToken(token);
    }

}
