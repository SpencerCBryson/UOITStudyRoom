package com.example.spenc.uoitstudyroom;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/19/2017.
 */

public class Booking implements Parcelable {
    private String time;
    private String room;
    private String link;
    private String group;
    private int bookingState;
    private ArrayList<String> validDurations = new ArrayList<>();
    //private int bookID = new String(room+time).hashCode(); // unique id

    Booking(String time, String room, int bookingState) {
        this.time = time;
        this.room = room;
        this.bookingState = bookingState;
    }


    Booking(Parcel in) {
        time = in.readString();
        room = in.readString();
        link = in.readString();
        bookingState = in.readInt();
        in.readTypedList(validDurations, null);
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

    void assignDuration(ArrayList<Booking> bookings) {
        String[] durations = {"0.5", "1", "1.5", "2"};

        if (this.getTime().equals("11:00 PM"))
            return;

        for (Booking booking : bookings) {
            for (int i = 0; i < 4; i++) {
                if (booking.getTime().equals(incTime(this.getTime()))
                        && booking.getRoom() == this.getRoom())
                    validDurations.add(durations[i]);
            }
        }
    }

    String incTime(String time) {
        int time1h = parseHours(time);
        int time1m = parseMinutes(time);
        String period = parsePeriod(time);

        if (time1m + 30 == 60) {
            time1h += 1;
            time1m = 0;
        } else {
            time1m += 30;
        }
        if (time1h == 12) {
            time1h = 1;
            period = "PM";
        }

        String retTime = time1h + ":";
        if (time1m == 0) retTime += Integer.toString(time1m) + Integer.toString(time1m) + " " + period;
        else retTime += time1m + " " + period;

        return retTime;
    }

    int parseMinutes(String time) {
        int mins;
        String reg = "[0-9]{2}\\s";

        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(time);

        m.find();

        return Integer.parseInt(m.group().substring(0,2));
    }

    int parseHours(String time) {
        int hours;
        String reg = "[0-9]*";

        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(time);

        m.find();

        return Integer.parseInt(m.group());
    }

    String parsePeriod(String time) {
        String reg = "[A|P]M";

        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(time);

        m.find();
        return m.group();
    }

    String[] getValidDurations() {
        // TODO: IMPLEMENT THE FOLLOWING METHOD TO SCRAPE AND CHECK FOLLOWING BOOKINGS FOR VALID DURATIONS.

        // Bootleg version to follow

        //return validDurations;

        return new String[] {"0.5", "1", "1.5", "2"};
    }

    String getInfo() {
        return time + "\n" + room + "\n" + link + "\n" + group;
    }

    String getTime() { return time; }

    String getRoom() {
        return room;
    }

    public String getLink() {
        return link;
    }

    int getBookingState() {return bookingState; }

    String formatReq() { return "Min: " + this.getMinReq() + " | Max: " + this.getCapacity();}

    int getCapacity() {
        if(room.equals("LIB202A") || room.equals("LIB202B") || room.equals("LIB202C"))
            return 4;
        else
            return 8;
    }

    int getMinReq() {
        if(room.equals("LIB202A") || room.equals("LIB202B") || room.equals("LIB202C"))
            return 2;
        else
            return 3;
    }

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
        dest.writeList(validDurations);
    }
}
