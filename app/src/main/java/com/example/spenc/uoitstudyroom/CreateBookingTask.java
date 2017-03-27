package com.example.spenc.uoitstudyroom;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateBookingTask extends AsyncTask<HashMap<String,String>,String,Void> {

    private ProgressDialog dialog;
    private ArrayList<Booking> bList;
    private Context context;
    private boolean done = false, err = false;
    private String[] formData;
    private String date;

    CreateBookingTask(Context context) {
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading bookings... please wait.");
        dialog.show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BookingIntentService.SCRAPE_DONE);
        ResponseReceiver rr = new ResponseReceiver();
        context.registerReceiver(rr, intentFilter);

        Intent i = new Intent(context, BookingIntentService.class);
        context.startService(i);
    }

    @Override
    protected Void doInBackground(HashMap<String, String>... params) {
        while(!done) { //blocking code
            //waiting for response from datascraping intent
        }

        HashMap<String,String> postData = params[0];
        DataScraper ds = new DataScraper();

        date = postData.get("date");
        ds.postDate(Integer.parseInt(date), formData);
        char[] cbuf = ds.selectBooking(postData.get("room"),postData.get("time"),0);

        Parser parser = new Parser(cbuf);

        postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
        postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
        postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));

        cbuf = ds.postBooking(postData);
        parser = new Parser(cbuf);

        //Ensuring that user entered content is valid will prevent this error from occuring
        if(parser.select("span", "id", "ContentPlaceHolder1_LabelError") != null) {
            err = true;
        }

        if(new String(cbuf).contains("HTTP/1.1 500")) {
            System.out.println("BAD REQUEST");
            err = true;
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        String successString = "Partial booking successfully created";
        String failedString = "An error has occured. Check your booking and please try again.";

        if(err)
            Toast.makeText(context, failedString , Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, successString , Toast.LENGTH_LONG).show();

        //TODO: send user back to main activity from the posting activity
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "SCRAPE_DONE";

        @Override
        public void onReceive(Context context, Intent intent) {
            bList = intent.getExtras().getParcelableArrayList(date);
            formData = intent.getExtras().getStringArray("formData");
            dialog.setMessage("Posting booking... please wait.");
            done = true;
        }
    }
}
