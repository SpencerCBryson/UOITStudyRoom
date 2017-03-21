package com.example.michael.userinterface;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by michael on 20/03/17.
 */

public class BookingList extends BaseAdapter{
    private Context listContext;
    private List<Booking> bookingList;

    public BookingList(Context listContext, List<Booking> bookingList){
        this.listContext = listContext;
        this.bookingList = bookingList;
    }
    @Override
    public int getCount(){
        return bookingList.size();
    }
    @Override
    public Object getItem(int pos){
        return bookingList.get(pos);
    }
    @Override
    public long getItemId(int pos){
        return pos;
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        View view = View.inflate(listContext, R.layout.booking_list, null);
        TextView room = (TextView)view.findViewById(R.id.room);
        TextView capacity = (TextView)view.findViewById(R.id.capacity);
        TextView avail = (TextView)view.findViewById(R.id.avail);
        TextView time = (TextView)view.findViewById(R.id.time);
        room.setText(bookingList.get(pos).getRoom());
        capacity.setText(bookingList.get(pos).getCapacity());
        avail.setText(bookingList.get(pos).getAvail());
        time.setText(bookingList.get(pos).getTime());
        view.setTag(bookingList.get(pos).getId());
        return view;
    }
}
