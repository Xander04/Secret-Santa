package com.santa;

import java.util.HashMap;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Participant implements Handler {

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
            <title>Secret Santa | Submit Gift</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
        </head>
        <body>
            <h1> %s </h1>
            <h3> %s </h3><br>
            <form method="post" action="/Participant" enctype="multipart/form-data">
                <input type="hidden" name="EventID" value="%s">
                <label for="SenderName"> Your Name: </label><br>
                <input type="text" name="SenderName"><br>
                <label for="RecieverName"> Recipients Name: </label><br>
                <input type="text" name="RecieverName"><br>
                <label for="GiftDescription"> Description of gift wrapping: </label><br>
                <textarea type="long-text" name="GiftDescription"></textarea><br>
                <button>Submit</button><br>
            </form>
        </body>
    </html>
    """, details.get("EventName"), details.get("EventDescription"), id);

        context.html(html);
    }
}
