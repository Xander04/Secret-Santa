package com.santa;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Presentation implements Handler{

    @Override
    public void handle(Context context) {

        String id = context.queryString();

        String tkn = DBManager.AuthVerify(context.cookie("Auth"));
        if (tkn == null || !tkn.equals(id)) {
            context.redirect("/");
        }

        String html = "";
        html += String.format("""
        <!DOCTYPE html>
        <head>
            <title>Secret Santa | Register Event</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <script src="presentation.js"></script> 
        </head>
        <body onload="main(%s)">
            <div id="pres_container" >
                <h1> %s </h1>
                <p id="name_to">To: </p>
                <p style="display:inline-block;"> From: </p><button id="name_from" class="hidden" onclick="revealSender()"></button>
                <p id="description"> </p>
                <button onclick="next()"> Next </button>
            </div>
        </body>
    </html>
            """,id,  DBManager.getEventSummary(id).get("EventName"));

        context.html(html);
    }

}
