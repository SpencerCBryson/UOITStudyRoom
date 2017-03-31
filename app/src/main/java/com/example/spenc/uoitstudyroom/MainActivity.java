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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    ArrayList<Room> rooms = new ArrayList<>();
    RecyclerView rv;
    RVAdapter adapter;

    final HashMap<String, HashMap<String, BookingRoom>> bookingRooms
            = new HashMap<>();

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

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        adapter = new RVAdapter(rooms);
        rv.setAdapter(adapter);

        //bookingList = (ListView) findViewById(R.id.bookingList);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);

        //Button button = (Button) findViewById(R.id.loadBookings);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String date = (String) dateSpinner.getSelectedItem();
//                selectedDateId = dateToId.get(date);
//                display.clear();
//                ArrayList<Booking> bookings = bookingMap.get(selectedDateId);
//                for (Booking booking : bookings)
//                    display.add(booking);
//                bookingAdapter.notifyDataSetChanged();
//            }
//        });

        // TODO: IMPLEMENT FILTER BUTTONS HERE



//        bookingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Booking selected = display.get(position);
//                System.out.println(selectedDateId);
//
//                // TODO: IMPLEMENT JOIN ONLY AND PARTIAL BOOKING ACTIVITIES
//
//                switch (selected.getBookingState()) {
//                    case 0:
//                        Intent joinIntent =
//                                new Intent(view.getContext(), CreateBookingActivity.class);
//                        joinIntent.putExtra("booking", selected);
//                        joinIntent.putExtra("date", selectedDateId);
//                        joinIntent.putExtra("formData", formData);
//                        startActivity(joinIntent);
//                        break;
//                    case 1:
//                        Intent partialIntent =
//                                new Intent(view.getContext(), CreateBookingActivity.class);
//                        partialIntent.putExtra("booking", selected);
//                        startActivity(partialIntent);
//                        break;
//                    case 2:
//                        Intent joinOnlyIntent =
//                                new Intent(view.getContext(), CreateBookingActivity.class);
//                        joinOnlyIntent.putExtra("booking", selected);
//                        startActivity(joinOnlyIntent);
//                        break;
//                }
//
//            }
//        });

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
            Bundle extras = intent.getExtras();

            ArrayList<String> dateIds = extras.getStringArrayList("dates");
            ArrayList<String> dateStrings = extras.getStringArrayList("dateStrings");

            for (int i = 0; i < dateIds.size(); i++) {
                bookingRooms.put(dateIds.get(i),
                        (HashMap) extras.get(dateIds.get(i)));
                //dateToId.put(dateIds.get(i), dateStrings.get(i));
                dateToId.put(dateStrings.get(i), dateIds.get(i));
            }

            arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, dateStrings);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            dateSpinner.setAdapter(arrayAdapter);

            int dateId = 0;
            rooms.add(new Room("LIB202A",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB202A").getBookings().size(), R.drawable.lib202a));
            rooms.add(new Room("LIB202B",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB202B").getBookings().size(), R.drawable.lib202b));
            rooms.add(new Room("LIB202C",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB202C").getBookings().size(), R.drawable.lib202c));
            rooms.add(new Room("LIB303",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB303").getBookings().size(), R.drawable.lib303));
            rooms.add(new Room("LIB304",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB304").getBookings().size(), R.drawable.lib304));
            rooms.add(new Room("LIB305",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB305").getBookings().size(), R.drawable.lib305));
            rooms.add(new Room("LIB306",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB306").getBookings().size(), R.drawable.lib306));
            rooms.add(new Room("LIB307",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB307").getBookings().size(), R.drawable.lib307));
            rooms.add(new Room("LIB309",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB309").getBookings().size(), R.drawable.lib309));
            rooms.add(new Room("LIB310",
                    bookingRooms.get(dateIds.get(dateId)).get("LIB310").getBookings().size(), R.drawable.lib310));

            adapter.notifyDataSetChanged();

            dateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Item selected!");
                    String date = (String) dateSpinner.getSelectedItem();
                    for(Room room : rooms) {
                        room.setCapacity(bookingRooms.get(dateToId.get(date)).get(room.getName()).getBookings().size());
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            dialog.hide();
        }
    }
}
