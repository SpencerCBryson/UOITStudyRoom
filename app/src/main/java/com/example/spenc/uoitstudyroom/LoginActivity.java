package com.example.spenc.uoitstudyroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by michael on 28/03/17.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText mSIDView;
    private EditText mPasswordView;
    Boolean skipped = false;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login (optional)");

        Intent i = this.getIntent();

        if (i.getExtras() != null) skipped = true;

        mSIDView = (EditText) findViewById(R.id.sid);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin(textView.getContext());
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        Button skipButton = (Button) findViewById(R.id.skip_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(view.getContext());
            }
        });
        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SharedPreferences preferences = getSharedPreferences("login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("valid", false);
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(Context context) {
        // Reset errors.
        mSIDView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String sid = mSIDView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if password is valid.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mSIDView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check if Student ID is valid.
        if (TextUtils.isEmpty(sid)) {
            mSIDView.setError(getString(R.string.error_field_required));
            focusView = mSIDView;
            cancel = true;
        } else if (!isSidValid(sid)) {
            mSIDView.setError(getString(R.string.error_invalid_sid));
            focusView = mSIDView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Login Passed, Finish task and move on to MainActivity (BookingList)
            ValidateLoginTask validateLoginTask =
                    new ValidateLoginTask(sid, password, context);

            validateLoginTask.execute((Void) null);
        }
    }

    /**
     * Test user login information to see if it is valid
     */
    class ValidateLoginTask extends AsyncTask<Void, Void, Void> {

        private String studentid;
        private String password;
        private Context context;
        private boolean err = false;

        ValidateLoginTask(String studentid, String password, Context context) {
            this.studentid = studentid;
            this.password = password;
            this.context = context;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Attempting Login.");
            dialog.show();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BookingIntentService.SCRAPE_DONE);

            Intent i = new Intent(context, BookingIntentService.class);
            context.startService(i);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String successString = "Login Successful!";
            String failedString = "Login failed. Check your login credentials again.";

            if(err)
                Toast.makeText(context, failedString, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(context, successString, Toast.LENGTH_LONG).show();
                SharedPreferences preferences = getSharedPreferences("login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("valid", true);
                editor.putString("studentid", studentid);
                editor.putString("password", password);

                editor.apply();
                finish();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> postData = new HashMap<>();
            DataScraper ds = new DataScraper();
            char[] cbuf = ds.getRawLoginHtml();

            Parser parser = new Parser(cbuf);

            postData.put("vstate", parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value"));
            postData.put("vstategen", parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value"));
            postData.put("evalid", parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value"));
            postData.put("password", password);
            postData.put("studentid", studentid);

            cbuf = ds.postLogin(postData);
            parser = new Parser(cbuf);

            if(parser.select("span", "id", "ContentPlaceHolder1_LabelError") != null) {
                err = true;
            }

            return null;
        }
    }

    private boolean isSidValid(String sid) { return sid.startsWith("100"); }
    private boolean isPasswordValid(String password) { return password.length() > 4; }

}
