package com.example.henrytran.deltahacksandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // GoogleAPIClient used for location services
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Econtact mEContact;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private ImageView head;
    private TextView xCordTV;
    private TextView yCordTV;
    int count = 5;
    private boolean stopBackgroundThread;

    private int sleepCount = 0;

    // Handles data coming from the server
    private final Handler mHandler = new Handler() {
        final MediaPlayer mp = new MediaPlayer();
        int threshold = 17;
        @Override
        public void handleMessage(Message msg) {
            int[] posVals = (int[]) msg.obj;
            xCordTV.setText(String.valueOf(posVals[0]));
            yCordTV.setText(String.valueOf(posVals[1]));

            if (posVals[0] > threshold || posVals[1] > threshold || posVals[0] < -threshold || posVals[1] < -threshold) {

                if(mp.isPlaying())
                {
                    mp.stop();
                }

                try {
                    mp.reset();
                    AssetFileDescriptor afd;
                    afd = getAssets().openFd("alarm_beep.mp3");
                    mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mp.prepare();
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (sleepCount == 20) {
                    sendSms();
                } else if (sleepCount == 40) {
                    callNumber();
                }
                sleepCount++;
                Log.e(LOG_TAG, "Count: " + sleepCount);
            }
            int max = Math.max(Math.abs(posVals[1]), Math.abs(posVals[0]));

                    if(max>15){
                        head.setColorFilter(Color.parseColor("#4c0000"));
                    } else {
                        if(max>10){
                            head.setColorFilter(Color.parseColor("#660000"));
                        }
                    }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        xCordTV = (TextView) findViewById(R.id.x);
        yCordTV = (TextView) findViewById(R.id.y);
        head = (ImageView) findViewById(R.id.head);
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
        // Get eContactNumber from shared preferences
        SharedPreferences prefs = getSharedPreferences("EContactInfo", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("EContact", "");
        mEContact = gson.fromJson(json, Econtact.class);

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
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//
//            Log.e("MainActivity", String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));
//        }
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
            Log.e(LOG_TAG, "Can't Call " + Uri.parse("tel:" + mEContact.getPhoneNumber()).toString());
            return;
        }
        stopBackgroundThread = true;
        startActivity(callIntent);
    }

    private void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mEContact.getPhoneNumber(), null, "Hey "+mEContact.getFullName()+ ", I'm feeling a bit tired at the wheel. You should give me a call to talk! -Sent By LifeLine Android Application", null, null);
        Log.e(LOG_TAG, "Sent message");
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

            stopBackgroundThread = false;

            while (!stopBackgroundThread) {

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
                    }

                    jsonData = buffer.toString();
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
            int[] posVals = new int[3];
            posVals[0] = dataJsonArray.getInt(0);
            posVals[1] = dataJsonArray.getInt(1);
            posVals[2] = dataJsonArray.getInt(2);
            count++;
            if(count%20 == 0) {
                head.setRotation(posVals[0] * 3);
                int max = Math.max(Math.abs(posVals[1]), Math.abs(posVals[0]));
                head.setTranslationY(max * 8);
            }
            if (count % 10 == 0) {
                // Send data to handler
                Message message = new Message();
                message.obj = posVals;
                mHandler.sendMessage(message);
            }
        }






    }
}
