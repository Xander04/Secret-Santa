package com.santa;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static com.santa.DBManager.Authenticate;
import static com.santa.DBManager.InsertToken;
import static com.santa.DBManager.ValidateEventID;

import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.http.HttpStatus;

public class Main {

    public static final int JAVALIN_PORT = 8080;
    public static final int SSL_PORT = 4430;
    public static final String HOSTNAME = "192.168.1.100";
    public static final String CSS_DIR = "com/santa/Resources/CSS/";
    public static final String JS_DIR = "com/santa/Resources/JS/";
    public static final String IMG_DIR = "com/santa/Resources/IMG/";
    public static final String SSL_DIR = "SECURE/";
    public static final boolean SSL_ENABLED = true;

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            // Add static files
            config.staticFiles.add(CSS_DIR);
            config.staticFiles.add(JS_DIR);
            config.staticFiles.add(IMG_DIR);

            // Configure SSL
            if (SSL_ENABLED) {
                try {
                    SslPlugin plugin = new SslPlugin(conf -> {
                        conf.pemFromClasspath(SSL_DIR + "fullchain1.pem", SSL_DIR + "privkey1.pem");
                        conf.redirect = true;
                        conf.insecurePort = JAVALIN_PORT;
                        conf.securePort = SSL_PORT;
                        conf.host = HOSTNAME;
                        conf.sniHostCheck = true; //! Enable after testing
                    }); 
                    config.registerPlugin(plugin);
                } catch (Exception e) {
                    System.err.println("Unable to start SSL. Have you generated a certificate?");
                    System.err.println(e);
                }
            }
        }).start(JAVALIN_PORT)
                // Set handling for errors
                .error(HttpStatus.NOT_FOUND, ctx -> ctx.html("Page not found!"))
                .error(HttpStatus.FORBIDDEN, ctx -> {
                    ctx.html("<script>alert('You do not have access to this page') </script>");
                    ctx.redirect("/");
                })
                .error(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, ctx -> ctx.redirect("/"))
                .error(HttpStatus.INTERNAL_SERVER_ERROR, ctx -> ctx.html("Internal Server Error"));
        configureRoutes(app);

        // Start housekeeping thread
        Thread housekeeper = new Elf();
        housekeeper.start();

    }

    public static void configureRoutes(Javalin app) {
        // Configure GET routes
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

        // Configure post routes
        app.post("/Participant", ctx -> {
            HashMap<String, String> data = new HashMap<>();
            data.put("EventID", ctx.formParam("EventID"));
            data.put("SenderName", ctx.formParam("SenderName"));
            data.put("RecieverName", ctx.formParam("RecieverName"));
            data.put("GiftDescription", ctx.formParam("GiftDescription"));

            if (ValidateEventID(data.get("EventID"))) {
                DBManager.InsertGift(data);

                ctx.html("Done!");
            } else {
                ctx.html("Event Not Found!");
            }

        });
        app.post("/login", ctx -> {
            String EventId = ctx.formParam("EventId");
            String hashstr = Helper.getPwHash(ctx);
            if (Authenticate(EventId, hashstr)) {
                ctx.status(HttpStatus.OK);
                System.out.println(ctx.formParam("tokenise"));
                String tkn = Helper.generateNewToken();
                InsertToken(EventId, tkn);
                ctx.cookie("Auth", tkn);
                ctx.redirect("/Dashboard?" + EventId);
            } else {
                ctx.status(HttpStatus.FORBIDDEN);
            }
        });
        app.post("/register", ctx -> {
            try {
                final SecureRandom secureRandom = new SecureRandom();
                HashMap<String, String> data = new HashMap<>();
                data.put("EventName", ctx.formParam("EventName"));
                data.put("EventDescription", ctx.formParam("EventDescription"));
                data.put("CreationDate", Instant.now().toString());
                String id = String.format("%04d", secureRandom.nextInt(9999));
                Instant startTime = Instant.now();
                while (ValidateEventID(id)) {
                    id = String.format("%04d", secureRandom.nextInt(9999));
                    if (ChronoUnit.MINUTES.between(startTime, Instant.now()) >= 2) {
                        throw new TimeoutException();
                    }
                }
                data.put("EventID", id);
                DBManager.InsertEvent(data);

                String hashstr = Helper.getPwHash(ctx);

                DBManager.InsertAuth(id, hashstr);

                String tkn = Helper.generateNewToken();
                InsertToken(id, tkn);
                ctx.cookie("Auth", tkn);
                ctx.redirect("/Dashboard?" + id);
            } catch (TimeoutException e) {
                ctx.status(HttpStatus.LOOP_DETECTED);

            }

        });

        // Configure delete routes
        app.delete("/report", ctx -> {
            System.out.println("delete" + ctx.header("EventId"));
            System.out.println("auth: " + ctx.cookie("Auth"));
            String eventId = DBManager.AuthVerify(ctx.cookie("Auth"));
            if (eventId != null && eventId.equals(ctx.header("EventId"))) {
                DBManager.deleteGift(ctx.header("GiftId"));
                ctx.status(HttpStatus.OK);
            } else {
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
