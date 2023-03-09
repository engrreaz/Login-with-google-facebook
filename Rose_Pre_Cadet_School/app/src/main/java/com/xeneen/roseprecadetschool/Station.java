package com.xeneen.roseprecadetschool;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Station extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(Station.this, Browser.class);
            startActivity(i);
            //finish();
        }, 100);
    }
}