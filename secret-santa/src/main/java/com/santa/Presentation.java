package com.santa;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Presentation implements Handler{

    @Override
    public void handle(Context context) {

        String id = context.queryString();

        if(Helper.Authenticate(context)) {

            String html = "";
            html += String.format("""
            <!DOCTYPE html>
        <head>
            <title>Secret Santa | Login</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
            <script type="text/javascript" src="script.js"></script>
            <script type="text/javascript" src="index.js"></script>
            <script type="text/javascript" src="presentation.js"></script>
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
        <body onload="main(%s)">

            <aside class="index" id = "Log in">
                <form>
                <div id="pres_container" >
                        <h1> %s </h1>
                        <div class="loginBreak"> </div>
                        <p id="name_to">To: </p>
                        <div class="loginBreak2"> </div>
                        <div id="animate" onload="animation()">
                            <p style="display:inline-block;"> From: </p><button type="button"  id="name_from" class="hidden" onclick="revealSender();"></button>
                        </div>
                        <div class="loginBreak2"> </div>
                        <p id="description"> </p>
                        <div class="loginBreak2"> </div>
                        <button type="button" onclick="next()"> Next </button>
                    </div>
                    </form>
            </aside>

        

            <div class="foot1">
            &copy; Xander Dundon 2025
            </div>
        </body>
    </html>
                """,id,  DBManager.getEventSummary(id).get("EventName"), id, id, id, DBManager.getEventSummary(id).get("EventName"));

            context.html(html);
        }
    }

}
