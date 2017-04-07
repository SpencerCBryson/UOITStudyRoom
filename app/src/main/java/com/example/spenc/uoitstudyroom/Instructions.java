package com.example.spenc.uoitstudyroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by michael on 06/04/17.
 */

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        TextView howToBook = (TextView) findViewById(R.id.HowToBook);
        TextView guidelines = (TextView) findViewById(R.id.Guidelines);
        String populateHowTo = "";
        String populateGuidelines = "";
        try{
            BufferedReader inputHowTo = new BufferedReader(
                    new InputStreamReader(getAssets().open("howToBook.txt")));
            BufferedReader inputGuide =  new BufferedReader(
                    new InputStreamReader(getAssets().open("guidelines.txt")));
            String line;

            while((line = inputHowTo.readLine()) != null) {
                populateHowTo = populateHowTo + line + "\n";
            }
            while((line = inputGuide.readLine()) != null) {
                populateGuidelines = populateGuidelines + line + "\n";
            }

            inputHowTo.close();
            inputGuide.close();
            howToBook.setText(populateHowTo);
            guidelines.setText(populateGuidelines);
            howToBook.setMovementMethod(new ScrollingMovementMethod());
            guidelines.setMovementMethod(new ScrollingMovementMethod());
        }catch (IOException e){
            System.exit(0);
        }


    }
}
