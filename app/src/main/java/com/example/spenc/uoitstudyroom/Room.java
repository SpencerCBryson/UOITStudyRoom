package com.example.spenc.uoitstudyroom;

import java.util.ArrayList;

/**
 * Created by Spenc on 2017-03-31.
 */

class Room {
    String name;
    int available;
    int min;
    int imgId;

    public Room(String name, int available, int imgId) {
        this.name = name;
        this.available = available;
        this.imgId = imgId;
    }

    private ArrayList<Room> rooms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return String.valueOf(available) + " bookings available";
    }

    public void setCapacity(int capacity) {
        this.available = capacity;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
}


