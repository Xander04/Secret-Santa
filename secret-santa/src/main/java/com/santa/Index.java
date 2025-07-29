package com.santa;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Index implements Handler{
    
    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        html += """
    <!DOCTYPE html>
        <head>
            <title>Secret Santa | Login</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
            <script type="text/javascript" src="script.js"></script>
            <script type="text/javascript" src="index.js"></script>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
            <div class="head1">
            <img src="logo.png" class="logo" width=75px>
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

                <div class="formstyle">
                    <form method="get" action="/Participant" enctype="multipart/form-data">
                        <h1 class="loginTitle"> Log in </h1>
                        <div class="loginBreak1"> </div>
                        <label for="EventId"> Enter event code </label><br>
                        <input type="text" name="EventId" maxlength="4" oninput="this.value = this.value.replace(/[^0-9.]/g, ''); this.value = this.value.replace(/(\\..*)\\./g, '$1');"><br>
                        <div class="loginBreak2"> </div>
                        <button onclick="submitEventId()" class="EventIdInput" style="width: 100%; ">Submit</button><br>
                    </form>
                </div>
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
