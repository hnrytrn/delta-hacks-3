package com.example.henrytran.deltahacksandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class enterContactActivity extends AppCompatActivity {
    private static final String TAG1 = "LD";
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Econtact econtact1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_contact);

//        Set Button Listener
        ((Button) findViewById(R.id.enterButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setEcontact()){

//                  Save Contact Info (setEcontact sets local var econtact1

                    SharedPreferences prefs = getSharedPreferences("EContactInfo", MODE_PRIVATE);

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(econtact1);
                    prefsEditor.putString("EContact", json);
                    prefsEditor.commit();

//                    TODO: Successful alert!

//                    Change to Menu View

                  Intent intent = new Intent(enterContactActivity.this, menuActivity.class);
                    startActivity(intent);
                    return;
                }
                else{
//                    Dont save ( checkEcontactValid() will handle error messages )
                    Log.e(LOG_TAG,"Error in data entry");
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
        if (!isPhoneNumberValid(phoneNum)) {
//            Invalid PhoneNum
//            TODO: Invalid Phone number alert!
            Log.d(TAG1,"Phone Num Not Valid");
            return false;
        }


        econtact1 = new Econtact(fName,lName,phoneNum);
        Log.d(TAG1,"Econtact saved!");
        return true;
    }


    public static boolean isPhoneNumberValid(String phoneNumber){
        boolean isValid = false;

        //Initialize reg ex for phone number.
        String expression= "(\\d{10})";


            CharSequence inputStr = phoneNumber;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            if(matcher.matches()){
                isValid = true;
            }

        String expression2= "(\\d{3})(\\[-])(\\d{7})";


        CharSequence inputStr2 = phoneNumber;
        Pattern pattern2 = Pattern.compile(expression);
        Matcher matcher2 = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }


//        "(\\[(])(\\d{3})(\\[)])(\\d{7})","(\\[(])(\\d{3})(\\[)])(\\d{3})(\\[-])(\\d{4})"


        return isValid;
    }

}
