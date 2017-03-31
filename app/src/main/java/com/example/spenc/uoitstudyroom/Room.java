package com.example.spenc.uoitstudyroom;

import java.util.ArrayList;

/**
 * Created by Spenc on 2017-03-31.
 */

class Room {
    String name;
    int capacity;
    int min;
    int imgId;

    public Room(String name, int capacity, int imgId) {
        this.name = name;
        this.capacity = capacity;
        this.imgId = imgId;
        min = capacity/2;
    }

    private ArrayList<Room> rooms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return String.valueOf(capacity) + " bookings available";
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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


