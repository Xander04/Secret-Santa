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
            <title>Secret Santa | Home</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
            <script type="text/javascript" src="script.js"></script>
        </head>
        <body>
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
            <aside class="buffer"></aside>
            <aside class="Event_Selection" id = "Log in">

                <div class="formstyle">
                    <form method="post" action="/" enctype="multipart/form-data">
                        <label for="EventId"> Enter Event Code: </label><br>
                        <input type="text" name="EventId" maxlength="4"><br>
                        <button>Submit</button><br>
                    </form>
                </div>
            </aside>
            <aside class="buffer"></aside>
            
            

            <div class="foot1">
                &copy; Xander Dundon 2025
            </div>
        </body>
    </html>
    """;

    context.html(html);
    }
}
