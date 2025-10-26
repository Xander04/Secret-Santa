package com.santa;

import java.util.HashMap;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ParticipantLogin implements Handler {

    @Override
    public void handle(Context context) throws Exception {
        String id = context.queryParam("EventId");
        HashMap<String, String> details = DBManager.getEventSummary(id);
        if (id == "" || details.get("EventName") == null) {
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
                        <form method="post" action="/Participant" enctype="multipart/form-data">
                            <input type="hidden" id="EventID" name="EventID" value="%s">
                            <label for="email"> Email: </label><br>
                            <input type="text" id="email" name="email"><br>
                            <label for="password"> Password: </label><br>
                            <input type="password" id="password" name="password"><br>
                            <div id="register">
                                <label for="name"> Name: </label><br>
                                <input type="text" id="name" name="name"><br>
                            </div>
                                <div class="loginBreak2"> </div>
                            <button type="button" onclick="submitForm()">Submit</button><br>
                        </form>
                    </div>
                </aside>
            </aside>

        

            <div class="foot1">
            &copy; Xander Dundon 2025
            </div>
        </body>
    </html>
    """, details.get("EventName"), details.get("EventDescription"), id);

        context.html(html);
    }
}
