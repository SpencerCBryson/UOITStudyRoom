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
import android.widget.Toast;

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
    RVAdapter rvAdapter;

    HashMap<String, HashMap<String, BookingRoom>> bookingRooms
            = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dialog = new ProgressDialog(this);
        this.setTitle("Select a room");

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
        rvAdapter = new RVAdapter(rooms,this);
        rv.setAdapter(rvAdapter);

        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
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
            formData = extras.getStringArray("formData");

            for (int i = 0; i < dateIds.size(); i++) {
                bookingRooms.put(dateIds.get(i),
                        (HashMap) extras.get(dateIds.get(i)));
                dateToId.put(dateStrings.get(i), dateIds.get(i));
            }

            arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, dateStrings);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dateSpinner.setAdapter(arrayAdapter);
            dateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String date = (String) dateSpinner.getSelectedItem();
                    selectedDateId = dateToId.get(date);
                    System.out.println("Selected " + selectedDateId);

                    buildRooms(date);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dialog.hide();
        }
    }

    private void buildRooms(String date) {
        rooms.clear();

        rooms.add(new Room("LIB202A",
                bookingRooms.get(selectedDateId).get("LIB202A").getBookings().size(), R.drawable.lib202a));
        rooms.add(new Room("LIB202B",
                bookingRooms.get(selectedDateId).get("LIB202B").getBookings().size(), R.drawable.lib202b));
        rooms.add(new Room("LIB202C",
                bookingRooms.get(selectedDateId).get("LIB202C").getBookings().size(), R.drawable.lib202c));
        rooms.add(new Room("LIB303",
                bookingRooms.get(selectedDateId).get("LIB303").getBookings().size(), R.drawable.lib303));
        rooms.add(new Room("LIB304",
                bookingRooms.get(selectedDateId).get("LIB304").getBookings().size(), R.drawable.lib304));
        rooms.add(new Room("LIB305",
                bookingRooms.get(selectedDateId).get("LIB305").getBookings().size(), R.drawable.lib305));
        rooms.add(new Room("LIB306",
                bookingRooms.get(selectedDateId).get("LIB306").getBookings().size(), R.drawable.lib306));
        rooms.add(new Room("LIB307",
                bookingRooms.get(selectedDateId).get("LIB307").getBookings().size(), R.drawable.lib307));
        rooms.add(new Room("LIB309",
                bookingRooms.get(selectedDateId).get("LIB309").getBookings().size(), R.drawable.lib309));
        rooms.add(new Room("LIB310",
                bookingRooms.get(selectedDateId).get("LIB310").getBookings().size(), R.drawable.lib310));

        rvAdapter.notifyDataSetChanged();
    }

    public void onClickCalled(String roomName) {

        if(bookingRooms.get(selectedDateId).get(roomName).getBookings().size() != 0) {
            Intent intent = new Intent(getBaseContext(), RoomActivity.class);

            String dateId = dateToId.get((String) dateSpinner.getSelectedItem());
            intent.putExtra("bookingData", bookingRooms.get(dateId).get(roomName));
            intent.putExtra("formData", formData);
            intent.putExtra("dateId", selectedDateId);

            startActivity(intent);
        } else {
            Toast.makeText(this, "No available bookings" , Toast.LENGTH_LONG).show();
        }
    }
}
