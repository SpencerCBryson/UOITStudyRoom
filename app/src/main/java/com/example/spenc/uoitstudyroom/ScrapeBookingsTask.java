package com.example.spenc.uoitstudyroom;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scrapes raw HTML data from website, needing to be parsed
 */

class ScrapeBookingsTask extends AsyncTask<Void, Void, Void> {

    private ProgressDialog dialog;
    private HashMap<String,ArrayList<Booking>> bookingMap = new HashMap<>();
    private Context context;
    boolean done = false;
    private String[] formData;
    private String[] dates;

    ScrapeBookingsTask(MainActivity activity) {
        this.dialog = new ProgressDialog(activity);
        context = activity;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading bookings... please wait.");
        dialog.show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BookingIntentService.SCRAPE_DONE);
        ScrapeBookingsTask.ResponseReceiver rr = new ScrapeBookingsTask.ResponseReceiver();
        context.registerReceiver(rr, intentFilter);

        Intent i = new Intent(context, BookingIntentService.class);
        context.startService(i);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        //TODO: UPDATE THE UI IN HERE VIA THE REFERENCE TO `context`


    }

    @Override
    protected Void doInBackground(Void... param) {

        while(!done) { //blocking code
            //waiting for response from datascraping intent
        }

        //TODO: YOUR DATA IS IN `bookingMap` and `dates`!!!!!!!!!!!!!


        return null;
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "SCRAPE_DONE";

        @Override
        public void onReceive(Context context, Intent intent) {
            dates = intent.getExtras().getStringArray("dates");
            for(String date : dates) {
                ArrayList<Booking> bList = intent.getExtras().getParcelableArrayList(date);
                bookingMap.put(date,bList);
            }
            formData = intent.getExtras().getStringArray("formData");
            done = true;
        }
    }
}

//        //TODO: Setup partial booking joining with non-hardcoded values
//        postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
//        postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
//        postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));
//        postData.put("btn","Create or Join a Group");
//        postData.put("radio","swag");
//
//        String groupname = postData.get("radio");
//
//        String[] bookingData = new String[2];
//        bookingData[0] = "LIB304";
//        bookingData[1] = "8:30 PM";
//
//        cbuf = dataScraper.selectPartialBooking(postData,bookingData);

//        parser = new Parser(cbuf);
//        postData = new HashMap<String,String>();
//        postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
//        postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
//        postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));
//        postData.put("password","nothankyou");
//        postData.put("studentid","999999999");
//        postData.put("btn","Join " + groupname);

//Incorrect student id/password error expected
//        cbuf = dataScraper.joinPartialBooking(postData);

//TODO: Join existing booking...
// cbuf = dataScraper.selectBooking(room, time, 2);
// scrape cbuf for form data...
// cbuf = dataScraper.existingBooking(postData);

//        long totalTime = System.currentTimeMillis() - startTime;

//        System.out.println(new String(cbuf));
//System.out.println("Scraped bookings in " + totalTime + " ms.");