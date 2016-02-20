package maikcaru.yourbin;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class BinStatus extends NavigationDrawerParent {

    private ImageView vectorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_status);
        createToolbar();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, getNotification());
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
        vectorImage.setImageResource(R.drawable.bin_animated_70);
        ((Animatable) vectorImage.getDrawable()).start();
        TextView textViewFillLevel = (TextView) findViewById(R.id.textFillLevel);
        String[] fillLevels = getResources().getStringArray(R.array.fill_array);
        textViewFillLevel.setText(fillLevels[6]);

        //Animate on click
        vectorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Animatable) vectorImage.getDrawable()).start();

            }
        });

    }

    private Notification getNotification() {
        Notification notification = new NotificationCompat.Builder(BinStatus.this)
                .setContentTitle("YourBin Reminder")
                .setContentText("Move YourBin to it's collection point. ")
                .setSmallIcon(R.drawable.bin_clipart_vector)
                .setContentIntent(PendingIntent.getActivity(BinStatus.this, 0, new Intent(BinStatus.this, BinStatus.class), 0))
                .build();
        return notification;
    }

}

