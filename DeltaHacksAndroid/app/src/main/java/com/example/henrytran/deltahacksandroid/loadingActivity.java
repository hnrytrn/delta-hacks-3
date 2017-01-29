package com.example.henrytran.deltahacksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class loadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //        Set Button Listener
        ((Button) findViewById(R.id.enterButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LD", "Enter button of welcome pressed");

                Intent intent = new Intent(loadingActivity.this, enterContactActivity.class);
                startActivity(intent);
            }

        });
    }


}
