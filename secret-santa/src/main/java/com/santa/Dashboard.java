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
            <link rel='stylesheet' type='text/css' href='style.css' />
        </head>
        <body>
            <div class = 'container'>
                <div class = 'sidenav'> 
                    <a><div class = 'sidenav-item'> <h2> Home </h2> </div> </a>
                    <a><div class = 'sidenav-item'> <h2> Summary </h2> </div> </a>
                    <a><div class = 'sidenav-item'> <h2> Report </h2> </div> </a>
                    <a><div class = 'sidenav-item'> <h2> Present </h2> </div> </a>
                </div>
                <div class = 'header'> 
                    <center>
                        <h1> Dashboard </h1>
                    </center>
                </div>
                <div class = 'content' </div>
            </div>
        </body>
    </html>
    """;

    context.html(html);
    }
}
