package com.example.henrytran.deltahacksandroid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by L1amDuncan on 2017-01-28.
 */
public class Econtact implements Parcelable{


    String firstName;
    String lastName;
    String phoneNumber;

    public Econtact(String firstName, String lastName, String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    protected Econtact(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.firstName = data[0];
        this.lastName = data[1];
        this.phoneNumber = data[2];
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
            this.firstName,
            this.lastName,
            this.phoneNumber
        });
    }

    public static final Parcelable.Creator<Econtact> CREATOR = new Parcelable.Creator<Econtact>() {

        @Override
        public Econtact createFromParcel(Parcel source) {
            return new Econtact(source);
        }

        @Override
        public Econtact[] newArray(int size) {
            return new Econtact[size];
        }
    };
}
