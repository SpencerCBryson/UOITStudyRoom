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
        AddBooking addBooking = new AddBooking();

        bookingParser = new BookingList(getApplicationContext(), addBooking.bookingList);
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
