package com.example.spenc.uoitstudyroom;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/27/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Booking> mBookings;

    public BookingAdapter(Context context, ArrayList<Booking> bookings) {
        mContext = context;
        mBookings = bookings;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mBookings.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.booking_item, parent, false);

        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.booking_list_title);
        TextView roomTextView =
                (TextView) rowView.findViewById(R.id.booking_list_room);
        TextView reqTextView =
                (TextView) rowView.findViewById(R.id.booking_list_requirements);
        TextView joinTextView =
                (TextView) rowView.findViewById(R.id.booking_list_join);

        Booking booking = (Booking) getItem(position);

        titleTextView.setText(booking.getTime());
        if(booking.getBookingState() != 1)
            titleTextView.setTextColor(Color.parseColor("#228B22"));
        roomTextView.setText(booking.getRoom());
        reqTextView.setText(booking.formatReq());


        return rowView;
    }

}