package com.example.michael.userinterface;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity implements View.OnClickListener {
    Button login;
    Button skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwindow);
        login = (Button) findViewById(R.id.loginbutton);
        skip = (Button) findViewById(R.id.skipbutton);

        login.setOnClickListener(LoginActivity.this);
        skip.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginbutton){
            EditText getSID = (EditText)findViewById(R.id.username);
            EditText getPW = (EditText)findViewById(R.id.password);
            String sid = getSID.getText().toString();
            String pw = getPW.getText().toString();
            System.out.println(sid + pw);
            if (isPasswordValid(pw) && isSIDValid(sid)) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
        if(v.getId() == R.id.skipbutton){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private boolean isSIDValid(String sid) {
        return sid.length() == 9;
    }

    private boolean isPasswordValid(String password) {
        return password.length() <= 12;
    }
}
