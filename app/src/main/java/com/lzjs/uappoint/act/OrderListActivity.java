package com.lzjs.uappoint.act;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.myview.NoScrollViewPager;
import com.lzjs.uappoint.myview.Util;
import com.lzjs.uappoint.util.ExitApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表页面
 * Created by wangdq on 2016/1/20
 *
 */
public class OrderListActivity extends FragmentActivity {
    private NoScrollViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private ImageView cursorimage;
    private static int bmpW;//横线图片宽度
    private static int offset;//图片移动的偏移量

    private TextView view1, view2;
    private int currIndex;


    //返回按钮
    private ImageView backImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        mViewPager = (NoScrollViewPager) findViewById(R.id.orderViewPager);
        TextView center_title_text= (TextView) findViewById(R.id.center_title_text);
        center_title_text.setText("我的订单");
        initView();
        InitImage();
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        view1.setTextColor(getResources().getColor(R.color.green));
        mViewPager.setOnPageChangeListener(new myOnPageChangeListener());


    }
    //内部类 重写TextView点击事件
    public class txtListener implements View.OnClickListener{
        private int index = 0;
        public txtListener(int i){
            index = i;
        }
        @Override
        public void onClick(View v){
            mViewPager.setCurrentItem(index);
        }
    }

    private  void initView(){
        view1 = (TextView) findViewById(R.id.tv_hot);
        view2 = (TextView) findViewById(R.id.tv_news);
        view1.setOnClickListener(new txtListener(0));
        view2.setOnClickListener(new txtListener(1));

        backImageView = (ImageView) findViewById(R.id.title1_back);
        //返回事件
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListActivity.this.finish();
            }
        });


        EqOrderFragment eqOrderFragment=EqOrderFragment.newInstance(null, Util.getWindowWidth(this));
       // EqOrderFragment eqOrderFragment=new EqOrderFragment();
        VaueOrderFragment vaueOrderFragment=VaueOrderFragment.newInstance(null,Util.getWindowWidth(this));
        mFragments.add(vaueOrderFragment);
        mFragments.add(eqOrderFragment);
    }
    public class myOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset*2 +bmpW;//两个相邻页面的偏移量
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            Log.i("aaaaaaaaaaaaa",String.valueOf(arg0));
            Log.i("one", String.valueOf(one));
            Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画
            currIndex = arg0;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒
            cursorimage.startAnimation(animation);//是用ImageView来显示动画的
            resetTabBtn();
            switch (arg0){
                case 0:
                    view1.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 1:
                    view2.setTextColor(getResources().getColor(R.color.green));
                    break;
            }
            //int i = currIndex + 1;
            // Toast.makeText(MainActivity.this, "您选择了第"+i+"个页卡", Toast.LENGTH_SHORT).show();
        }
    }
    protected void resetTabBtn(){
        view2.setTextColor(getResources().getColor(R.color.Black));
        view1.setTextColor(getResources().getColor(R.color.Black));
    }
    /*
     * 初始化图片的位移像素
     */
    public void InitImage(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        cursorimage = (ImageView)findViewById(R.id.cursor);
        Bitmap bm =BitmapFactory.decodeResource(getResources(), R.drawable.bgborder);
        Bitmap newBm = scaleImg(bm , screenW/2, bm.getHeight());
        cursorimage.setImageBitmap(newBm);
        bmpW =bm.getWidth();


        offset = (screenW/2 - bmpW)/2;
        Log.i("screenW", String.valueOf(screenW));
        Log.i("bmpW",String.valueOf(bmpW));
        Log.i("offset",String.valueOf(offset));
        //imgageview设置平移，使下划线平移到初始位置（平移一个offset）
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursorimage.setImageMatrix(matrix);
    }
    protected Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
        // 图片源
        // Bitmap bm = BitmapFactory.decodeStream(getResources()
        // .openRawResource(id));
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth1 = newWidth;
        int newHeight1 = newHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }
}
