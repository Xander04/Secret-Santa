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
            <title>Secret Santa | Dashboard</title>
            <link rel='stylesheet' type='text/css' href='CSS\style.css' />
        </head>
        <body>
            
        </body>
    </html>
    """;

    context.html(html);
    }
}
