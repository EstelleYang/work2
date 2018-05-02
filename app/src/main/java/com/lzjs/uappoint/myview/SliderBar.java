package com.lzjs.uappoint.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lzjs.uappoint.R;

/**
 * Created by Administrator on 2015/8/25.
 */
//绘制对应的英文字母
public class SliderBar extends View {
    //new对象的时候调用
    public SliderBar(Context context) {
        super(context);
    }

    //Xml文件创建控件对象是调用
    public SliderBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    //画笔
    private Paint paint = new Paint();

    private OnTouchingLetterChangedListener letterChangedListener;

    //26个字母
    public static String[] slidBar = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    int charLength = slidBar.length;
    //选中
    private int choose = -1;
    //    //
    private TextView mTextDialog;
//private boolean showBg = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);//设置画笔颜色
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体样式
        paint.setTextSize(34);//设置字体大小
        //获取自定义View的高度和宽度
        int height = getHeight();
        int width = getWidth();
        //设定每一个字母所占控件的高度
        int eachCharHeight = (height - 20) / charLength;
        //在自定义View中绘制字母
        for (int i = 0; i < charLength; i++) {
            //
//            if(i == choose){
//                paint.setColor(getResources().getColor(R.color.red));
//                paint.setFakeBoldText(true);//粗体
//            }
            //字母所在X轴的偏移量
            float x = width / 2 - paint.measureText(slidBar[i]) / 2;
            //字母所在区域Y轴的偏移量
            float y = (1 + i) * eachCharHeight;
            canvas.drawText(slidBar[i], x, y, paint);
            //
//            paint.reset();//重置画笔
        }
    }


    //定义监听事件
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);//根据滑动位置的索引做出处理
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.letterChangedListener = onTouchingLetterChangedListener;
    }

    //
    public void setmTextDialog(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    //分发对应的Touch监听
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();//获取动作

        final float y = event.getY();//获取电机的Y坐标
        //
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = letterChangedListener;
        final int c = (int) (y / getHeight() * charLength);//获取点击y轴坐标所占总高度的比例*数组的长度就是等于数组中点击的字母索引
        switch (action) {
            case MotionEvent.ACTION_UP://抬起
                setBackgroundResource(android.R.color.transparent);
                invalidate();
//                //
                if (mTextDialog != null) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mTextDialog.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);//3秒后执行Runnable中的run方法


                }
                break;

            default:
                setBackgroundResource(R.drawable.sidebar_background);
                //setBackgroundResource(R.color.red);
                if (oldChoose != c) {
                    if (c >= 0 && c < charLength) {
                        if (listener != null) {

                            listener.onTouchingLetterChanged(slidBar[c]);
                        }
//                        //
                        if (mTextDialog != null) {
                            mTextDialog.setText(slidBar[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }
}
