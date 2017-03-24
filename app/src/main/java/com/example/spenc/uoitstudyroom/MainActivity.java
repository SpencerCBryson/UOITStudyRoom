package com.example.spenc.uoitstudyroom;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Booking> mBookingList;

    public ArrayList<Booking> getmBookingList() {
        return mBookingList;
    }

    public void setmBookingList(ArrayList<Booking> mBookingList) {
        this.mBookingList = mBookingList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* START OF DRIVER CODE FOR POSTING A NEW BOOKING */

        HashMap<String,String> postData = new HashMap<String,String>();
        postData.put("date","6292");
        postData.put("room","LIB304");
        postData.put("time","6:00 PM");
        postData.put("btnreserve","Create group");
        postData.put("duration","0.5");
        postData.put("institution","uoit");
        postData.put("groupcode","test");
        postData.put("groupname","test");
        postData.put("notes","test"); //OPTIONAL, however it still need to send it as empty
        postData.put("password","nothankyou");
        postData.put("studentid","999999999");

        //TODO: Verify postData is VALID *BEFORE* sending it to the task!!!

        AsyncTask task = new CreateBookingTask(this).execute(postData);

        /* END OF DRIVER CODE FOR POSTING A NEW BOOKING */
    }
}
