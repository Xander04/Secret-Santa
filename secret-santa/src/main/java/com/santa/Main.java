package com.santa;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

import static com.santa.DBManager.Authenticate;
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
        app.get("/login", new Login());

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
        app.post("/login", ctx -> {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String EventId = ctx.formParam("EventId");
            byte[] hash = digest.digest(ctx.formParam("EventPw").getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%X", b));
            }
            String hashstr = sb.toString();
            if (Authenticate(EventId, hashstr)) {
                ctx.html("Success");
            }
            else {
                ctx.html("Login failure");
            }
        });

    }
}
