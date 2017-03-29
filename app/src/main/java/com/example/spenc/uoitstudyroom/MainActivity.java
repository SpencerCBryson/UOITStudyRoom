package com.example.spenc.uoitstudyroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView bookingList;
    BookingAdapter bookingAdapter;
    Spinner dateSpinner;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Booking> display;
    final HashMap<String,ArrayList<Booking>> bookingMap = new HashMap<>();
    HashMap<String, String> dateToId = new HashMap<>();
    ArrayList<Booking> bList;
    private String[] formData;
    String selectedDateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookingList = (ListView) findViewById(R.id.bookingList);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);

        AsyncTask task = new ScrapeBookingsTask(this).execute();

        Button button = (Button) findViewById(R.id.loadBookings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = (String) dateSpinner.getSelectedItem();
                selectedDateId = dateToId.get(date);
                display.clear();
                ArrayList<Booking> bookings = bookingMap.get(selectedDateId);
                for (Booking booking : bookings)
                    display.add(booking);
                bookingAdapter.notifyDataSetChanged();
            }
        });

        // TODO: IMPLEMENT FILTER BUTTONS HERE

        bookingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking selected = display.get(position);
                System.out.println(selectedDateId);

                // TODO: IMPLEMENT JOIN ONLY AND PARTIAL BOOKING ACTIVITIES

                switch (selected.getBookingState()) {
                    case 0:
                        Intent joinIntent =
                                new Intent(view.getContext(), CreateBookingActivity.class);
                        joinIntent.putExtra("booking", selected);
                        joinIntent.putExtra("date", selectedDateId);
                        joinIntent.putExtra("formData", formData);
                        startActivity(joinIntent);
                        break;
                    case 1:
                        Intent partialIntent =
                                new Intent(view.getContext(), CreateBookingActivity.class);
                        partialIntent.putExtra("booking", selected);
                        startActivity(partialIntent);
                        break;
                    case 2:
                        Intent joinOnlyIntent =
                                new Intent(view.getContext(), CreateBookingActivity.class);
                        joinOnlyIntent.putExtra("booking", selected);
                        startActivity(joinOnlyIntent);
                        break;
                }

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
        private ArrayList<String> dates;
        public ArrayList<String> dateStrings;


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

            arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, dateStrings);
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


            return null;
        }

        public class ResponseReceiver extends BroadcastReceiver {
            public static final String ACTION_RESP =
                    "SCRAPE_DONE";

            ResponseReceiver() {}

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                dates = extras.getStringArrayList("dates");
                dateStrings = extras.getStringArrayList("dateStrings");

                for(String date : dates) {
                    bookingMap.put(date, (ArrayList) extras.getParcelableArrayList(date));
                }

                for (int i = 0; i < dates.size(); i++)
                    dateToId.put(dateStrings.get(i), dates.get(i));

                formData = intent.getExtras().getStringArray("formData");
                done = true;
            }
        }
    }
}
