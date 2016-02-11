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
    int dayOfWeek;


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
        paint.setTextSize(70);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    protected void onDraw(Canvas canvas) {

        int topPoint;
        int bottomPoint;
        int margin = 80;

        float leftPoint = (dayOfWeek * 2) + 0.5f;
        float rightPoint = (dayOfWeek * 2) + 1.5f;
        float middlePoint = (dayOfWeek * 2) + 1;

        float textPoint = middlePoint;
        float step = canvas.getWidth() / 14;

        if (dayOfWeek == 0) {
            textPoint = leftPoint;
            paint.setTextAlign(Paint.Align.LEFT);
        } else if (dayOfWeek == 6) {
            textPoint = rightPoint;
            paint.setTextAlign(Paint.Align.RIGHT);
        }

        if (flipped) {
            topPoint = margin;
            bottomPoint = canvas.getHeight();
            canvas.drawText("Today", textPoint * step, topPoint / 1.5f, paint);
        } else {
            topPoint = canvas.getHeight() - margin;
            bottomPoint = 0;
            canvas.drawText("Collection", textPoint * step, canvas.getHeight(), paint);
        }

        Path path = new Path();

        path.moveTo(middlePoint * step, bottomPoint);
        path.lineTo(rightPoint * step, topPoint);
        path.lineTo(leftPoint * step, topPoint);
        path.lineTo(middlePoint * step, bottomPoint);

        canvas.drawPath(path, paint);

    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;

    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Background image’s size
        int desiredWidth = 409600;
        int desiredHeight = 100000;

        Log.e("onm", "width spec mode " + MeasureSpec.getMode(widthMeasureSpec) + " size " + MeasureSpec.getSize(widthMeasureSpec));
        Log.e("onm", "height spec mode " + MeasureSpec.getMode(heightMeasureSpec) + " size " + MeasureSpec.getSize(heightMeasureSpec));

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // Final size
        int width, height;

        // Set width depending on mode
        if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        else if(widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        }
        else {
            width = desiredWidth;
        }

        // Set height depending on mode
        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        else if(heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        }
        else {
            height = desiredHeight;
        }

        // Finally, set dimension
        Log.e("onm", "smd " + width + " " + height);
        setMeasuredDimension(width, height);
    }
*/
}

