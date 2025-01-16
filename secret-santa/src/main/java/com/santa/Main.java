package com.santa;

import io.javalin.Javalin;

public class Main {
    public static final int         JAVALIN_PORT    = 80;
    public static final String      CSS_DIR         = "css/";
    public static void main(String[] args) {
        


        Javalin app = Javalin.create(config -> {
        }).start(JAVALIN_PORT).error(404, config -> config.html("Page not found!"));
        configureRoutes(app);

    }

    public static void configureRoutes(Javalin app) {
        // All webpages are listed here as GET pages
        app.get("/Dashboard", new Dashboard());

    }
}