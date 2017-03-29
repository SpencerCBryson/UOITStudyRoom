package com.example.michael.userinterface;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateBooking extends Activity implements View.OnClickListener {
    Button createBooking;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        createBooking = (Button) findViewById(R.id.createGroup);
        back = (Button) findViewById(R.id.Back);

        createBooking.setOnClickListener(CreateBooking.this);
        back.setOnClickListener(CreateBooking.this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginbutton){
            EditText getSID = (EditText)findViewById(R.id.username);
            EditText getPW = (EditText)findViewById(R.id.password);
            String sid = getSID.getText().toString();
            String pw = getPW.getText().toString();
            System.out.println(sid + pw);
            //Need to have Booking complete page instead of MainActivity
            startActivity(new Intent(CreateBooking.this, MainActivity.class));
            finish();
        }
        if(v.getId() == R.id.skipbutton){
            startActivity(new Intent(CreateBooking.this, MainActivity.class));
            finish();
        }
    }
}
