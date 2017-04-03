package com.example.spenc.uoitstudyroom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/23/2017.
 */

public class BookingRoom implements Parcelable {
    String room;

    public ArrayList<Booking> getBookings() {
        ArrayList<Booking> openBookings = new ArrayList<>();
        for(Booking booking : bookings)
            if(booking.getBookingState() != 2)
                openBookings.add(booking);

        return openBookings;
    }

    ArrayList<Booking> bookings = new ArrayList<>();

    public BookingRoom(Parcel in) {
        this.room = in.readString();
        this.bookings = in.readArrayList(this.getClass().getClassLoader());
    }

    public BookingRoom(String room) {
        this.room = room;
    }

    void add(Booking booking) { bookings.add(booking); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(room);
        dest.writeList(bookings);
    }

    public static final Creator<BookingRoom> CREATOR = new Creator<BookingRoom>() {
        @Override
        public BookingRoom createFromParcel(Parcel in) {
            return new BookingRoom(in);
        }

        @Override
        public BookingRoom[] newArray(int size) {
            return new BookingRoom[size];
        }
    };
}
