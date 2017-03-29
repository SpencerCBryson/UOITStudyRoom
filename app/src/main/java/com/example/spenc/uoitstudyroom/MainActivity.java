package com.example.spenc.uoitstudyroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView bookingList;
    BookingAdapter bookingAdapter;
    Spinner dateSpinner;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Booking> display;
    final HashMap<String,ArrayList<Booking>> bookingMap = new HashMap<>();
    ArrayList<Booking> bList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookingList = (ListView) findViewById(R.id.bookingList);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);

        //TODO: FINISH THE TASK !!!
        AsyncTask task = new ScrapeBookingsTask(this).execute();

        Button button = (Button) findViewById(R.id.loadBookings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = (String) dateSpinner.getSelectedItem();
                display.clear();
                ArrayList<Booking> bookings = bookingMap.get(date);
                for (Booking booking : bookings)
                    display.add(booking);
                bookingAdapter.notifyDataSetChanged();
            }
        });

        //TODO: Verify postData is VALID *BEFORE* sending it to the task!!!

//        AsyncTask task = new CreateBookingTask(this).execute(postData);

        /* END OF DRIVER CODE FOR POSTING A NEW BOOKING */
    }

    class ScrapeBookingsTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Activity context;
        boolean done = false;
        private String[] formData;
        private ArrayList<String> dates;


        ScrapeBookingsTask(MainActivity activity) {
            this.dialog = new ProgressDialog(activity);
            context = activity;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading bookings... please wait.");
            dialog.show();

            IntentFilter intentFilter = new IntentFilter(BookingIntentService.SCRAPE_DONE);
            ResponseReceiver rr = new ResponseReceiver();
            LocalBroadcastManager.getInstance(context).registerReceiver(
                    rr, intentFilter
            );

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

            arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            System.out.println(dates.get(0) + bookingMap.get(dates.get(0)).size());
            display = new ArrayList<>(bookingMap.get(dates.get(0)));

            dateSpinner.setAdapter(arrayAdapter);
            bookingAdapter = new BookingAdapter(context, display);
            bookingList.setAdapter(bookingAdapter);
            bookingAdapter.notifyDataSetChanged();


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

            ResponseReceiver() {}

            @Override
            public void onReceive(Context context, Intent intent) {
                dates = intent.getExtras().getStringArrayList("dates");
                Bundle extras = intent.getExtras();

                for(String date : dates) {
                    bookingMap.put(date, (ArrayList) extras.getParcelableArrayList(date));
                }
                formData = intent.getExtras().getStringArray("formData");
                done = true;
            }
        }
    }
}
