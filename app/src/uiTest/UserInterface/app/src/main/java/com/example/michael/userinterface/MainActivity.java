package com.example.michael.userinterface;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public ListView listVBooking;
    public BookingList bookingParser;
    public List<Booking> bookingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listVBooking = (ListView)findViewById(R.id.listview_booking);

        bookingList = new ArrayList<>();
        //Sample Test Data until merged with real data
        bookingList.add(new Booking(1, "Today" , "Room 1", "4", "Availiable", "3pm", "Link"));
        bookingList.add(new Booking(2, "Today" , "Room 2", "4", "Availiable", "2pm", "Link"));
        bookingList.add(new Booking(3, "Today" , "Room 3", "4", "Incomplete", "5pm", "Link"));
        bookingList.add(new Booking(4, "Today" , "Room 4", "4", "Availiable", "3pm", "Link"));
        bookingList.add(new Booking(5, "Today" , "Room 5", "4", "Booked", "4pm", "Link"));
        bookingList.add(new Booking(6, "Today" , "Room 6", "4", "Availiable", "9pm", "Link"));

        bookingParser = new BookingList(getApplicationContext(), bookingList);
        listVBooking.setAdapter(bookingParser);

        listVBooking.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                Toast.makeText(getApplicationContext(), "Clicked " + view.getTag(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
