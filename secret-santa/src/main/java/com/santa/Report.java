package com.santa;

import java.util.ArrayList;
import java.util.HashMap;

import static com.santa.DBManager.GiftsFromEvent;
import static com.santa.DBManager.readGift;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Report implements Handler{
    
    @Override
    public void handle(Context context) throws Exception {
        String id = context.queryString();

        String tkn = DBManager.AuthVerify(context.cookie("Auth"));
        if (tkn == null || !tkn.equals(id)) {
            context.redirect("/");
        }

        ArrayList<Integer> gifts = GiftsFromEvent(id);
        String html = "";
        html += String.format("""
        <!DOCTYPE html>
        <head>
            <title>Secret Santa | Report</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <script src="script.js"></script> 
        </head>
        <body>
            <div class = 'container'>
                <div class = 'sidenav'> 
                    <a href = "/logout"><div class = 'sidenav-item'> <h2> Log Out </h2> </div> </a>
                    <a href = "/Dashboard?%s"><div class = 'sidenav-item'> <h2> Summary </h2> </div> </a>
                    <a><div class = 'sidenav-item'> <h2> Report </h2> </div> </a>
                    <a><div class = 'sidenav-item'> <h2> Present </h2> </div> </a>
                </div>
                <div class = 'header'> 
                    <center>
                    <h1> Gift Report </h1>
                    </center>
                </div>
                <div class = 'content'>
        """, id);
        if (gifts.size() > 0) {
        html += """
                        <table id = "report">
                            <tr>
                                <th> Sender </th>
                                <th> Recipient </th>
                                <th> Description </th>
                            </tr>

                            """;
        for (int i = 0; i < gifts.size(); i++) {
            HashMap<String, String> giftInfo = readGift(Integer.toString(gifts.get(i)));
            html += String.format("""
                    <tr>
                        <td> 
                        <button onclick="displayText(%s)" id="%sa" style="display: inline-block">Reveal</button>
                        <button onclick="hideText(%s)" id="%sb" style="display: none">Hide</button>
                            <div id="%s" style="display: none;">
                            %s
                            </div> 
                        </td>
                        <td> %s </td>
                        <td> %s </td>
                    </tr>
                    """, giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("SenderName"), giftInfo.get("RecieverName"), giftInfo.get("GiftDescription"));
        }
        html += """
                    </table>
                    """;
                            
    }
    else {
        html += """
                <h2> No Gifts Yet! </h3> <br>
                <h3> Spread the word and have participants register their gifts </h3>
                """;
    }
    html += """
                </div>
            </div>
        </body>
    </html>
    """;

    context.html(html);
    }
}
