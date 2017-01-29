package com.example.henrytran.deltahacksandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class enterContactActivity extends AppCompatActivity {
    private static final String TAG1 = "LD";
    private Econtact econtact1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_contact);

//        Set Button Listener
        ((Button) findViewById(R.id.enterButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG1,"Econtact Submit Press");
                if(setEcontact()){

//                  Save Contact Info (setEcontact sets local var econtact1

                    SharedPreferences prefs = getSharedPreferences("deltahacks3.app", Context.MODE_PRIVATE);

                    Log.d(TAG1,"Saving Econtact: "+ econtact1);

                    prefs.edit().putString("EContact",econtact1.toString()).apply();

//                    TODO: Successful alert!

//                    Change to Menu View

                  Intent intent = new Intent(enterContactActivity.this, menuActivity.class);
                    startActivity(intent);
                    return;
                }
                else{
//                    Dont save ( checkEcontactValid() will handle error messages )
                    Log.d(TAG1,"Error in data entry");
                    return;
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }








    public boolean setEcontact() {

        String fName = ((EditText) findViewById(R.id.fName)).getText().toString();
        String lName = ((EditText) findViewById(R.id.lName)).getText().toString();
        String phoneNum = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();

        if (fName == null || fName.length() == 0) {
//              No first name entered
//            TODO: Pop up alert!
            Log.d(TAG1,"No fName");
            return false;
        }
        if (lName == null || lName.length() == 0) {
//              No last name entered
//            TODO: Pop up alert!
            Log.d(TAG1,"No lName");
            return false;
        }
        if (phoneNum == null || phoneNum.length() == 0) {
//              No phone num entered
//            TODO: Pop up alert!
            Log.d(TAG1,"No phone num");
            return false;
        }
//        if (phoneNumCheck(phoneNum)) {
////            Invalid PhoneNum
////            TODO: Invalid Phone number alert!
//            Log.d(TAG1,"Phone Num Not Valid");
//            return false;
//        }


        econtact1 = new Econtact(fName,lName,phoneNum);
        Log.d(TAG1,"Econtact saved!");
        return true;
    }

//    boolean phoneNumCheck(String phoneTest){
//        String[] regexStr = {"^[0-9]*$","^[0-9]{10}$","^[0-9\\-]*$","^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$"};
//        for (String x:regexStr) {
//            if (phoneTest.matches(x)){
//                return true;
//            }
//        }
//        return false;
//    }

}
