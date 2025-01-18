package com.santa;

import java.util.HashMap;

import static com.santa.DBManager.ValidateEventID;

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

        app.post("/", ctx -> {
            String url = "/Participant?" + ctx.formParam("EventId");
            ctx.redirect(url);
        });
        app.post("/Participant", ctx -> {
            HashMap<String, String> data = new HashMap<>();
            var a = ctx.formParamMap();
            data.put("EventID", ctx.formParam("EventID"));
            data.put("SenderName", ctx.formParam("SenderName"));
            data.put("RecieverName", ctx.formParam("RecieverName"));
            data.put("GiftDescription", ctx.formParam("GiftDescription"));
            
            if (ValidateEventID(data.get("EventID"))) {
                DBManager.InsertGift(data);

            ctx.html("Done!");
            }
            else{
                ctx.html("Event Not Found!");
            }
            
        });

    }
}
