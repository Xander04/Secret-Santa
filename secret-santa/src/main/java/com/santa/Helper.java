package com.santa;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class Helper {
    public static void Authenticate(Context context) {
        String id = context.queryString();
        HttpStatus response;
        String tkn = DBManager.AuthVerify(context.cookie("Auth"));
        if (tkn == null) {
            response = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
        }
        else if (tkn.equals(id)){
            response = HttpStatus.OK;
        }
        else if (!tkn.equals(id)){
            response = HttpStatus.FORBIDDEN;
        }
        else {
            response = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        context.status(response);
    }
}
