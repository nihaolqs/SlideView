package demo.lqs.com.slideviewlibrary;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by lin on 2017/3/4.
 * 移动的方法的接口
 */

public interface ISlide {

    void onMove(MotionEvent event);

    void onTouch(MotionEvent event);

    float getSlideX();

    float getSlideY();

    float getTouchX();

    float getTouchY();
}
