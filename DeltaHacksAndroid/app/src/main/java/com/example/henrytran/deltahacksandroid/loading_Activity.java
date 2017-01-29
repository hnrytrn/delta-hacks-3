package com.example.henrytran.deltahacksandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class loading_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firsttime_user);

        Intent intent = new Intent(this, enter_econtact_Activity.class);
        startActivity(intent);
    }

    }

