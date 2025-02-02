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
        </head>
        <body>
            <form method="post" action="/" enctype="multipart/form-data">
                <label for="EventId"> Enter Event Code: </label><br>
                <input type="text" name="EventId"><br>
                <button>Submit</button><br>
            </form>
            <a href = "/register"> Create Event </a> <br>
            <a href="/login"> Manage Event </a>
        </body>
    </html>
    """;

    context.html(html);
    }
}
