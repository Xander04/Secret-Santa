package com.santa;

import static com.santa.DBManager.AuthVerify;

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
            <link rel='stylesheet' type='text/css' href='style.css' />
            <div class="head1">
            </div>
            <div class="head_line">
            </div>
        </head>
        <body>

            <aside class="login_left" id = "Log in">

                <div class="formstyle">
                    <form method="post" action="/login" enctype="multipart/form-data">
                        <label for="EventId"> Enter Event Code: </label><br>
                        <input type="text" name="EventId"><br>
                        <label for="EventPw"> Enter Password: </label><br>
                        <input type="password" name="EventPw"><br>
                        <button>Submit</button><br>
                    </form>
                </div>
            </aside>

            <aside class="login_right" id = "SignUp?">

            </aside>

            <div class="foot1">
            </div>
        </body>
    </html>
    """;

    context.html(html);
    
    String tkn = context.cookie("Auth");
    String id = AuthVerify(tkn);
    if(id != null) {
        context.redirect("/Dashboard?" + id);
    }

    }
}
