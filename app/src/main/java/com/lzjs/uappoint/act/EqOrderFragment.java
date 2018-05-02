package com.lzjs.uappoint.act;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 装备订单列表
 * Created by wangdq on 2016/1/21.
 */
public class EqOrderFragment extends Fragment{
    private FragmentPagerAdapter mAdapter;
    private ViewPager viewPager;
    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
    private ImageView cursorimage;
    private ViewPager mViewPager;
    private static int bmpW;//横线图片宽度
    private static int offset;//图片移动的偏移量
    private List<Fragment> mFragments = new ArrayList<>();
    private static int screenW;//
    private TextView view1,view2,view3,view4;
    private int currIndex;


    public EqOrderFragment() {
        super();
    }



    public static EqOrderFragment newInstance(String title,int screenW){
        EqOrderFragment fragment = new EqOrderFragment();
        EqOrderFragment.screenW =screenW;
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eq_order, container, false);
        initView(view);
        InitImage(view);
        mViewPager = (ViewPager) view.findViewById(R.id.eq_order_ViewPager);
        mAdapter=new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        view1.setTextColor(getResources().getColor(R.color.green));
        mViewPager.setOnPageChangeListener(new myOnPageChangeListener());
        return view;
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


    /**
     * 订单状态 00A 待确认  00B 成功 00C 失败
     */

    private  void initView(View view){

        view1 = (TextView) view.findViewById(R.id.vaue_order_all);
        view2 = (TextView) view.findViewById(R.id.vaue_order_yzf);
        view3= (TextView) view.findViewById(R.id.vaue_order_wzf);
        view4= (TextView) view.findViewById(R.id.vaue_order_tk);

        view1.setOnClickListener(new txtListener(0));
        view2.setOnClickListener(new txtListener(1));
        view3.setOnClickListener(new txtListener(2));
        view4.setOnClickListener(new txtListener(3));
        EqOrderListFragment vaueFragment= (EqOrderListFragment) EqOrderListFragment.getInstance("");
        EqOrderListFragment vaueFragment1=(EqOrderListFragment) EqOrderListFragment.getInstance("00B");
        EqOrderListFragment vaueFragment2=(EqOrderListFragment) EqOrderListFragment.getInstance("00A");
        EqOrderListFragment vaueFragment3=(EqOrderListFragment) EqOrderListFragment.getInstance("00I");


        mFragments.add(vaueFragment);
        mFragments.add(vaueFragment1);
        mFragments.add(vaueFragment2);
        mFragments.add(vaueFragment3);


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
            Log.i("aaaaaaaaaaaaa", String.valueOf(arg0));
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
                case 2:
                    view3.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 3:
                    view4.setTextColor(getResources().getColor(R.color.green));
                    break;

            }
        }
    }
    protected void resetTabBtn(){
        view2.setTextColor(getResources().getColor(R.color.Black));
        view1.setTextColor(getResources().getColor(R.color.Black));
        view3.setTextColor(getResources().getColor(R.color.Black));
        view4.setTextColor(getResources().getColor(R.color.Black));


    }
    /*
     * 初始化图片的位移像素
     */
    public void InitImage(View view){

        int screenW = EqOrderFragment.screenW;
        cursorimage = (ImageView)view.findViewById(R.id.eq_order_cursor);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bgborder);
        Bitmap newBm = scaleImg(bm , screenW/4, bm.getHeight());
        cursorimage.setImageBitmap(newBm);
        bmpW =bm.getWidth();


        offset = (screenW/4 - bmpW)/2;
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
