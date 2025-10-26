package com.santa;

import java.util.HashMap;

import static com.santa.DBManager.getEventSummary;
import static com.santa.DBManager.getUsersFromEvent;
import static com.santa.DBManager.readUser;
import static com.santa.DBManager.getSecretSanta;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Dashboard implements Handler{
    
    @Override
    public void handle(Context context) throws Exception {
        String id = context.queryString();
        
        if (Helper.Authenticate(context)) {

            HashMap<String, String> eventSummary = getEventSummary(id);
            java.util.ArrayList<Integer> userIds = getUsersFromEvent(id);

            // Build participants HTML list
            String participantsHtml = "";
            if (userIds != null && !userIds.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("<div class=\"participants\">\n");
                sb.append("<h2>Participants</h2>\n");
                sb.append("<table class=\"participants-table\">\n");
                sb.append("<tr><th>Name</th><th>Assigned</th></tr>\n");
                for (int userId : userIds) {
                    java.util.HashMap<String, String> userInfo = readUser(Integer.toString(userId));
                    String name = userInfo.getOrDefault("Name", "(no name)");
                    String receiverId = getSecretSanta(Integer.toString(userId));
                    boolean assigned = receiverId != null && !receiverId.equals("-1");
                    String checked = assigned ? "checked" : "";
                    sb.append(String.format("<tr><td>%s</td><td><input class=\"assigned-checkbox\" type=\"checkbox\" disabled %s></td></tr>\n", name, checked));
                }
                sb.append("</table>\n");
                sb.append("</div>\n");
                participantsHtml = sb.toString();
            } else {
                participantsHtml = "<div class=\"participants\"><h2>No participants yet</h2></div>";
            }

            String html = "";
            html += String.format("""
            <!DOCTYPE html>
        <head>
            <title>Secret Santa | Dashboard</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
            <script type="text/javascript" src="script.js"></script>
            <script type="text/javascript" src="index.js"></script>
            <script>
                function assignSecretSantas(eventId) {
                    fetch(`/assign-secret-santas?${eventId}`, {
                        method: 'POST'
                    })
                    .then(response => {
                        if (response.ok) {
                            alert('Secret Santas assigned successfully!');
                            location.reload();
                        } else {
                            alert('Failed to assign Secret Santas. Please try again.');
                        }
                    })
                    .catch(err => {
                        console.error('Error assigning Secret Santas:', err);
                        alert('An error occurred. Please try again.');
                    });
                }
            </script>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
            <div class="head1">
            <a href="/"><img src="logo.png" class="logo" width=75px></a>
            <div class="titleHolder">
            <h1 class="pageTitle">%s - %s</h1>
            </div>
            <div class="headSpace"> </div>
                <div class="dropdown">
                    <button onclick="dropFunction()" class="dropDown">Manage &#x25BC;</button>
                    <div id="eventDrop" class="dropdown-content dropdown-content-el">
                        <a href="/logout">Logout</a>
                        <div class="dropdown-line"> </div>
                    </div>
                    <div id="eventDrop1" class="dropdown-content-1 dropdown-content-el">
                        <a href="/login">Summary</a>
                        <div class="dropdown-line"> </div>
                    </div>
                    <div id="eventDrop2" class="dropdown-content-2 dropdown-content-el">
                        <a href="/report?%s">Report</a>
                        <div class="dropdown-line"> </div>
                    </div>
                    <div id="eventDrop3" class="dropdown-content-3 dropdown-content-el">
                        <a href="/presentation?%s">Present</a>
                    </div>
                </div>
            </div>
            <div class="head_line">
            </div>
        </head>
        <body>

            <aside class="index" id = "Log in">
                <div class="formstyle">
                <form method="post" action="/Participant" enctype="multipart/form-data">
                        <h1>Participant Count: %s</h1>
                        <button type="button" onclick="assignSecretSantas('%s')">Assign Secret Santas</button>
                    </form>
                </div>
                <div class="formstyle">
                    <form>
                        %s
                    </form>

                </div>
                
            </aside>

            <div class="foot1">
            &copy; Xander Dundon 2025
            </div>
        </body>
    </html>
            """, eventSummary.get("EventName"), id, id, id, eventSummary.get("UserCount"), id, participantsHtml);

            context.html(html);
        }
    }
}
