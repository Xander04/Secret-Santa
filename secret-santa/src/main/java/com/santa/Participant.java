package com.santa;

import java.util.HashMap;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Participant implements Handler{
    
    @Override
    public void handle(Context context) throws Exception {
        String id = context.queryString();

        HashMap<String, String> eventInfo = DBManager.readEvent(id);

        HashMap<String, String> data = new HashMap<>();

        data.put("EventID", "69");
        data.put("CreationDate", "1/1/1999");
        data.put("EventName", "Machine code");
        data.put("EventDescription", "Crimmis");

        DBManager.InsertEvent(data);
        String html = "";
        html += String.format("""
        <!DOCTYPE html>
        <head>
            <title>Secret Santa | Dashboard</title>
            <link rel='stylesheet' type='text/css' href='CSS\style.css' />
        </head>
        <body>
            %s
        </body>
    </html>
    """, eventInfo.get("EventName"));

    context.html(html);
    }
}
