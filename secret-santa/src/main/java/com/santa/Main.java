package com.santa;

import com.santa.Pages.Dashboard;
import com.santa.Pages.Index;
import com.santa.Pages.Participant;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        


        var app = Javalin.create(/*config*/)
            .get("/", ctx -> ctx.html(new Index().html))
            .get("/dashboard", ctx -> ctx.html(new Dashboard().html))
            .get("/participant", ctx -> ctx.html(new Participant().html))
            .error(404, ctx -> ctx.html("Page not found!"))
            .start(7070);
    }
}