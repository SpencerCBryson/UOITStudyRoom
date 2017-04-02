package com.example.spenc.uoitstudyroom;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Spenc on 2017-03-31.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RoomViewHolder>{

    public static class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView roomName;
        TextView capacity;
        ImageView roomImg;

        RoomViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            roomName = (TextView)itemView.findViewById(R.id.room_name);
            roomImg = (ImageView)itemView.findViewById(R.id.room_img);
            capacity = (TextView)itemView.findViewById(R.id.capacity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println(roomName.getText());
        }
    }

    ArrayList<Room> rooms;

    RVAdapter(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room, viewGroup, false);
        RoomViewHolder rvh = new RoomViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RoomViewHolder roomViewHolder, int i) {
        roomViewHolder.roomName.setText(rooms.get(i).getName());
        roomViewHolder.capacity.setText(rooms.get(i).getCapacity());
        roomViewHolder.roomImg.setImageResource(rooms.get(i).getImgId());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
