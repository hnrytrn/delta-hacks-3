//package com.example.henrytran.deltahacksandroid;
//
///**
// * Created by L1amDuncan on 2017-01-29.
// */
//
//import android.database.Cursor;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.app.LoaderManager.LoaderCallbacks;
//import android.widget.AdapterView;
//
//
//
//public class ContactsFragment extends Fragment implements
//        LoaderManager.LoaderCallbacks<Cursor>,
//        AdapterView.OnItemClickListener {
//
//    // Called just before the Fragment displays its UI
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        // Always call the super method first
//        super.onActivityCreated(savedInstanceState);
//
//        // Initializes the loader
//        getLoaderManager().initLoader(0, null, this);
//
//    }
//
//}