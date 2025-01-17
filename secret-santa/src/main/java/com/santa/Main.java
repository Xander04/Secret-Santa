package com.santa;

import io.javalin.Javalin;

public class Main {

    public static final int JAVALIN_PORT = 80;
    public static final String CSS_DIR = "com/santa/CSS/";

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(CSS_DIR);
        }).start(JAVALIN_PORT).error(404, config -> config.html("Page not found!"));
        configureRoutes(app);

    }

    public static void configureRoutes(Javalin app) {
        // All webpages are listed here as GET pages
        app.get("/", new Index());
        app.get("/Dashboard", new Dashboard());
        app.get("/Participant", new Participant());         //?Rename

    }
}
