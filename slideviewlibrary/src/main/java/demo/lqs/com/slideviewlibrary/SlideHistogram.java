package demo.lqs.com.slideviewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2017/3/4.
 */

public class SlideHistogram extends AbsSlideView {
    private CanvasSize mCanvasSize;

    private ChildCanvasSize mChildCanvasSize;

    private Paint paint;

    private static final int line_Padding = 60;

    private ArrayList<HistogramData> mHistogramDatas = new ArrayList<>();

    private double mMaxValue;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDrawSlideChildView(Canvas canvas, ISlide slide) {
        if (mCanvasSize == null) {
            mCanvasSize = new CanvasSize(canvas);
            mChildCanvasSize = new ChildCanvasSize(0, 900, 0, 500);
        }

        canvas.translate(line_Padding, mCanvasSize.height - line_Padding);
        canvas.drawLine(0, 0, mCanvasSize.width -line_Padding , 0, paint);
        canvas.drawLine(0, 0, 0, -mCanvasSize.height +line_Padding, paint);

        canvas.clipRect(new Rect(0, -mCanvasSize.height, mCanvasSize.width,line_Padding ));
        canvas.drawARGB(55,55,55,55);

        canvas.translate(slide.getSlideX(), slide.getSlideY());

        for (int i = 0; i < 30; i++) {
            canvas.translate(40, 0);
            canvas.drawRect(0, -30, 20, 0, paint);
        }
        canvas.restore();

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

    public void setHistogramDatas(List<HistogramData> datas){
        this.mHistogramDatas.addAll(datas);
        for(HistogramData data : mHistogramDatas){
            if(mMaxValue < data.getmValue()){
                mMaxValue = data.getmValue();
            }
        }
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

                if (mCanvasSize.width > mChildCanvasSize.higX - mChildCanvasSize.lowX) {
                    mSlideX = -mChildCanvasSize.lowX;
                } else if (mSlideX > mChildCanvasSize.lowX) {
                    mSlideX = mChildCanvasSize.lowX;
                } else if (mSlideX < mCanvasSize.width - mChildCanvasSize.higX) {
                    mSlideX = mCanvasSize.width - mChildCanvasSize.higX;
                }

                if (mCanvasSize.height > mChildCanvasSize.higY - mChildCanvasSize.lowY) {
                    mSlideY = -mChildCanvasSize.lowY;
                } else if (mSlideY > mChildCanvasSize.lowY) {
                    mSlideY = mChildCanvasSize.lowY;
                } else if (mSlideY < mCanvasSize.height - mChildCanvasSize.higX) {
                    mSlideY = mCanvasSize.height - mChildCanvasSize.higX;
                }


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

        public ChildCanvasSize(int lowX, int higX, int lowY, int higY) {
            this.lowX = lowX;
            this.higX = higX;
            this.lowY = lowY;
            this.higY = higY;
        }

    }

    public static class HistogramData{
        private String mTitle;
        private double mValue;

        public HistogramData(String mTitle, int mValue) {
            this.mTitle = mTitle;
            this.mValue = mValue;
        }

        public String getmTitle() {
            return mTitle;
        }

        public double getmValue() {
            return mValue;
        }
    }


}
