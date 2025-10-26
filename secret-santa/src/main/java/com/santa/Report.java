package com.santa;

import java.util.ArrayList;
import java.util.HashMap;

import static com.santa.DBManager.getUsersFromEvent;
import static com.santa.DBManager.getEventSummary;
import static com.santa.DBManager.readUser;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Report implements Handler{
    
    @Override
    public void handle(Context context) throws Exception {
        String id = context.queryString();

        if (Helper.Authenticate(context)) {

            ArrayList<Integer> gifts = getUsersFromEvent(id);
            HashMap<String, String> eventSummary = getEventSummary(id);
            String html = "";
            html += String.format("""
           <!DOCTYPE html>
        <head>
            <title>Secret Santa | Login</title>
            <link rel='stylesheet' type='text/css' href='style.css' />
            <link rel="icon" type="image/x-icon" href="logo.png">
            <script type="text/javascript" src="script.js"></script>
            <script type="text/javascript" src="index.js"></script>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
            <div class="head1">
            <a href="/"><img src="logo.png" class="logo" width=75px></a>
            <div class="titleHolder">
            <h1 class="pageTitle">%s - %s</h1>
            </div>
            <div class="headSpace"> </div>
                <div class="dropdown">
                    <button onclick="dropFunction()" class="dropDown">Manage &#x25BC;</button>
                    <div id="eventDrop" class="dropdown-content dropdown-content-el">
                        <a href="/logout">Logout</a>
                        <div class="dropdown-line"> </div>
                    </div>
                    <div id="eventDrop1" class="dropdown-content-1 dropdown-content-el">
                        <a href="/login">Summary</a>
                        <div class="dropdown-line"> </div>
                    </div>
                    <div id="eventDrop2" class="dropdown-content-2 dropdown-content-el">
                        <a href="/report?%s">Report</a>
                        <div class="dropdown-line"> </div>
                    </div>
                    <div id="eventDrop3" class="dropdown-content-3 dropdown-content-el">
                        <a href="/presentation?%s">Present</a>
                    </div>
                </div>
            </div>
            <div class="head_line">
            </div>
        </head>
        <body>

            <aside class="index" id = "Log in">
            <div class="formstyle report">
            <form class="report">
                        <center><h1>Gift Count: %s</h1><br></center>
                        <div class="loginBreak2"> </div>
                            <center>
                            <h1> Gift Report </h1>
                            </center>
                            
                """, eventSummary.get("EventName"), id, id, id, eventSummary.get("GiftCount"));
                if (!gifts.isEmpty()) {
                html += """
                            <div class="report-table">
                                <table id = "report">
                                    <tr class="head">
                                        <th> Sender </th>
                                        <th> Recipient </th>
                                        <th> Description </th>
                                        <th></th>
                                    </tr>

                                    """;
                for (int i = 0; i < gifts.size(); i++) {
                    HashMap<String, String> giftInfo = readUser(Integer.toString(gifts.get(i)));
                    html += String.format("""
                            <tr id="%s">
                                <td class="ShowHide">
                                    <button type="button" onclick="displayText(%s)" id="%sa" style="display: inline-block">&#x1F441;</button>
                                    <button type="button" onclick="hideText(%s)" id="%sb" style="display: none"><s>&#x1F441;</s> %s</button>
                                </td>
                                <td> %s </td>
                                <td> %s </td>
                                <td class="deleteButton"> <button type="button"  class="deleteButton" onclick="deleteEvent(%s, %s)">&#128465 </button> </td>
                            </tr>
                            """, giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("GiftID"), giftInfo.get("SenderName"), giftInfo.get("RecieverName"), giftInfo.get("GiftDescription"), giftInfo.get("EventID"), giftInfo.get("GiftID"));
                }
                html += """
                            </table>
                            </div>
                            </form>
                            </div>
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
            </aside>

        

            <div class="foot1">
            &copy; Xander Dundon 2025
            </div>
        </body>
    </html>
            """;

            context.html(html);
        }
    }
}
