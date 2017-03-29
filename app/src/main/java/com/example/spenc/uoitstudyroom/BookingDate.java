package com.example.spenc.uoitstudyroom;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/23/2017.
 */

public class BookingDate {
    int id;
    String date;
    String viewState;
    String eventValidation;
    HashMap<String, BookingRoom> bookingRooms = new HashMap<>();

    BookingDate(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public BookingDate(int id, String date,
                       String viewState, String eventValidation,
                       HashMap<String, BookingRoom> bookingRooms) {
        this.date = date;
        this.viewState = viewState;
        this.eventValidation = eventValidation;
        this.id = id;
        this.bookingRooms = bookingRooms;
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }

    public void setEventValidation(String eventValidation) {
        this.eventValidation = eventValidation;
    }

    public void createBookingRooms(ArrayList<Booking> bookings) {
        for (Booking booking : bookings) {

         //   bookingRooms.put(booking.getRoom(), );
        }
    }
}
