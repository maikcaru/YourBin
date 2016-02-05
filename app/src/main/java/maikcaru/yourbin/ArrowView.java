package maikcaru.yourbin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by michael.carr on 05/02/16.
 */
public class ArrowView extends View {

    private Paint paint;
    private boolean flipped;

    public ArrowView(Context context) {
        super(context);
        createPaint();
    }

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createPaint();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArrowView, 0, 0);
        flipped = a.getBoolean(R.styleable.ArrowView_flipped, false);

    }

    private void createPaint() {
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
    }

    protected void onDraw(Canvas canvas) {


        int step = canvas.getWidth() / 14;
        int height;
        int otherHeight;

        if (flipped) {
            height = 0;
            otherHeight = canvas.getHeight();
        } else {
            height = canvas.getHeight();
            otherHeight = 0;
        }

        Path path = new Path();

        path.moveTo(step, otherHeight);
        path.lineTo(1.5f * step, height);
        path.lineTo(0.5f * step, height);
        path.lineTo(step, otherHeight);

        canvas.drawPath(path,paint);



    }


}

