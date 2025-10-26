package com.santa;

import java.util.HashMap;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Participant implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        System.out.println("Participant page accessed");
        System.out.println("Query String: " + context.queryString());
        String UserId = context.queryString();
        String EventId = DBManager.getUserEventId(UserId);
        HashMap<String, String> details = DBManager.getEventSummary(EventId);

        if (EventId == "" || details.get("EventName") == null) {
            context.redirect("/");
        }

        if (DBManager.AuthVerifyUserToken(context.cookie("Auth")) != null && DBManager.AuthVerifyUserToken(context.cookie("Auth")).equals(UserId)) {
            System.out.println("User authenticated");
        } else {
            context.redirect("/");
        }

        String html = "";
        html += String.format("""
    <!DOCTYPE html>
        <head>
            <title>Secret Santa | Login</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
            <script type="text/javascript" src="script.js"></script>
            <script type="text/javascript" src="index.js"></script>
            <script type="text/javascript" src="participant.js"></script>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
            <div class="head1">
            <a href="/"><img src="logo.png" class="logo" width=75px></a>
            <div class="titleHolder">
            <h1 class="pageTitle">MachineCode Secret Santa</h1>
            </div>
            <div class="headSpace"> </div>
                <div class="dropdown">
                    <button onclick="dropFunction()" class="dropDown">Events &#x25BC;</button>
                    <div id="eventDrop" class="dropdown-content dropdown-content-el">
                        <a href="/register">Create</a>
                        <div class="dropdown-line"> </div>
                    </div>
                    <div id="eventDrop1" class="dropdown-content-1 dropdown-content-el">
                        <a href="/login">Manage</a>
                    </div>
                </div>
            </div>
            <div class="head_line">
            </div>
        </head>
        <body>

            <aside class="index" id = "Log in">
                <aside class="login_left" id = "Log in">
                    <div class="formstyle">
                    <form>
                        <h1> %s </h1><br>
                        <h3> %s </h3><br>
                        </form>
                    </div>
                </aside>
                <aside class="login_left" id = "Log in">
                    <div class="formstyle">
                    """, details.get("EventName"), details.get("EventDescription"));
                    System.out.println(DBManager.getSecretSanta(UserId));
                            if (DBManager.getSecretSanta(UserId) != null) {
                                html += String.format("""
                                <form method="post" action="/Participant" enctype="multipart/form-data">
                                            <h2>Your Secret Santa is: %s</h2>
                                            <div class="loginBreak"></div>
                                    <input type="hidden" id="UserID" name="UserID" value="%s">
                                    <input type="hidden" id="EventID" name="EventID" value="%s">
                                    <label for="gift"> Gift Description: </label><br>
                                    <textarea id="gift" name="gift"></textarea><br>
                                    <div class="loginBreak2"> </div>
                                    <button>Submit</button><br>
                                </form>
                                """, DBManager.getUserName(DBManager.getSecretSanta(UserId)), UserId, EventId);
                            }
                            else {
                            html += String.format("""
                                <h2>Your Secret Santa has not been assigned yet. Please check back later!</h2>
                            """);
                            }
                        html += """
                </aside>
            </aside>
            <div class="foot1">
            &copy; Xander Dundon 2025
            </div>
        </body>
    </html>
    """;

        context.html(html);
    }
}
