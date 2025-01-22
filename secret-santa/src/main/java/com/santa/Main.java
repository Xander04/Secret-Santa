package com.santa;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;

import static com.santa.DBManager.Authenticate;
import static com.santa.DBManager.InsertToken;
import static com.santa.DBManager.ValidateEventID;

import io.javalin.Javalin;

public class Main {

    public static final int JAVALIN_PORT = 8080;
    public static final String CSS_DIR = "com/santa/Resources/CSS/";
    public static final String JS_DIR = "com/santa/Resources/JS/";
    public static final String IMG_DIR = "com/santa/Resources/IMG/";

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateNewToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(CSS_DIR);
            config.staticFiles.add(JS_DIR);
            config.staticFiles.add(IMG_DIR);
        }).start(JAVALIN_PORT).error(404, config -> config.html("Page not found!"));
        configureRoutes(app);

        Thread housekeeper = new Elf();
        housekeeper.start();

    }

    public static void configureRoutes(Javalin app) {
        // All webpages are listed here as GET pages
        app.get("/", new Index());
        app.get("/Dashboard", new Dashboard());
        app.get("/Participant", new Participant());         //?Rename
        app.get("/login", new Login());
        app.get("/register", new Register());
        app.get("/report", new Report());

        app.get("/logout", ctx -> {
            String tkn = ctx.cookie("Auth");
            DBManager.Deauthenticate(tkn);
            ctx.removeCookie("Auth");
            ctx.redirect("/");
        });

        app.post("/", ctx -> {
            String url = "/Participant?" + ctx.formParam("EventId");
            ctx.redirect(url);
        });
        app.post("/Participant", ctx -> {
            HashMap<String, String> data = new HashMap<>();
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
                System.out.println(ctx.formParam("tokenise"));
                String tkn = generateNewToken();
                InsertToken(EventId, tkn);
                ctx.cookie("Auth", tkn);
                ctx.redirect("/Dashboard?" + EventId);
            }
            else {
                ctx.html("Login failure");
            }
        });
        app.post("/register", ctx -> {
            HashMap<String, String> data = new HashMap<>();
            data.put("EventName", ctx.formParam("EventName"));
            data.put("EventDescription", ctx.formParam("EventDescription"));
            data.put("CreationDate", Instant.now().toString());
            String id = String.format("%04d", secureRandom.nextInt(9999));
            while (ValidateEventID(id)){
                id = String.format("%04d", secureRandom.nextInt(9999));
            }
            data.put("EventID", id);
            DBManager.InsertEvent(data);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(ctx.formParam("EventPw").getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%X", b));
            }
            String hashstr = sb.toString();

            DBManager.InsertAuth(id, hashstr);

            String tkn = generateNewToken();
            InsertToken(id, tkn);
            ctx.cookie("Auth", tkn);
            ctx.redirect("/Dashboard?" + id);

        });

    }
}
