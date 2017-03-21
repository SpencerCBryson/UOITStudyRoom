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

    Booking(String date, String room) {
        this.date = date;
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public String getRoom() {
        return room;
    }

    public String getLink() {
        return link;
    }
}
