package demo.lqs.com.slideviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by lin on 2017/3/4.
 */

public class SlideHistogram extends AbsSlideView {
    private CanvasSize mCanvasSize;
    private Paint paint;

    public SlideHistogram(Context context) {
        super(context);
        initPaint();

    }

    public SlideHistogram(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mOrientation = SlideOrientation.Horizontal;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 10, 300, 500, paint);
    }

    @Override
    protected void onDrawSlideChildView(Canvas canvas, ISlide slide) {
        if (mCanvasSize == null) {
            mCanvasSize = new CanvasSize(canvas);
        }
        canvas.drawRect(0, 30, 10, 0, paint);

        canvas.translate(slide.getSlideX(), slide.getSlideY());


        for (int i = 0; i < 30; i++) {
            canvas.translate(40, 0);
            canvas.drawRect(0, 0, 20, 30, paint);
        }

    }

    @Override
    protected ISlide setSlide() {
        return new MySlide();
    }

    private void initPaint() {
        paint = new Paint();
//        paint.setColor(0xff000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);


    }

    private class MySlide implements ISlide {

        private float x;
        private float y;
        private float mSlideX = 0;
        private float mSlideY = 0;

        @Override
        public void onMove(MotionEvent event) {

            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                x = event.getX();
                y = event.getY();
            }

            if (action == MotionEvent.ACTION_MOVE) {
                switch (mOrientation) {
                    case Horizontal:
                        mSlideX += event.getX() - x;
                        break;
                    case Vertical:
                        mSlideY += event.getY() - y;
                        break;
                    default: {
                        mSlideX += event.getX() - x;
                        mSlideY += event.getY() - y;
                    }
                    break;
                }

                x = event.getX();
                y = event.getY();

                Log.e("mSlideX", "" + mSlideX);
                Log.e("mSlideY", "" + mSlideY);
                invalidate();
            }
        }

        @Override
        public float getSlideX() {
            return mSlideX;
        }

        @Override
        public float getSlideY() {
            return mSlideY;
        }
    }

    private static class CanvasSize {

        final int width;
        final int height;

        public CanvasSize(Canvas canvas) {
            width = canvas.getWidth();
            height = canvas.getHeight();
        }
    }

    private static class ChildCanvasSize {
        int lowX;
        int higX;
        int lowY;
        int higY;
    }


}
