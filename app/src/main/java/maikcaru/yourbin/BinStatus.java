package maikcaru.yourbin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
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
}
