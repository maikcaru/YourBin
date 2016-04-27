package maikcaru.yourbin;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class BinStatus extends NavigationDrawerParent {

    int animations[] = {
            R.drawable.bin_animated_10,
            R.drawable.bin_animated_20,
            R.drawable.bin_animated_30,
            R.drawable.bin_animated_40,
            R.drawable.bin_animated_50,
            R.drawable.bin_animated_60,
            R.drawable.bin_animated_70,
            R.drawable.bin_animated_80,
            R.drawable.bin_animated_90,
            R.drawable.bin_animated
    };
    private ImageView vectorImage;
    private Integer fillLevel = 0;
    private String location;
    private Integer fillPercentage;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_status);
        createToolbar();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, buildNotification());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + 2000;


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        ArrowView todayArrow = (ArrowView) findViewById(R.id.todayArrow);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        //Convert Android day of week numbering convention to my own
        if (currentDay > 1) {
            currentDay -= 2;
        } else {
            currentDay += 5;
        }
        todayArrow.setDayOfWeek(currentDay);

        ArrowView collectionArrow = (ArrowView) findViewById(R.id.collectionArrow);
        SharedPreferences prefs = getSharedPreferences("maikcaru.yourbin", Context.MODE_PRIVATE);
        collectionArrow.setDayOfWeek(prefs.getInt("dayOfWeek", 0));

        int hour = prefs.getInt("hour", 22);
        int minute = prefs.getInt("minute", 00);
        int dayOfWeek = prefs.getInt("dayOfWeek", -1);

        dayOfWeek = ((dayOfWeek + 1) % 7) + 1;

        Calendar now = Calendar.getInstance();
        int today = now.get(Calendar.DAY_OF_WEEK);
        if (today != dayOfWeek) {
            // calculate how much the difference between today and set day of week
            int days = (dayOfWeek - today) % 7;
            now.add(Calendar.DAY_OF_YEAR, days);
        }
        now.set(Calendar.HOUR_OF_DAY, hour);
        now.set(Calendar.MINUTE, minute);

        Date date = now.getTime();
        double notificationTime = date.getTime();

        //Animate the bin
        vectorImage = (ImageView) findViewById(R.id.bin);
        // Request initial data from the hub.
        new Refresh().execute();

        //Animate on click
        vectorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Refresh().execute();
                Toast toast = Toast.makeText(BinStatus.this, "Retrieving data", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private Integer getPercentage() {
        if (fillLevel > 108) {
            fillLevel = 108;
        }
        Double fillPerc;
        // Get bin height from preferences file, defaults to standard height of a household bin of 108 cm
        Integer binHeight = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("binHeight", "108"));
        Log.e("Bin Height is", binHeight + "");
        fillPerc = ((double) fillLevel / binHeight) * 100;
        fillPerc = Math.round((fillPerc) / 10.0) * 10.0;
        fillPercentage = fillPerc.intValue();
        return fillPercentage;
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this)
                .setContentTitle("YourBin Reminder")
                .setContentText("Move YourBin to it's collection point. ")
                .setSmallIcon(R.drawable.bin_clipart_vector)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, BinStatus.class), 0))
                .build();
    }

    private void updateUI() {
        Log.e("Animations index", ((fillPercentage / 10)) + "");
        int index = 10 - (fillPercentage / 10);
        if (index == 10) {
            index--;
        }
        vectorImage.setImageResource(animations[index]);
        ((Animatable) vectorImage.getDrawable()).start();
        TextView textViewFillLevel = (TextView) findViewById(R.id.textFillLevel);
        String[] fillLevels = getResources().getStringArray(R.array.fill_array);
        textViewFillLevel.setText(fillLevels[index]);

        LatLng binLocation = splitGPS(location);
        LatLng collectionLatLng = splitGPS(PreferenceManager.getDefaultSharedPreferences(this).getString("collectionLocation", "0,0"));
        LatLng storageLatLng = splitGPS(PreferenceManager.getDefaultSharedPreferences(this).getString("storageLocation", "0,0"));

        TextView binLocationIs = (TextView) findViewById(R.id.binLocationIs);

        if (withIn5m(collectionLatLng, binLocation)) {
            binLocationIs.setText("Bin Location Is: Collection Location");
        } else if (!withIn5m(storageLatLng, binLocation)) {
            binLocationIs.setText("Bin Location Is: Storage Location");
        } else {
            binLocationIs.setText("Bin Location Is Not Known:" + binLocation.toString());
        }

    }

    public boolean withIn5m(LatLng testLocation, LatLng binLocation) {

        float[] results = new float[1];
        Location.distanceBetween(testLocation.latitude, testLocation.longitude, binLocation.latitude, binLocation.longitude, results);
        float distanceInMeters = results[0];
        boolean status = distanceInMeters < 5;
        Log.e("With in 5m", "" + status);
        return status;
    }


    public LatLng splitGPS(String testLocation) {
        LatLng locationLatLng = new LatLng();
        String[] locationStringSplit = location.split(",");
        try {
            locationLatLng.latitude = Double.parseDouble(locationStringSplit[0]);
            locationLatLng.longitude = Double.parseDouble(locationStringSplit[1]);
        } catch (NumberFormatException nfe) {
            Log.e("No location", "found");
        }

        return locationLatLng;
    }


    public class Refresh extends AsyncTask<Void, Void, JSONObject> {

        JSONObject sendREST(String RESTCommand) {
            try {
                URL url = new URL("https://graph-eu01-euwest1.api.smartthings.com/api/smartapps/installations/393e6424-7bcc-428a-99d4-dbdb3a3cbaff/" + RESTCommand);
                //   URL url = new URL("http://10.0.0.15:8080/");
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                httpCon.setDoInput(true);
                httpCon.setRequestMethod("GET");
                httpCon.setRequestProperty("Authorization", "Bearer 2b68e2e7-9295-4ffb-ade7-569b4347687d");

                int status = httpCon.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        return new JSONObject(sb.toString());
                }
            } catch (Exception ioE) {
                Log.e("Exception", ioE.toString());
            }
            return null;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            sendREST("refresh");
            JSONObject response = sendREST("read");
            while (response == null || response.isNull("fillLevel")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response = sendREST("read");
            }
            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            try {
                fillLevel = data.getInt("fillLevel");
                location = data.getString("location");
                Log.e("Fill Level", fillLevel.toString());
                Log.e("Location", location);
                Log.e("Fill Percentage", getPercentage().toString());
                updateUI();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}

