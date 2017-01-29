package com.example.henrytran.deltahacksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class welcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


//        Set Button Listener
        ((ImageView) findViewById(R.id.wheel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LD", "Enter button of welcome pressed");

                Intent intent = new Intent(welcomeActivity.this, enterContactActivity.class);
                startActivity(intent);
            }

        });
    }
}
