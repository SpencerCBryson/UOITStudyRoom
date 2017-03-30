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
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dialog = new ProgressDialog(this);

        IntentFilter intentFilter = new IntentFilter(BookingIntentService.SCRAPE_DONE);
        Intent scrape = new Intent(this, BookingIntentService.class);
        this.startService(scrape);
        ResponseReceiver rr = new ResponseReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                rr, intentFilter
        );

        dialog.setMessage("Loading bookings... please wait.");
        dialog.show();

        startActivity(new Intent(this, LoginActivity.class));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bookingList = (ListView) findViewById(R.id.bookingList);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);

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

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "SCRAPE_DONE";

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Received intent");

            dialog.hide();
        }
    }
}
