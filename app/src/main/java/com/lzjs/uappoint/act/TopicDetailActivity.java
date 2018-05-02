package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.NoScrollGridAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.myview.CollapsibleTextView;
import com.lzjs.uappoint.myview.NoScrollGridView;
import com.lzjs.uappoint.myview.NoScrollViewPager;
import com.lzjs.uappoint.myview.Util;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DisplayUtil;
import com.lzjs.uappoint.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题详情
 */

public class TopicDetailActivity extends BaseActivity {
    private static final String TAG =TopicDetailActivity.class.getSimpleName() ;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private ImageView cursorimage;
    private static int bmpW;//横线图片宽度
    private static int offset;//图片移动的偏移量
    private TextView view1, view2;
    private int currIndex;
    private String topicId;
    @ViewInject(R.id.civ_head)
    private ImageView civ_head;
    @ViewInject(R.id.tv_fdata)
    private TextView tv_fdata;
    //@ViewInject(R.id.content)
    //private TextView tv_content;
    @ViewInject(R.id.collapsible_textview)
    private CollapsibleTextView collapsibleTextView;
    @ViewInject(R.id.tv_user_name)
    private  TextView tv_user_name;
    @ViewInject(R.id.tv_comment)
    private  TextView tv_comment;
    @ViewInject(R.id.tv_support)
    private  TextView tv_support;
    @ViewInject(R.id.tv_view_count)
    private  TextView tv_viewcount;
    private boolean clickflag=false;
    @ViewInject(R.id.gv_friends)
    private NoScrollGridView gridview;



    private Toolbar mToolbar;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        topicId=getIntent().getStringExtra("topicId");

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        tv_title=(TextView)findViewById(R.id.toolbar_name);
        mToolbar.setTitle("");
        tv_title.setText("话题详情");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewUtils.inject(this);
        mViewPager = (ViewPager) findViewById(R.id.topic_ViewPager);
        initView();
        InitImage();
        getData();
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
        mViewPager.setOnPageChangeListener(new myOnPageChangeListener());
    }

    private  void getData(){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", topicId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.QUERY_USER_QNUEN,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e(TAG, "onSuccess: " + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("response");
                            JSONObject jsonData = response.getJSONObject("data");
                            String createdate=jsonData.getString("createdate");
                            String headimage=jsonData.getString("headimage");
                            String username=jsonData.getString("username");
                            String content=jsonData.getString("content");
                            String replyCount=jsonData.getString("replyCount");
                            String voteCount=jsonData.getString("voteCount");
                            String viewCount=jsonData.getString("viewCount");
                            List<String> images=new ArrayList<String>();
                            images= JSONArray.parseArray(jsonData.getJSONArray("images").toJSONString(),String.class);
                            /**根据请求到的数据来设置几个控件的状态*/

                            /**设置姓名*/
                            if (!StringUtils.isEmpty(createdate)){
                                tv_fdata.setText(createdate);
                            }
                            /**设置职业*/
                            if (!StringUtils.isEmpty(headimage)){
                                ImageLoader.getInstance().displayImage(headimage, civ_head, DisplayUtil.getOptions());
                            }
                            if (!StringUtils.isEmpty(username)){
                                tv_user_name.setText(createdate);
                            }
                            if (!StringUtils.isEmpty(content)){
                                //tv_content.setText(content);
                                collapsibleTextView.setText(content);
                            }
                            if (images == null || images.size() == 0) { // 没有图片资源就隐藏GridView
                                gridview.setVisibility(View.GONE);
                            } else {
                                gridview.setAdapter(new NoScrollGridAdapter(getApplication(), images));
                                gridview.setVerticalSpacing(3);
                                gridview.setHorizontalSpacing(5);
                            }
                            tv_comment.setText(replyCount);
                            tv_support.setText(voteCount);
                            tv_viewcount.setText(viewCount);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getBaseContext(), "系统异常，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
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
        //collapsibleTextView = (CollapsibleTextView) findViewById(R.id.collapsible_textview);
        tv_comment=(TextView) findViewById(R.id.tv_comment);
        tv_support=(TextView) findViewById(R.id.tv_support);
        tv_viewcount=(TextView) findViewById(R.id.tv_view_count);

        view1 = (TextView) findViewById(R.id.topic_reply);
        view2 = (TextView) findViewById(R.id.topic_vote);
        view1.setOnClickListener(new TopicDetailActivity.txtListener(0));
        //view2.setOnClickListener(new TopicDetailActivity.txtListener(1));

        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TopicDetailActivity.this, BodyAuthenticateActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putBoolean("isImgShow", true);
                mbundle.putString("titleShow", "发表评论");
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });

        tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvitem = (TextView) v;
                if(clickflag){
                    v.setSelected(false);
                    tvitem.setText(String.valueOf(Integer.parseInt(tvitem.getText().toString()) - 1));
                    clickflag=false;
                }else{
                    v.setSelected(true);
                    tvitem.setText(String.valueOf(Integer.parseInt(tvitem.getText().toString()) + 1));
                    clickflag=true;
                }
            }
        });

        ReplyListActivity eqOrderFragment= (ReplyListActivity) ReplyListActivity.getInstance(topicId);
        // EqOrderFragment eqOrderFragment=new EqOrderFragment();
        //VaueOrderFragment vaueOrderFragment=VaueOrderFragment.newInstance(null,Util.getWindowWidth(this));
        //mFragments.add(vaueOrderFragment);
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
        cursorimage = (ImageView)findViewById(R.id.toppic_cursor);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bgborder);
        Bitmap newBm = scaleImg(bm , screenW/2, bm.getHeight());
        //cursorimage.setImageBitmap(newBm);
        cursorimage.setBackgroundColor(getResources().getColor(R.color.chocolate));
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
    /*
 * 返回
 */
    public void doBack(View view) {
        onBackPressed();
    }
}
