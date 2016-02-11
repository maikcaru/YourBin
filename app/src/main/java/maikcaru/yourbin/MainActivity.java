package maikcaru.yourbin;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;

public class MainActivity extends NavigationDrawerParent {

    private ImageView vectorImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToolbar();


        Calendar calendar = Calendar.getInstance();
        ArrowView todayArrow = (ArrowView) findViewById(R.id.todayArrow);
        todayArrow.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));


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
