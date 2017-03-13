package com.example.spenc.uoitstudyroom;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataScraper dataScraper = new DataScraper();
        AsyncTask mScrapeBookingsTask = new ScrapeBookingsTask(MainActivity.this).execute(dataScraper);


    }
}
