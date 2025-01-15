package com.santa;

import com.santa.Pages.Dashboard;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        


        var app = Javalin.create(/*config*/)
            .get("/", ctx -> ctx.html("Hello World"))
            .get("/dashboard", ctx -> ctx.html(new Dashboard().html))
            .error(404, ctx -> ctx.html("Page not found!"))
            .start(7070);
    }
}