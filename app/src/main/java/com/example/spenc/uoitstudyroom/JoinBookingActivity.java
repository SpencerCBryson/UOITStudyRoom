package com.example.spenc.uoitstudyroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<String> codes = new ArrayList<>();
    ArrayAdapter<String> groupListAdapter;
    DataScraper ds = new DataScraper();

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

        final SharedPreferences preferences = getSharedPreferences("login", Activity.MODE_PRIVATE);

        final String studentid = preferences.getString("studentid", "");
        final String password = preferences.getString("password", "");

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
                if (groups.get(position).equals("Create new group!")) {
                    Intent createIntent = new Intent(view.getContext(), CreateBookingActivity.class);
                    createIntent.putExtra("booking", currentBooking);
                    createIntent.putExtra("date", date);
                    createIntent.putExtra("formData", formData);
                    startActivity(createIntent);
                    finish();
                    return;
                }

                if (!preferences.getBoolean("valid", false)) {
                    Intent i = new Intent(view.getContext(), LoginActivity.class);
                    i.putExtra("skipped", true);
                    startActivity(i);
                    return;
                }

                postData.put("password", password);
                postData.put("studentid", studentid);
                postData.put("btn", "Create or Join a Group");

                JoinBookingTask joinBookingTask =
                        new JoinBookingTask(view.getContext(), groups.get(position), codes.get(position));
                joinBookingTask.execute(postData);
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

            date = postData.get("date");
            ds.postDate(Integer.parseInt(date), formData);
            char[] cbuf = ds.selectBooking(currentBooking.getRoom(), currentBooking.getTime(), 1);

            Parser parser = new Parser(cbuf);

            postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
            postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
            postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));

            ArrayList<Element> elems = parser.getElements("b>");
            ArrayList<Element> elems2 = parser.getElements("label");

            for (Element elem : elems2) {
                if (!elem.getContent().equals("Create a new group")) {
                    String content = elem.getContent();

                    String code = content.substring(content.length() - 5, content.length() - 1);

                    if (!codes.contains(code)) codes.add(code);
                }
            }

            for (Element elem : elems)
                if (!groups.contains(elem.getContent())) groups.add(elem.getContent());

            groups.add("Create new group!");

            return null;
        }
    }

    class JoinBookingTask extends AsyncTask<HashMap<String,String>,String,Void> {
        Context context;
        ProgressDialog dialog;
        String groupName;
        String code;

        JoinBookingTask(Context context, String groupName, String code) {
            this.context = context;
            this.groupName = groupName;
            this.code = code;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Posting booking.");
            dialog.show();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BookingIntentService.SCRAPE_DONE);

            Intent i = new Intent(context, BookingIntentService.class);
            context.startService(i);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) dialog.dismiss();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String successString = "Partial booking successfully joined!";

            Toast.makeText(context, successString, Toast.LENGTH_LONG).show();
            finish();

        }

        @Override
        protected Void doInBackground(HashMap<String, String>... params) {
            HashMap<String,String> postData = params[0];
            postData.put("radio", code);

            char[] cbuf = ds.selectPartialBooking(postData);

            Parser parser = new Parser(cbuf);

            postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
            postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
            postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));

            postData.put("btn", "Join " + groupName);
            cbuf = ds.joinPartialBooking(postData);

            parser = new Parser(cbuf);
            System.out.println(cbuf);

            return null;
        }
    }

}

