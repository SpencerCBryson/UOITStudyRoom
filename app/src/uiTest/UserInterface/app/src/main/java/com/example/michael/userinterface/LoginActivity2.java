package com.example.michael.userinterface;

import android.content.Intent;
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

/**
 * Created by michael on 28/03/17.
 */

public class LoginActivity2 extends AppCompatActivity {
    private EditText mSIDView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mSIDView = (EditText) findViewById(R.id.sid);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mSIDView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String sid = mSIDView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if password is valid.
        if (!TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isPasswordValid(password)) {
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
            startActivity(new Intent(LoginActivity2.this, MainActivity.class));
            finish();
        }
    }

    private boolean isSidValid(String sid) { return sid.startsWith("100"); }
    private boolean isPasswordValid(String password) { return password.length() > 4; }

}

