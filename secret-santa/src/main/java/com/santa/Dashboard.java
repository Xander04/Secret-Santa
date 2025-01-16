package com.santa;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Dashboard implements Handler{
    
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
            <div>
                <div id = 'sidebar'> </div>
                <div id = 'header'> 
                    <center>
                        <h1> Dashboard </h1>
                    </center>
                </div>
                <div id = 'content' </div>
            </div>
        </body>
    </html>
    """;

    context.html(html);
    }
}
