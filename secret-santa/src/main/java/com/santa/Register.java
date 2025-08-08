package com.santa;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Register implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        html += String.format("""
    <!DOCTYPE html>
        <head>
            <title>Secret Santa | Login</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
            <script type="text/javascript" src="script.js"></script>
            <script type="text/javascript" src="index.js"></script>
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

                <div class="formstyle">
                    <form method="post" action="/register" enctype="multipart/form-data">
                        <h1 class="loginTitle"> Register Event </h1>
                        <label for="EventName"> Event Name: </label><br>
                        <input type="text" name="EventName"><br>
                        <label for="EventDescription"> Event Description: </label><br>
                        <input type="text" name="EventDescription"><br>
                        <label for="EventPw"> Password: </label><br>
                        <input type="password" name="EventPw"><br>
                        <div class="loginBreak2"> </div>
                        <button>Submit</button><br>
                    </form>
                </div>
            </aside>

        

            <div class="foot1">
            &copy; Xander Dundon 2025
            </div>
        </body>
    </html>
    """);

        context.html(html);
    }
}
