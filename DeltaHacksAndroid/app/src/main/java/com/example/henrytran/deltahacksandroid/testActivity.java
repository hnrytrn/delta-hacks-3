package com.example.henrytran.deltahacksandroid;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yayandroid.rotatable.Rotatable;

/**
 * Created by Yahya Bayramoglu on 04/12/15.
 */
public class testActivity extends AppCompatActivity {

    private Handler handler;
    private final int ANIM_DURATION = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        handler = new Handler();


//        Set Button Listener
                ((android.widget.Button) findViewById(R.id.addX)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runAnimationOn(R.id.headLeft, Rotatable.ROTATE_BOTH, 20, 0);

            }
        });


    }

    private void runAnimationOn(final int resId, final int direction, final int degree, int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Rotatable rotatable = new Rotatable.Builder(findViewById(resId))
                        .direction(Rotatable.ROTATE_BOTH)
                        .build();
                rotatable.rotate(direction, degree, ANIM_DURATION);
            }
        }, delay);
    }

}


