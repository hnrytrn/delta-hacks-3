package com.example.henrytran.deltahacksandroid;

/**
 * Created by L1amDuncan on 2017-01-28.
 */
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