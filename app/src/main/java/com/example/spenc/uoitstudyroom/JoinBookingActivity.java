package com.example.spenc.uoitstudyroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by michael on 3/30/2017.
 */

public class JoinBookingActivity extends AppCompatActivity {
    TextView bookingText;
    ListView groupList;
    Booking currentBooking;
    HashMap<String, String> postData = new HashMap<>();
    String date;
    String[] formData;
    ArrayList<String> groups = new ArrayList<>();
    ArrayAdapter<String> groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_only);

        Intent i = this.getIntent();
        Bundle extras = i.getExtras();
        currentBooking = extras.getParcelable("booking");
        date = extras.getString("date");
        formData = extras.getStringArray("formData");
        SelectBookingTask selectBookingTask = new SelectBookingTask(this);
        groupList = (ListView) findViewById(R.id.groupList);
        bookingText = (TextView) findViewById(R.id.bookingInfo);

        bookingText.setText(
                currentBooking.getRoom() + "\n" +
                currentBooking.getTime() + "\n" +
                currentBooking.getCapacity() + "\n" +
                currentBooking.getMinReq()
        );

        // Select booking, scrape & display groups
        postData.put("date", date);
        postData.put("room", currentBooking.getRoom());
        postData.put("time", currentBooking.getTime());
        postData.put("btn", "Create or Join a Group");
        selectBookingTask.execute(postData);

        groupList = (ListView) findViewById(R.id.groupList);
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    class SelectBookingTask extends AsyncTask<HashMap<String,String>,String,Void>{
        ProgressDialog dialog;
        Context context;

        SelectBookingTask(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading booking.");
            dialog.show();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BookingIntentService.SCRAPE_DONE);

            Intent i = new Intent(context, BookingIntentService.class);
            context.startService(i);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) dialog.dismiss();

            groupListAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1, groups);
            groupList.setAdapter(groupListAdapter);
            groupListAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(HashMap<String, String>... params) {
            HashMap<String,String> postData = params[0];
            DataScraper ds = new DataScraper();

            date = postData.get("date");
            ds.postDate(Integer.parseInt(date), formData);
            char[] cbuf = ds.selectBooking(currentBooking.getRoom(), currentBooking.getTime(), 1);

            Parser parser = new Parser(cbuf);

            ArrayList<Element> elems = parser.getElements("b");

            for (Element elem : elems)
                if (!groups.contains(elem.getContent())) groups.add(elem.getContent());

            postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
            postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
            postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));

            return null;
        }
    }

    class JoinBookingTask extends AsyncTask<HashMap<String,String>,String,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(HashMap<String, String>... params) {
            return null;
        }
    }

}

