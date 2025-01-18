package com.santa;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Login implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        html += """
        <!DOCTYPE html>
        <head>
            <title>Secret Santa | Login</title>
            <link rel='stylesheet' type='text/css' href='CSS\style.css' />
        </head>
        <body>
            <form method="post" action="/login" enctype="multipart/form-data">
                <label for="EventId"> Enter Event Code: </label><br>
                <input type="text" name="EventId"><br>
                <label for="EventPw"> Enter Password: </label><br>
                <input type="password" name="EventPw"><br>
                <button>Submit</button><br>
            </form>
        </body>
    </html>
    """;

    context.html(html);
    }
}
