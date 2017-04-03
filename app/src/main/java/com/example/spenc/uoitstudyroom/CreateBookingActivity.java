package com.example.spenc.uoitstudyroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateBookingActivity extends AppCompatActivity {
    private String[] formData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);
        Intent i = this.getIntent();
        Bundle extras = i.getExtras();
        final Booking booking = extras.getParcelable("booking");
        final String id = extras.getString("date");
        formData = extras.getStringArray("formData");

        SharedPreferences preferences = getSharedPreferences("login", Activity.MODE_PRIVATE);

        final String studentid = preferences.getString("studentid", "");
        final String password = preferences.getString("password", "");

        Button createButton = (Button) findViewById(R.id.createButton);
        TextView bookingInfo = (TextView) findViewById(R.id.bookingInfo);
        bookingInfo.setText(booking.getRoom() + "\n" + booking.getTime() + "\n");

        final EditText groupNameField = (EditText) findViewById(R.id.groupNameField);
        final EditText groupCodeField = (EditText) findViewById(R.id.groupCodeField);
        final RadioButton uoitSelected = (RadioButton) findViewById(R.id.uoitSelected);

        final Spinner durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, booking.getValidDurations());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentid.isEmpty() || password.isEmpty()) {
                    Intent i = new Intent(v.getContext(), LoginActivity.class);
                    i.putExtra("skipped", true);
                    startActivity(i);
                    return;
                }

                String groupName = groupNameField.getText().toString();
                String groupCode = groupCodeField.getText().toString();
                Boolean isUOIT = uoitSelected.isChecked();
                HashMap<String, String> postData = new HashMap<>();

                // TODO: ADD REST OF REQUIRED DATA FOR POST / password and student id from login

                postData.put("btnreserve", "Create group");
                postData.put("date", id);
                postData.put("room", booking.getRoom());
                postData.put("time", booking.getTime());
                //postData.put("duration", (String) durationSpinner.getSelectedItem()); //FIX ME
                postData.put("duration","0.5");
                postData.put("institution", isUOIT ? "uoit" : "dc");
                postData.put("groupcode", groupCode);
                postData.put("groupname", groupName);
                postData.put("password", studentid);
                postData.put("studentid", password);
                postData.put("notes", "test");

                AsyncTask task = new CreateBookingTask(v.getContext()).execute(postData);
            }
        });
    }

    // TODO: CURRENTLY NOTHING IS PERFORMED WITHIN THIS TASK / CONSIDER ADDING REFACTORING

    public class CreateBookingTask extends AsyncTask<HashMap<String,String>,String,Void> {

        private ProgressDialog dialog;
        private ArrayList<Booking> bList;
        private Context context;
        private String date;
        private boolean done = false, err = false;

        CreateBookingTask(Context context) {
            this.context = context;
            this.dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Posting booking... please wait.");
            dialog.show();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BookingIntentService.SCRAPE_DONE);
            ResponseReceiver rr = new ResponseReceiver();
            context.registerReceiver(rr, intentFilter);

            Intent i = new Intent(context, BookingIntentService.class);
            context.startService(i);
        }

        @Override
        protected Void doInBackground(HashMap<String, String>... params) {

            HashMap<String,String> postData = params[0];
            DataScraper ds = new DataScraper();

            date = postData.get("date");
            ds.postDate(Integer.parseInt(date), formData);
            char[] cbuf = ds.selectBooking(postData.get("room"),postData.get("time"),0);

            Parser parser = new Parser(cbuf);

            postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
            postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
            postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));

            cbuf = ds.postBooking(postData);
            parser = new Parser(cbuf);

            //Ensuring that user entered content is valid will prevent this error from occuring
            if(parser.select("span", "id", "ContentPlaceHolder1_LabelError") != null) {
                err = true;
                System.out.println(cbuf);
            }

            if(new String(cbuf).contains("HTTP/1.1 500")) {
                System.out.println("BAD REQUEST");
                System.out.println(cbuf);
                err = true;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String successString = "Partial booking successfully created!";
            String failedString = "An error has occurred. Check your booking and please try again.";

            if(err)
                Toast.makeText(context, failedString , Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(context, successString, Toast.LENGTH_LONG).show();
                finish();
            }

            //TODO: send user back to main activity from the posting activity
        }

        public class ResponseReceiver extends BroadcastReceiver {
            public static final String ACTION_RESP =
                    "SCRAPE_DONE";

            @Override
            public void onReceive(Context context, Intent intent) {
                bList = intent.getExtras().getParcelableArrayList(date);
                formData = intent.getExtras().getStringArray("formData");
                done = true;
            }
        }
    }

}
