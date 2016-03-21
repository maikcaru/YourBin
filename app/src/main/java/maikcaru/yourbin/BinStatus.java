package maikcaru.yourbin;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class BinStatus extends NavigationDrawerParent {

    private ImageView vectorImage;
    private Integer fillLevel = 0;
    private String location;
    private Integer fillPercentage;
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
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        //Convert Android day of week numbering convention to my own
        if (dayOfWeek > 1) {
            dayOfWeek -= 2;
        } else {
            dayOfWeek += 5;
        }
        todayArrow.setDayOfWeek(dayOfWeek);

        ArrowView collectionArrow = (ArrowView) findViewById(R.id.collectionArrow);
        SharedPreferences prefs = getSharedPreferences("maikcaru.yourbin", Context.MODE_PRIVATE);
        collectionArrow.setDayOfWeek(prefs.getInt("dayOfWeek", 0));

        //Animate the bin
        vectorImage = (ImageView) findViewById(R.id.bin);

        //Hard-coded to 70%, needs to be sourced from hub
        new Refresh().execute();

        //Animate on click
        vectorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Refresh().execute();
            }
        });

    }

    private Integer getPercentage() {
        if (fillLevel > 108) {
            fillLevel = 108;
        }
        Double fillPerc;
        fillPerc = ((double) fillLevel / 108) * 100;
        fillPerc = Math.round((fillPerc) / 10.0) * 10.0;
        fillPercentage = fillPerc.intValue();
        return fillPercentage;
    }

    private Notification buildNotification() {
        Notification notification = new NotificationCompat.Builder(BinStatus.this)
                .setContentTitle("YourBin Reminder")
                .setContentText("Move YourBin to it's collection point. ")
                .setSmallIcon(R.drawable.bin_clipart_vector)
                .setContentIntent(PendingIntent.getActivity(BinStatus.this, 0, new Intent(BinStatus.this, BinStatus.class), 0))
                .build();
        return notification;
    }

    private void updateUI() {
        Log.e("Animations index", ((fillPercentage / 10)) + "");
        int index = (fillPercentage / 10);
        if (index == 10) {
            index--;
        }
        vectorImage.setImageResource(animations[index]);
        ((Animatable) vectorImage.getDrawable()).start();
        TextView textViewFillLevel = (TextView) findViewById(R.id.textFillLevel);
        String[] fillLevels = getResources().getStringArray(R.array.fill_array);
        textViewFillLevel.setText(fillLevels[index]);

    }

    public class Refresh extends AsyncTask<Void, Void, JSONObject> {

        JSONObject sendREST(String RESTCommand) {
            try {
                URL url = new URL("https://graph-eu01-euwest1.api.smartthings.com/api/smartapps/installations/eb482c79-1131-4b39-874e-0730a9ce1e43/" + RESTCommand);
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

