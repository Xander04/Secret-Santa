package com.santa;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class Helper {
    public static boolean Authenticate(Context context) {
        String id = context.queryString();
        HttpStatus response;
        String tkn = DBManager.AuthVerifyEventToken(context.cookie("Auth"));
        boolean authorised = false;
        if (tkn == null) {
            response = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
        }
        else if (tkn.equals(id)){
            response = HttpStatus.OK;
            authorised = true;
        }
        else if (!tkn.equals(id)){
            response = HttpStatus.FORBIDDEN;
        }
        else {
            response = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        context.status(response);
        return authorised;
    }


    public static String generateNewToken() {
        final SecureRandom secureRandom = new SecureRandom();
        final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    
    public static String getPwHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String hashstr = "";
        if (password != null) {
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%X", b));
            }
            hashstr = sb.toString();
        }
            
        return hashstr;
    }
}
