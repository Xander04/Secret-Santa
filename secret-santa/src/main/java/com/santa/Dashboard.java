package com.santa;

import java.util.HashMap;

import static com.santa.DBManager.getEventSummary;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Dashboard implements Handler{
    
    @Override
    public void handle(Context context) throws Exception {
        String id = context.queryString();

        if (!DBManager.AuthVerify(context.cookie("Auth")).equals(id)) {
            context.redirect("/");
        }

        HashMap<String, String> eventSummary = getEventSummary(id);
        String html = "";
        html += String.format("""
        <!DOCTYPE html>
        <head>
            <title>Secret Santa | Dashboard</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
        </head>
        <body>
            <div class = 'container'>
                <div class = 'sidenav'> 
                    <a href = "/logout"><div class = 'sidenav-item'> <h2> Log Out </h2> </div> </a>
                    <a><div class = 'sidenav-item'> <h2> Summary </h2> </div> </a>
                    <a href = "/report?%s"><div class = 'sidenav-item'> <h2> Report </h2> </div> </a>
                    <a><div class = 'sidenav-item'> <h2> Present </h2> </div> </a>
                </div>
                <div class = 'header'> 
                    <center>
                        <h1> Dashboard </h1><br>
                        <h3> %s - %s</h3><br>
                        <p> Gift Count: %s </p><br>

                    </center>
                </div>
                <div class = 'content' </div>
            </div>
        </body>
    </html>
    """, id, eventSummary.get("EventName"), id, eventSummary.get("GiftCount"));

    context.html(html);
    }
}
