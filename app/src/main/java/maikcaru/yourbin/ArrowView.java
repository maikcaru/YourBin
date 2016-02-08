package maikcaru.yourbin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by michael.carr on 05/02/16.
 */
public class ArrowView extends View {

    private Paint paint;
    private boolean flipped;
    float leftPoint;
    float rightPoint;
    float step;
    float middlePoint;


    public ArrowView(Context context) {
        super(context);
        createPaint();
        setDayOfWeek(6);

    }

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createPaint();
        setDayOfWeek(6);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArrowView, 0, 0);
        flipped = a.getBoolean(R.styleable.ArrowView_flipped, false);

    }

    private void createPaint() {
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(45);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    protected void onDraw(Canvas canvas) {

        step = canvas.getWidth() / 14;
        int topPoint;
        int bottomPoint;
        int margin = 80;

        if (flipped) {
            topPoint = margin;
            bottomPoint = canvas.getHeight();
            canvas.drawText("Today", middlePoint * step, topPoint/1.5f, paint);
        } else {
            topPoint = canvas.getHeight() - margin;
            bottomPoint = 0;
            canvas.drawText("Collection", middlePoint  * step, canvas.getHeight(), paint);
        }

        Path path = new Path();

        path.moveTo(middlePoint * step, bottomPoint);
        path.lineTo(rightPoint * step, topPoint);
        path.lineTo(leftPoint * step, topPoint);
        path.lineTo(middlePoint * step, bottomPoint);

        canvas.drawPath(path,paint);

    }

    public void setDayOfWeek(int dayOfWeek){

        if (dayOfWeek >=0 || dayOfWeek < 7) {
            leftPoint = (dayOfWeek * 2) + 0.5f;
            rightPoint = (dayOfWeek * 2) + 1.5f;
            middlePoint = (dayOfWeek * 2) + 1;
        }
    }



}

