package com.example.michael.userinterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 21/03/17.
 */

public class AddBooking extends MainActivity{
    public List<Booking> bookingList;
    public AddBooking(){
        bookingList = new ArrayList<>();
        //Sample Test Data until merged with real data
        bookingList.add(new Booking(1, "Today" , "Room 1", "4", "Availiable", "3pm", "Link"));
        bookingList.add(new Booking(2, "Today" , "Room 2", "4", "Availiable", "2pm", "Link"));
        bookingList.add(new Booking(3, "Today" , "Room 3", "4", "Incomplete", "5pm", "Link"));
        bookingList.add(new Booking(4, "Today" , "Room 4", "4", "Availiable", "3pm", "Link"));
        bookingList.add(new Booking(5, "Today" , "Room 5", "4", "Booked", "4pm", "Link"));
        bookingList.add(new Booking(6, "Today" , "Room 6", "4", "Availiable", "9pm", "Link"));
    }



}
