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

<<<<<<< HEAD
@Deprecated
class ScrapeBookingsTask extends AsyncTask<Void, Void, ArrayList<Booking>> {
=======
class ScrapeBookingsTask extends AsyncTask<Void, Void, Void> {
>>>>>>> fe7240e5aeb3a7b8906f587400ecfb814bd67fc2

    private ProgressDialog dialog;
    private HashMap<String,ArrayList<Booking>> bookingMap = new HashMap<>();
    private Context context;
    boolean done = false;
    private String[] formData;
    private String[] dates;

    ScrapeBookingsTask(MainActivity activity) {
        this.dialog = new ProgressDialog(activity);
<<<<<<< HEAD
=======
        context = activity;
>>>>>>> fe7240e5aeb3a7b8906f587400ecfb814bd67fc2
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading bookings... please wait.");
        dialog.show();
<<<<<<< HEAD
    }

    @Override
    protected void onPostExecute(ArrayList<Booking> result)
    {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    @Override
    protected ArrayList<Booking> doInBackground(Void... param) {
        DataScraper dataScraper = new DataScraper();
        String[] formData = new String[4];
=======

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
>>>>>>> fe7240e5aeb3a7b8906f587400ecfb814bd67fc2

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
<<<<<<< HEAD
<<<<<<< HEAD

//        for (Booking b : bookingList)
//                System.out.println(b.getRoom() + " " + b.getDate());


        dataScraper.postDate(6289,formData);
        cbuf = dataScraper.selectBooking("LIB305","8:30%20PM");

        //TODO: Scrape updated eventvalidation and viewstate for posting a certain booking

        //TODO: Post booking with retrieved data

=======


//        //TODO: Scrape updated eventvalidation and viewstate for posting a certain booking
        cbuf = dataScraper.selectBooking("LIB304","6:00 PM",0);
//
//        //TODO: Post booking with retrieved data

        parser = new Parser(cbuf);

        HashMap<String,String> postData = new HashMap<>();

        postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
        postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
        postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));

        //TODO: GET VALUES FROM UI INSTEAD OF HARDCODING

        postData.put("btnreserve","Create group");
        postData.put("duration","0.5");
        postData.put("institution","uoit");
        postData.put("groupcode","spam");
        postData.put("groupname","spam");
        postData.put("notes","test"); //OPTIONAL, however it still need to send it as empty
        postData.put("password","nothankyou");
        postData.put("studentid","999999999");

        //incorrect student id and password error is expected
        cbuf = dataScraper.postBooking(postData);

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

<<<<<<< HEAD
<<<<<<< HEAD
        //TODO: Post booking with retrieved data
        dataScraper.postBooking(formData);
>>>>>>> 0f5626b5afa2531503ec353f1d3cd91b2981c2c4
=======
>>>>>>> 0429e7bd17b65315746dde725fc05e4d6a8650ac
=======
        //TODO: Join existing booking...
        // cbuf = dataScraper.selectBooking(room, time, 2);
        // scrape cbuf for form data...
        // cbuf = dataScraper.existingBooking(postData);
>>>>>>> 6512631b8dd169b6a37390fd41e49f005630feca

        long totalTime = System.currentTimeMillis() - startTime;

        System.out.println(new String(cbuf));
        System.out.println("Scraped bookings in " + totalTime + " ms.");

        return bookingList;

    }

    Booking parseBookingData(String data) {
        int bookingState = 2;
        if (data.contains("next=book"))
            bookingState = 0;
        else if (data.contains("viewleaveorjoin"))
            bookingState = 1;

        String reg = "\\d{1,2}:..\\s[A|P]M";
        String reg_ = "LIB[a-zA-Z0-9]*";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(data);
        Pattern p_ = Pattern.compile(reg_);
        Matcher m_ = p_.matcher(data);

        m.find();
        m_.find();

        return new Booking(m.group(), m_.group(), bookingState);
    }
}
=======
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
>>>>>>> fe7240e5aeb3a7b8906f587400ecfb814bd67fc2
