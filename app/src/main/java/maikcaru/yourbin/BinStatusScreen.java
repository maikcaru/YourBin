package maikcaru.yourbin;

import android.graphics.Path;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class BinStatusScreen extends AppCompatActivity {

    private ImageView vectorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bin_status_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vectorImage = (ImageView) findViewById(R.id.bin);
        vectorImage.setImageResource(R.drawable.bin_animated_70);

        vectorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Animatable) vectorImage.getDrawable()).start();
            }
        });


        View days = findViewById(R.id.slider);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(10, 10);
        path.lineTo(0, 20);
        path.lineTo(20, 20);
        path.close();
        PathShape shape = new PathShape(path, 140 , 30);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        //days.setBackground(drawable);

    }
}
