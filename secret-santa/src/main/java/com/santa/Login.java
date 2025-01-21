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
            <img src="logo.png" class="logo" width=75px>
            <div class="titleHolder">
            <h1 class="pageTitle">MachineCode Secret Santa</h1>
            </div>
            <div class="headSpace"> </div>
            <button class="dropDown">Events &#x25BC;</button>
            </div>
            <div class="head_line">
            </div>
        </head>
        <body>

            <aside class="login_left" id = "Log in">

                <div class="formstyle">
                    <form method="post" action="/login" enctype="multipart/form-data">
                        <h1 class="loginTitle"> Log in </h1>
                        <label for="EventId"> Enter event code </label><br>
                        <input type="text" name="EventId"><br>
                        <div class="loginBreak"> </div>
                        <label for="EventPw"> Password </label><br>
                        <input type="password" name="EventPw"><br>
                        <div class="loginBreak2"> </div>
                        <button>Sign in</button><br>
                    </form>
                </div>
            </aside>

            <aside class="login_right" id = "SignUp?">
                <div class="rightSide">
                    <h1 class="dontText1">Don't have an </h1> <h1 class="dontText"> event yet? </h1>
                    <form method="get" action="/register">
                    <button> Create a new event </button>
                    </form>
                </div>
            </aside>

        

            <div class="foot1">
            &copy; Xander Dundon 2025
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
