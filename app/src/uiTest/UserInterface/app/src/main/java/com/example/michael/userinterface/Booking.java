package com.example.michael.userinterface;

/**
 * Created by michael on 20/03/17.
 */
/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/19/2017.
 */

public class Booking {
    private int id;
    private String date;
    private String room;
    private String capacity;
    private String avail;
    private String time;
    private String link;

    public Booking(int id, String date, String room, String capacity, String avail, String time, String link) {
        this.id = id;
        this.date = date;
        this.room = "Room: " + room;
        this.capacity = "Capacity: " + capacity;
        this.avail = avail;
        this.time = time;
        this.link = link;
    }

    //Getters for BookingList to set View
    public int getId(){
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getRoom() {
        return room;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getAvail() {
        return avail;
    }

    public String getTime() {
        return time;
    }

    public String getLink() {
        return link;
    }
}