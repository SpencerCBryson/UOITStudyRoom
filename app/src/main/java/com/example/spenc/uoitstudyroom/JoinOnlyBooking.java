package com.example.spenc.uoitstudyroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by michael on 3/30/2017.
 */

public class JoinOnlyBooking extends AppCompatActivity {
    TextView bookingList;
    ListView groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_join_only);
        groupList = (ListView) findViewById(R.id.groupList);
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Selection of listView applied here
                System.out.println("");
                //Add items to grouplist using:
                //R.id.join_group_name
                //R.id.join_group_code
            }
            });
    ;}

}

