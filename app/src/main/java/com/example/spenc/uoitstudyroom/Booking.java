package com.example.spenc.uoitstudyroom;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/19/2017.
 */

public class Booking {
    String date;
    String room;
    String link;
    int bookingState;

    Booking(String date, String room, int bookingState) {
        this.date = date;
        this.room = room;
        this.bookingState = bookingState;
    }

    public String getDate() { return date; }

    public String getRoom() {
        return room;
    }

    public String getLink() {
        return link;
    }

    public int getBookingState() {return bookingState; }
}
