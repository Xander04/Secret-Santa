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
            <title>Secret Santa | Register Event</title>
            <link rel='stylesheet' type='text/css' href='CSS\style.css' />
        </head>
        <body>
            <form method="post" action="/register" enctype="multipart/form-data">
                <label for="EventName"> Event Name: </label><br>
                <input type="text" name="EventName"><br>
                <label for="EventDescription"> Event Description: </label><br>
                <input type="text" name="EventDescription"><br>
                <label for="EventPw"> Password: </label><br>
                <input type="password" name="EventPw"><br>
                <button>Submit</button><br>
            </form>
        </body>
    </html>
    """);

        context.html(html);
    }
}
