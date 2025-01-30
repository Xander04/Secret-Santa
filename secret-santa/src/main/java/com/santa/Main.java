package com.santa;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;

import static com.santa.DBManager.Authenticate;
import static com.santa.DBManager.InsertToken;
import static com.santa.DBManager.ValidateEventID;

import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class Main {

    public static final int JAVALIN_PORT = 8080;
    public static final String HOSTNAME = "127.0.0.1";
    public static final String CSS_DIR = "com/santa/Resources/CSS/";
    public static final String JS_DIR = "com/santa/Resources/JS/";
    public static final String IMG_DIR = "com/santa/Resources/IMG/";
    public static final String SSL_DIR = "secret-santa/src/main/java/com/santa/Resources/SECURE/";
    public static final boolean SSL_ENABLED = true;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateNewToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    public static String getPwHash(Context ctx) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String hashstr = "";
        String EventPw = ctx.formParam("EventPw");
        if (EventPw != null) {
            byte[] hash = digest.digest(EventPw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%X", b));
            }
            hashstr = sb.toString();
        }
            
        return hashstr;
    }

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(CSS_DIR);
            config.staticFiles.add(JS_DIR);
            config.staticFiles.add(IMG_DIR);

            if (SSL_ENABLED) {
                try {
                    SslPlugin plugin = new SslPlugin(conf -> {
                        conf.pemFromPath(SSL_DIR + "certificate.pem", SSL_DIR + "privateKey.pem");
                        conf.host = HOSTNAME;
                        conf.redirect = true;
                        conf.sniHostCheck = false; //! Enable after testing
                    }); 
                    config.registerPlugin(plugin);
                }
                catch (Exception e) {
                    System.err.println("Unable to start SSL. Have you generated a certificate?");
                    System.err.println(e);
                }
            }
        }).start(JAVALIN_PORT).error(404, config -> config.html("Page not found!")).error(HttpStatus.FORBIDDEN, ctx -> ctx.redirect("/"));
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
        app.get("/presentation", new Presentation());
        
        app.get("/presentation-data", ctx -> {
            String id = ctx.queryString();

        String tkn = DBManager.AuthVerify(ctx.cookie("Auth"));
        if (tkn == null || !tkn.equals(id)) {
            ctx.status(HttpStatus.FORBIDDEN);
        }

        ctx.header("giftContent", DBManager.getPresJson(id));
        ctx.status(HttpStatus.OK);
        });

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
            String EventId = ctx.formParam("EventId");
            String hashstr = getPwHash(ctx);
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

            String hashstr = getPwHash(ctx);

            DBManager.InsertAuth(id, hashstr);

            String tkn = generateNewToken();
            InsertToken(id, tkn);
            ctx.cookie("Auth", tkn);
            ctx.redirect("/Dashboard?" + id);

        });
        app.delete("/report", ctx -> {
            System.out.println("delete" + ctx.header("EventId"));
            System.out.println("auth: " + ctx.cookie("Auth"));
            String eventId = DBManager.AuthVerify(ctx.cookie("Auth"));
            if(eventId != null && eventId.equals(ctx.header("EventId"))) {
                DBManager.deleteGift(ctx.header("GiftId"));
                ctx.status(HttpStatus.OK);
            }
            else {
                System.err.printf("""
                    Invalid auth to delete gift: 
                        Auth: %s
                        EventId: %s 
                        GiftId: %s
                                                """, ctx.cookie("Auth"), ctx.header("EventId"), ctx.header("GiftId"));
                ctx.status(HttpStatus.FORBIDDEN);

            }
        });

    }
}
