package maikcaru.yourbin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
        if (dayOfWeek > 1) {
            dayOfWeek -= 2;
        } else {
            dayOfWeek += 5;
        }
        todayArrow.setDayOfWeek(dayOfWeek);

        ArrowView collectoinArrow = (ArrowView) findViewById(R.id.collectionArrow);
        SharedPreferences prefs = getSharedPreferences("maikcaru.yourbin", Context.MODE_PRIVATE);
        collectoinArrow.setDayOfWeek(prefs.getInt("dayOfWeek", 11));



        vectorImage = (ImageView) findViewById(R.id.bin);
        vectorImage.setImageResource(R.drawable.bin_animated_70);

        vectorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Animatable) vectorImage.getDrawable()).start();
            }
        });

    }
}
