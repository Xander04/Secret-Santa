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
            <link rel='stylesheet' type='text/css' href='CSS\style.css' />
        </head>
        <body>
            <form method="post" action="/" enctype="multipart/form-data">
                <label for="EventId"> Enter Event Code: </label><br>
                <input type="text" name="EventId"><br>
                <button>Submit</button><br>
            </form>
        </body>
    </html>
    """;

    context.html(html);
    }
}
