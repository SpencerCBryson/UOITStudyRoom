package com.example.spenc.uoitstudyroom;

import java.util.ArrayList;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/23/2017.
 */

public class BookingRoom {
    String room;
    ArrayList<Booking> bookings = new ArrayList<>();

    public BookingRoom(String room) {
        this.room = room;
    }


}
