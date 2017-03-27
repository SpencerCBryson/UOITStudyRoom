package com.example.spenc.uoitstudyroom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/19/2017.
 */

public class Booking implements Parcelable {
    private String time;
    private String room;
    private String link;
    private int bookingState;

    Booking(String time, String room, int bookingState) {
        this.time = time;
        this.room = room;
        this.bookingState = bookingState;
    }


    protected Booking(Parcel in) {
        time = in.readString();
        room = in.readString();
        link = in.readString();
        bookingState = in.readInt();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public String getTime() { return time; }

    public String getRoom() {
        return room;
    }

    public String getLink() {
        return link;
    }

    public int getBookingState() {return bookingState; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(room);
        dest.writeString(link);
        dest.writeInt(bookingState);
    }
}
