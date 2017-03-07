package demo.lqs.com.slideviewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
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
 * 可滑动的柱状图
 */

public class SlideHistogram extends AbsSlideView {
    private CanvasSize mCanvasSize;

    private ChildCanvasSize mChildCanvasSize;

    private Paint mTextPaint;

    private int line_Padding = 60; //边距

    private int line_padding_left = 60;
    private int line_padding_right = 60;
    private int line_padding_top = 60;
    private int line_padding_buttom = 60;
    private int mHistograSpacing = 20;
    private int mHistograWidth = 20;
    private int mXTitleTextSize = 30;
    private int mHistorgramColer = 0x99009900;
    private int mYTitleTextSize = 30;


    private ArrayList<HistogramData> mHistogramDatas = new ArrayList<>();

    private float mMaxValue = 1;
    private Paint mHistogramPaint;

    public SlideHistogram(Context context) {
        super(context);
        initPaint();

    }

    public SlideHistogram(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mOrientation = SlideOrientation.Horizontal;

        ArrayList<HistogramData> histogramDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            histogramDatas.add(new HistogramData("" + i, 5));
            histogramDatas.add(new HistogramData(".", 15));
            histogramDatas.add(new HistogramData(".", 7));
            histogramDatas.add(new HistogramData(".", 16));
            histogramDatas.add(new HistogramData(".", 3));
        }

        setHistogramDatas(histogramDatas);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDrawSlideChildView(Canvas canvas, ISlide slide) {
        if (mCanvasSize == null) {
            mCanvasSize = new CanvasSize(canvas);
            mChildCanvasSize.higY = mCanvasSize.height;
        }

        float touchX = slide.getTouchX();
//        float touchY = slide.getTouchY();

        canvas.translate(line_Padding, mCanvasSize.height - line_Padding);

        touchX -= line_Padding;

        canvas.drawLine(0, 0, mCanvasSize.width - 2 * line_Padding, 0, mTextPaint);
        canvas.drawLine(0, 0, 0, 2 * line_Padding - mCanvasSize.height, mTextPaint);

        int maxY = -mCanvasSize.height + 2 * line_padding_top + line_padding_buttom;
        canvas.drawText("" + mMaxValue, -line_padding_left, maxY, mTextPaint);
        canvas.drawText("" + mMaxValue / 2, -line_padding_left, (maxY) / 2, mTextPaint);


        canvas.clipRect(new Rect(0, -mCanvasSize.height, mCanvasSize.width - 2 * line_Padding, line_Padding));

        canvas.translate(slide.getSlideX(), slide.getSlideY());

//        touchX -= slide.getSlideX();

        for (HistogramData data : mHistogramDatas) {
            float value = data.getmValue();
            canvas.translate(mHistograSpacing + mHistograWidth, 0);

            touchX -= (mHistograSpacing + mHistograWidth);

            canvas.drawRect(0, maxY * value / mMaxValue, mHistograWidth, 0, mHistogramPaint);
            canvas.drawText(data.getmTitle(), 0, line_padding_buttom / 2, mTextPaint);

            if (touchX <= mHistograSpacing + mHistograWidth && touchX > 0) {
                canvas.drawRect(0, maxY * value / mMaxValue, mHistograWidth, 0, mTextPaint);
                canvas.drawText("" + value,0,maxY * value / mMaxValue - 10,mTextPaint);
            }

        }
        canvas.restore();
    }

    @Override
    protected ISlide setSlide() {
        return new MySlide();
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setTextSize(30);

        mHistogramPaint = new Paint();
        mHistogramPaint.setStyle(Paint.Style.FILL);
        mHistogramPaint.setStrokeCap(Paint.Cap.ROUND);
        mHistogramPaint.setColor(mHistorgramColer);
    }

    public void setHistogramDatas(List<HistogramData> datas) {
        this.mHistogramDatas.addAll(datas);
        for (HistogramData data : mHistogramDatas) {
            if (mMaxValue < data.getmValue()) {
                mMaxValue = data.getmValue();
            }
        }
        if (mChildCanvasSize == null) {
            mChildCanvasSize = new ChildCanvasSize(0, 900, 0, 500);
        }
        mChildCanvasSize.higX = (mHistograSpacing + mHistograWidth) * mHistogramDatas.size();
    }

    private class MySlide implements ISlide {

        private float x;
        private float y;
        private float mSlideX = 0;
        private float mSlideY = 0;
        private float mTouchX = 100000000;
        private float mTouchY = 100000000;
        private float starX;
        private float starY;

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
                } else if (mSlideX < (mCanvasSize.width - line_padding_right - line_padding_left) - mChildCanvasSize.higX) {
                    mSlideX = (mCanvasSize.width - line_padding_left - line_padding_right) - mChildCanvasSize.higX;
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
        public void onTouch(MotionEvent event) {
            int action = event.getAction();


            if (action == MotionEvent.ACTION_DOWN) {
                starX = event.getX();
                starY = event.getY();

            }

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_MOVE) {
                float newX = event.getX();
                float newY = event.getY();

                if (Math.abs(newX - starX) < 5 && Math.abs(newY - starY) < 5) {
                    mTouchX = starX - mSlideX;
                    invalidate();
                } else {
                    mTouchX = 100000000;
                    mTouchY = 100000000;
                }
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

        @Override
        public float getTouchX() {
            Log.e("mTouchX", "" + mTouchX);
            return this.mTouchX;
        }

        @Override
        public float getTouchY() {
            return this.mTouchY;
        }
    }

    private static class CanvasSize {   //画布大小

        final int width;
        final int height;

        public CanvasSize(Canvas canvas) {
            width = canvas.getWidth();
            height = canvas.getHeight();
        }
    }

    private static class ChildCanvasSize {  //滑动子组件的宽度
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

    public static class HistogramData {  //柱状图的数据类
        private String mTitle;
        private float mValue;

        public HistogramData(String mTitle, float mValue) {
            this.mTitle = mTitle;
            this.mValue = mValue;
        }

        public String getmTitle() {
            return mTitle;
        }

        public float getmValue() {
            return mValue;
        }
    }
}
