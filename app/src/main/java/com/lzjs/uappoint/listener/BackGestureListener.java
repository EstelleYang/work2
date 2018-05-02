package com.lzjs.uappoint.listener;

/**
 * Created by DELL on 2017/1/19.
 */

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.lzjs.uappoint.base.BaseActivity;

/**
 * 返回手势监听接口
 */
public class BackGestureListener implements GestureDetector.OnGestureListener {



    BaseActivity activity;


    public BackGestureListener(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        if ((e2.getX() - e1.getX()) >100 && Math.abs(e1.getY() - e2.getY()) < 60 ) {
            activity.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

}

