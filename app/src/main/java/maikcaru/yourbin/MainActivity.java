package maikcaru.yourbin;

import android.animation.AnimatorSet;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private ImageView vectorImage;
    AnimatorSet set;
    Object target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vectorImage = (ImageView) findViewById(R.id.imageView);


        vectorImage.setImageResource(R.drawable.bin_animated_10);


        vectorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Animatable) vectorImage.getDrawable()).start();
            }
        });

    }

}
