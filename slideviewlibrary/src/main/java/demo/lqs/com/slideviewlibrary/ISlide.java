package demo.lqs.com.slideviewlibrary;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by lin on 2017/3/4.
 */

public interface ISlide {
    void onMove(MotionEvent event);

    float getSlideX();

    float getSlideY();
}
