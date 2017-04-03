package com.example.spenc.uoitstudyroom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Spenc on 2017-03-31.
 */

public class RoomActivity extends AppCompatActivity {
    BookingRoom bookingData;
    ArrayList<Booking> display = new ArrayList<Booking>();
    ListView bookingList;
    BookingAdapter bookingAdapter;
    TextView txt_roomReq;
    String room;
    String[] formData;
    String dateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();

        bookingData = intent.getParcelableExtra("bookingData");
        formData = intent.getStringArrayExtra("formData");
        dateId = intent.getStringExtra("dateId");

        txt_roomReq = (TextView) findViewById(R.id.room_req_label);
        room = bookingData.room;

        this.setTitle("Viewing " + bookingData.room);

        if(room.equals("LIB202A")||room.equals("LIB202B")||room.equals("LIB202C"))
            txt_roomReq.setText("Two students needed to book a room");
        else
            txt_roomReq.setText("Three students needed to book a room");

        bookingList = (ListView) findViewById(R.id.bookingList);
        bookingAdapter = new BookingAdapter(this,display);
        bookingList.setAdapter(bookingAdapter);

        for (Booking booking : bookingData.getBookings())
            if(booking.getBookingState() != 2)
                display.add(booking);

        bookingAdapter.notifyDataSetChanged();

        bookingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking selected = display.get(position);

                // TODO: IMPLEMENT JOIN ONLY AND PARTIAL BOOKING ACTIVITIES

                switch (selected.getBookingState()) {
                    case 0:
                        Intent joinIntent =
                                new Intent(view.getContext(), CreateBookingActivity.class);
                        joinIntent.putExtra("booking", selected);
                        joinIntent.putExtra("date", dateId);
                        joinIntent.putExtra("formData", formData);
                        startActivity(joinIntent);
                        break;
                    case 1:
                        Intent partialIntent =
                                new Intent(view.getContext(), CreateBookingActivity.class);
                        partialIntent.putExtra("booking", selected);
                        startActivity(partialIntent);
                        break;
//                    case 2:
//                        Intent joinOnlyIntent =
//                                new Intent(view.getContext(), CreateBookingActivity.class);
//                        joinOnlyIntent.putExtra("booking", selected);
//                        startActivity(joinOnlyIntent);
//                        break;
                }
            }
        });
    }

}
