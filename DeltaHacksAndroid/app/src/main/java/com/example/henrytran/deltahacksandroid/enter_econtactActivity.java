package com.example.henrytran.deltahacksandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by L1amDuncan on 2017-01-28.
 */

public class enter_econtactActivity extends Activity{


    private static final String TAG1 = "LD";
    private Econtact econtact1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_econtact);

//        Set Button Listener
        ((Button) findViewById(R.id.submit_econtact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG1,"Econtact Submit Press");
                if(setEcontact()){

//                  Save Contact Info (setEcontact sets local var econtact1

                    SharedPreferences prefs = getSharedPreferences("deltahacks3.app", Context.MODE_PRIVATE);

                    Log.d(TAG1,"Saving Econtact: "+ econtact1);

                    prefs.edit().putString("EContact",econtact1.toString()).apply();

                }
                else{
//                    Dont save ( checkEcontactValid() will handle error messages )
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






    public class Econtact {


        String firstName;
        String lastName;
        String phoneNumber;

        Econtact(String firstName, String lastName, String phoneNumber){
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
        }

        public String getPhoneNumber(){
            return this.phoneNumber;
        }

        public String getFullName(){
            return this.firstName+" "+this.lastName;
        }




    }

    public boolean setEcontact() {

        String fName = ((EditText) findViewById(R.id.firstName)).getText().toString();
        String lName = ((EditText) findViewById(R.id.lastName)).getText().toString();
        String phoneNum = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();

        if (fName == null || fName.length() == 0) {
//              No first name entered
//            TODO: Pop up alert!
            return false;
        }
        if (lName == null || lName.length() == 0) {
//              No last name entered
//            TODO: Pop up alert!
            return false;
        }
        if (phoneNum == null || phoneNum.length() == 0) {
//              No phone num entered
//            TODO: Pop up alert!
            return false;
        }
        if (phoneNumCheck(phoneNum)) {
//            Invalid PhoneNum
//            TODO: Invalid Phone number alert!
            return false;
        }

        econtact1 = new Econtact(fName,lName,phoneNum);

        return true;
    }

    boolean phoneNumCheck(String phoneTest){
        String[] regexStr = {"^[0-9]*$","^[0-9]{10}$","^[0-9\\-]*$","^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$"};
        for (String x:regexStr) {
            if (phoneTest.matches(x)){
                return true;
            }
        }
        return false;
    }

}
