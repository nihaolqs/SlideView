package demo.lqs.com.slideviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lin on 2017/3/4.
 * 可滑动View的基类
 */

public abstract class AbsSlideView extends View {

//    private float mSlideX;
//    private float mSlideY;

    protected ISlide mSlide;
    protected SlideOrientation mOrientation = SlideOrientation.AllOrientation; //默认可以全方向滑动

    public AbsSlideView(Context context) {
        super(context);
        mSlide = setSlide();
    }

    public AbsSlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mSlide = setSlide();
    }

    @Override
    protected void onDraw(Canvas canvas) {


//        canvas.drawColor(0xff0000);

        onDrawSlideChildView(canvas, mSlide);



    }

    protected abstract void onDrawSlideChildView(Canvas canvas, ISlide slide);

    protected abstract ISlide setSlide();

    public void setOrientation(SlideOrientation orientation){
        this.mOrientation = orientation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.e("action",action + "");
        mSlide.onMove(event);
        mSlide.onTouch(event);
        if (action == 0) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }

    }

    public static enum SlideOrientation{
        Horizontal,Vertical,AllOrientation
    }
}
