package com.example.henrytran.deltahacksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class menuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //        Set Button Listener
        ((Button) findViewById(R.id.driveButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LD", "Enter button of welcome pressed");

                Intent intent = new Intent(menuActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

        //        Set Button Listener
        ((Button) findViewById(R.id.reviewButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LD", "Review button of welcome pressed");

                Intent intent = new Intent(menuActivity.this, testActivity.class);
                startActivity(intent);
            }

        });
    }

}
