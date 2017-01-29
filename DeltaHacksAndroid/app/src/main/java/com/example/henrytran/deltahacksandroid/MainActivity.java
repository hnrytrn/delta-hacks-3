package com.example.henrytran.deltahacksandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int DATA_MESSAGE = 10;

    // GoogleAPIClient used for location services
    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;
    private Econtact mEContact;
    private String LOG_TAG = this.getClass().getSimpleName();

    // Handles data coming from the server
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_MESSAGE:
                    double[] posVals = (double[]) msg.obj;

                    Log.e(LOG_TAG, "x = " + posVals[0] + "\ny = " + posVals[1] + "\nz = " + posVals[2]);

                    // TODO Update UI
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

//        // Create an instance of GoogleAPIClient
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Log.e(LOG_TAG, String.valueOf(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude()));
//        // Get eContactNumber from shared preferences
//        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = prefs.getString("EContact", "");
//        mEContact = gson.fromJson(json, Econtact.class);

        FetchDataThread fetchDataThread = new FetchDataThread();
        fetchDataThread.start();
    }

    @Override
    protected void onStart() {
//        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
  //      mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            Log.e("MainActivity", String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mEContact.getPhoneNumber()));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    /**
     *
     * Continuously fetches data from the http server
     *
     */
    private class FetchDataThread extends Thread {

        public void run() {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;

            while (true) {
                try {
                    // Create request to http server
                    URL url = new URL("http://172.17.43.101:8080");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                        Log.e(LOG_TAG, "Line: " + line);
                    }

                    jsonData = buffer.toString();
                    Log.e(LOG_TAG, jsonData);
                    parseJsonData(jsonData);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
            }
        }

        /**
         *
         * Parses Json data from the http server and passes it to the handler
         *
         * @param jsonStr json string response from http server
         * @throws JSONException
         */
        private void parseJsonData(String jsonStr) throws JSONException {
            JSONArray dataJsonArray = new JSONArray(jsonStr);
            // x, y, z values
            double[] posVals = new double[3];
            posVals[0] = dataJsonArray.getDouble(0);
            posVals[1] = dataJsonArray.getDouble(1);
            posVals[2] = dataJsonArray.getDouble(2);

            Log.e(LOG_TAG, "x = " + String.valueOf(posVals[0]) + "\ny = " + String.valueOf(posVals[1]) + "\nz = " + String.valueOf(posVals[2]));

            // Send data to handler
            mHandler.obtainMessage(DATA_MESSAGE, posVals);
        }
    }
}
