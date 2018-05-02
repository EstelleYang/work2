package com.lzjs.uappoint.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.TopnewsListAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.bean.Doctor;
import com.lzjs.uappoint.fresco.FrescoMainActivity;
import com.lzjs.uappoint.idcardscan.IdCardMainActivity;
import com.lzjs.uappoint.magicimage.MagicMainActivity;
import com.lzjs.uappoint.myview.ChannelInfoBean;
import com.lzjs.uappoint.myview.CommonAdapter;
import com.lzjs.uappoint.myview.GridViewGallery;
import com.lzjs.uappoint.myview.GridViewGallerySpecial;
import com.lzjs.uappoint.myview.ViewHolder;
import com.lzjs.uappoint.util.CheckConnectionUtils;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DisplayUtil;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.TimeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;
import lzjs.com.picplayer.CycleViewPager;
import lzjs.com.picplayer.bean.ADInfo;
import lzjs.com.picplayer.utils.ViewFactory;
import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    private static final String TAG = "MainActivity";

    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ImageView> views_zt = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;
    private PullToRefreshView mPullToRefreshView;

    private GridView gvgnzj;
    private GridView gvsnzj;
    private GridView gvzlzj;

    private List<Doctor> gnDoctorList;
    private List<Doctor> snDoctorList;
    private List<Doctor> zlDoctorList;
    private ArrayList<String> items = new ArrayList<String>();

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isLocation;
    private String cityName;//当前城市名称
    private String choiceCityName;
    private String locationCityName;

    //搜索按钮
    private ImageView search_button ;
    private TextView topCity;
    private Toolbar mToolbar;
    //
    private LinearLayout mLinearLayout;

    private static OkHttpClient client = new OkHttpClient();
    private long firstTime=0;

    /**
     * 在这里直接设置连接超时，静态方法内，在构造方法被调用前就已经初始话了
     */
    static {
        client.newBuilder().connectTimeout(3000, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(3000, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(3000, TimeUnit.SECONDS);
    }

    private Request request;
    private Request request_sn;
    private Request request_gn;
    private Request request_zl;

    private GridViewGallery mGallery;//菜单滚动部分
    private GridViewGallerySpecial specialGallay;
    private List<ChannelInfoBean> menulist;
    private List<ChannelInfoBean> speciallist;
    private static final int RG_REQUEST = 0; //判断回调函数的值
    private String exitTime;
    private String city;

    private int mIndex = 1;
    public static MainActivity MainActivity_instence=null;

    private ListView topnewsListView;
    private List<Article> topnewsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(mToolbar);
        /*final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }*/

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, SelectCityActivity.class));
            }
        });

        mToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_menu));

        MainActivity_instence=this;
        //topCity = (TextView) findViewById(R.id.home_arear_text);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");

        //定位开始
        mLocationOption=new AMapLocationClientOption();
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        isLocation = SharedUtils.getWelcomeBoolean(this);
        choiceCityName = SharedUtils.getCityName(this);
        locationCityName = SharedUtils.getLocationCityName(this);
        if (isLocation) {
            if (!choiceCityName.isEmpty()) {
                //topCity.setText(choiceCityName);
                mToolbar.setTitle(choiceCityName);
            }
            /*mLocationClient = ((ExitApplication) getApplication()).locationClient;
            ((ExitApplication) getApplication()).mLocationResult = topCity;
            ((ExitApplication) getApplication()).cityName = choiceCityName;
            //initLocation();
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    judgeLocationCity();
                }
            }, 3000);*/
        } else {
            Log.e("choiceCityName","choiceCityName:"+city);
            if (null != city){
                //topCity.setText(city);
                mToolbar.setTitle(city);
            }else {
                //topCity.setText(choiceCityName);
                mToolbar.setTitle(choiceCityName);
            }
        }
        exitTime = TimeHelper.addMinute(TimeHelper.getCurrentTime(),-10);
        //定位结束
//        configImageLoader();
        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getApplicationContext())){
            File sdcache = getExternalCacheDir();
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            client.newBuilder().cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
            initialize();
            initView();
            initDoctorListGN();
            initDoctorListSN();
            initDoctorListZL();
            getTopnewsData();
        }else {
            Toast.makeText(MainActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //使上下文菜单项图标显示
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e("exception:", getClass().getSimpleName() + "onMenuOpened...unable to set icons for overflow menu" + e);
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(getBaseContext(),"已签到！"+item.getItemId(),Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.action_sign:
                Toast.makeText(getBaseContext(),"已签到！",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 省内专家
     */
    private void initDoctorListSN() {
        snDoctorList=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("cityName", choiceCityName)
                            .add("pageNo", mIndex + "")
                            .build();
                    request_sn = new Request.Builder().url(Contants.HOME_SNZJ_LIST).post(formBody).build();
                    Response response = client.newCall(request_sn).execute();
                    String res=response.body().string();
                    Log.i(TAG, "initArticle run: " + res);
                    if(response.isSuccessful()&&!res.isEmpty() && res.indexOf("}")!=-1){
                        JSONObject obj= JSONObject.parseObject(res);
                        if(obj.getString("result").equals("200")){
                            JSONObject objdata=obj.getJSONObject("response");
                            JSONArray objArray=objdata.getJSONArray("datas");
                            int length = objArray.size();
                            if(objArray != null && length > 0){
                                Doctor doctor=null;
                                for(int i = 0; i < length; i++){
                                    doctor = new Doctor();
                                    doctor.setUsername(objArray.getJSONObject(i).getString("username"));
                                    doctor.setHeadimage(objArray.getJSONObject(i).getString("headimage"));
                                    doctor.setTitlename(objArray.getJSONObject(i).getString("titlename"));
                                    doctor.setIntroduce(objArray.getJSONObject(i).getString("introduce"));
                                    doctor.setUserid(objArray.getJSONObject(i).getString("userid"));
                                    doctor.setHisname(objArray.getJSONObject(i).getString("hisname"));
                                    snDoctorList.add(doctor);
                                }
                            }
                        }
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //省内专家
                            gvsnzj.setNumColumns(3);
                            gvsnzj.setHorizontalSpacing(10);
                            gvsnzj.setVerticalSpacing(10);
                            gvsnzj.setAdapter(new CommonAdapter<Doctor>(getApplication(),R.layout.layout_gridview_item, snDoctorList) {
                                @Override
                                protected void convert(ViewHolder viewHolder, final Doctor item, int position) {
                                    viewHolder.setText(R.id.textView_ItemText,item.getUsername());
                                    viewHolder.setText(R.id.textView_ItemText_1,item.getTitlename());
                                    viewHolder.setText(R.id.tv_hisName,item.getHisname());
                                    //viewHolder.setText(R.id.tv_introduce,item.getIntroduce());
                                    ImageView imageView=(ImageView) viewHolder.getView(R.id.imageView_ItemImage);
                                    ImageLoader.getInstance().displayImage(item.getHeadimage(), imageView, DisplayUtil.getOptions());
                                    viewHolder.setOnClickListener(R.id.imageView_ItemImage, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, ExpertsDetailActivity.class);
                                            intent.putExtra("expertId",item.getUserid()+"");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                            //设置scrollView滚动至最顶端
                            ScrollView sv = (ScrollView) findViewById(R.id.home_sv);
                            sv.smoothScrollTo(0, 0);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //国内专家
    public void initDoctorListGN() {
        gnDoctorList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("cityName", choiceCityName)
                            .add("pageNo", mIndex + "")
                            .build();
                    request_gn = new Request.Builder().url(Contants.HOME_GNZJ_LIST).post(formBody).build();
                    Response response = client.newCall(request_gn).execute();
                    String res=response.body().string();
                    Log.i(TAG, "initArticle run: " + res);
                    if(response.isSuccessful()&&!res.isEmpty() && res.indexOf("}")!=-1){
                        JSONObject obj= JSONObject.parseObject(res);
                        if(obj.getString("result").equals("200")){
                            JSONObject objdata=obj.getJSONObject("response");
                            JSONArray objArray=objdata.getJSONArray("datas");
                            int length = objArray.size();
                            if(objArray != null && length > 0){
                                Doctor doctor=null;
                                for(int i = 0; i < length; i++){
                                    doctor = new Doctor();
                                    doctor.setUsername(objArray.getJSONObject(i).getString("username"));
                                    doctor.setHeadimage(objArray.getJSONObject(i).getString("headimage"));
                                    doctor.setTitlename(objArray.getJSONObject(i).getString("titlename"));
                                    doctor.setIntroduce(objArray.getJSONObject(i).getString("introduce"));
                                    doctor.setUserid(objArray.getJSONObject(i).getString("userid"));
                                    doctor.setHisname(objArray.getJSONObject(i).getString("hisname"));
                                    gnDoctorList.add(doctor);
                                }
                            }
                        }
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gvgnzj.setNumColumns(3);
                            gvgnzj.setHorizontalSpacing(5);
                            gvgnzj.setVerticalSpacing(10);
                            gvgnzj.setAdapter(new CommonAdapter<Doctor>(getApplication(),R.layout.layout_gridview_item, gnDoctorList) {
                                @Override
                                protected void convert(ViewHolder viewHolder, final Doctor item, int position) {
                                    viewHolder.setText(R.id.textView_ItemText,item.getUsername());
                                    viewHolder.setText(R.id.textView_ItemText_1,item.getTitlename());
                                    viewHolder.setText(R.id.tv_hisName,item.getHisname());
                                    //viewHolder.setText(R.id.tv_introduce,item.getIntroduce());
                                    ImageView imageView=(ImageView) viewHolder.getView(R.id.imageView_ItemImage);
                                    ImageLoader.getInstance().displayImage(item.getHeadimage(), imageView, DisplayUtil.getOptions());
                                    viewHolder.setOnClickListener(R.id.imageView_ItemImage, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, ExpertsDetailActivity.class);
                                            intent.putExtra("expertId",item.getUserid()+"");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                            //设置scrollView滚动至最顶端
                            ScrollView sv = (ScrollView) findViewById(R.id.home_sv);
                            sv.smoothScrollTo(0, 0);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //肿瘤专家
    public void initDoctorListZL() {
        zlDoctorList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("cityName", choiceCityName)
                            .add("pageNo", mIndex + "")
                            .build();
                    request_zl = new Request.Builder().url(Contants.HOME_ZLZJ_LIST).post(formBody).build();
                    Response response = client.newCall(request_zl).execute();
                    String res=response.body().string();
                    Log.i(TAG, "initArticle run: " + res);
                    if(response.isSuccessful()&&!res.isEmpty() && res.indexOf("}")!=-1){
                        JSONObject obj= JSONObject.parseObject(res);
                        if(obj.getString("result").equals("200")){
                            JSONObject objdata=obj.getJSONObject("response");
                            JSONArray objArray=objdata.getJSONArray("datas");
                            int length = objArray.size();
                            if(objArray != null && length > 0){
                                Doctor doctor=null;
                                for(int i = 0; i < length; i++){
                                    doctor = new Doctor();
                                    doctor.setUsername(objArray.getJSONObject(i).getString("username"));
                                    doctor.setHeadimage(objArray.getJSONObject(i).getString("headimage"));
                                    doctor.setTitlename(objArray.getJSONObject(i).getString("titlename"));
                                    doctor.setIntroduce(objArray.getJSONObject(i).getString("introduce"));
                                    doctor.setUserid(objArray.getJSONObject(i).getString("userid"));
                                    doctor.setHisname(objArray.getJSONObject(i).getString("hisname"));
                                    zlDoctorList.add(doctor);
                                }
                            }
                        }
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gvzlzj.setNumColumns(3);
                            gvzlzj.setHorizontalSpacing(5);
                            gvzlzj.setVerticalSpacing(10);
                            gvzlzj.setAdapter(new CommonAdapter<Doctor>(getApplication(),R.layout.layout_gridview_item, zlDoctorList) {
                                @Override
                                protected void convert(ViewHolder viewHolder, final Doctor item, int position) {
                                    viewHolder.setText(R.id.textView_ItemText,item.getUsername());
                                    viewHolder.setText(R.id.textView_ItemText_1,item.getTitlename());
                                    viewHolder.setText(R.id.tv_hisName,item.getHisname());
                                    //viewHolder.setText(R.id.tv_introduce,item.getIntroduce());
                                    ImageView imageView=(ImageView) viewHolder.getView(R.id.imageView_ItemImage);
                                    ImageLoader.getInstance().displayImage(item.getHeadimage(), imageView, DisplayUtil.getOptions());
                                    viewHolder.setOnClickListener(R.id.imageView_ItemImage, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, ExpertsDetailActivity.class);
                                            intent.putExtra("expertId",item.getUserid()+"");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                            //设置scrollView滚动至最顶端
                            ScrollView sv = (ScrollView) findViewById(R.id.home_sv);
                            sv.smoothScrollTo(0, 0);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 首页轮播图
     */
    @SuppressLint("NewApi")
    private void initialize() {
        menulist = new ArrayList<>();
        speciallist=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request = new Request.Builder().url(Contants.HOME_CAROUSEL_PICS).build();
//                    request = new Request.Builder().url(Contants.GET_GUANG).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.i(TAG, "initialize run: "+response.isSuccessful() + result);
                    if(!result.isEmpty() && !result.equals("noData")){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata=obj.getJSONObject("response");
                        JSONArray objArray = objdata.getJSONArray("datas");
                        int length = objArray.size();
                        if(objArray != null && length > 0){
                            ADInfo info =null;
                            for(int i = 0; i < length; i++){
                                info = new ADInfo();
                                info.setUrl(objArray.getJSONObject(i).getString("imgPath"));
                                info.setId(objArray.getJSONObject(i).getString("advertId"));
                                info.setContent("图片-->" + i);
                                infos.add(info);
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cycleViewPager = (CycleViewPager) getFragmentManager()
                                    .findFragmentById(R.id.fragment_cycle_viewpager_content);
                            if (infos.size() > 0) {
                                Log.d("MainActivity", "hello");
                                // 将最后一个ImageView添加进来
                                views.add(ViewFactory.getImageView(getBaseContext(), infos.get(infos.size() - 1).getUrl()));
                                for (int i = 0; i < infos.size(); i++) {
                                    views.add(ViewFactory.getImageView(getBaseContext(), infos.get(i).getUrl()));
                                }
                                // 将第一个ImageView添加进来
                                views.add(ViewFactory.getImageView(getBaseContext(), infos.get(0).getUrl()));
                                // 设置循环，在调用setData方法前调用
                                cycleViewPager.setCycle(true);

                                // 在加载数据前设置是否循环
                                cycleViewPager.setData(views, infos, mAdCycleViewListener);
                                //设置轮播
                                cycleViewPager.setWheel(true);

                                // 设置轮播时间，默认5000ms
                                //cycleViewPager.setTime(2000);
                                //设置圆点指示图标组居中显示，默认靠右
                                cycleViewPager.setIndicatorCenter();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //初始化
    private void initView() {
        gvgnzj=(GridView) findViewById(R.id.gv_list_gnzj);
        gvsnzj=(GridView) findViewById(R.id.gv_list_snzj);
        gvzlzj=(GridView) findViewById(R.id.gv_list_zlzj);
        search_button = (ImageView) findViewById(R.id.search_button);

        topnewsListView= (ListView) findViewById(R.id.lv_topnews);

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(getTime());
        mPullToRefreshView.setEnablePullTorefresh(true);
        mPullToRefreshView.setEnablePullLoadMoreDataStatus(true);
        //让顶部继续获得焦点
        mLinearLayout = (LinearLayout)findViewById(R.id.neicount);
        mLinearLayout.requestFocus();
        mLinearLayout.setFocusable(true);
        mLinearLayout.setFocusableInTouchMode(true);

    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            Intent intent=new Intent(MainActivity.this,PublishInfoDetailActivity.class);
//                intent.putExtra("sceneId",imageView.getId());
            Bundle mbundle=new Bundle();
            mbundle.putString("sceneId",  info.getId().toString());
            mbundle.putString("FLAG","1");
            Log.e(TAG, "info.getId().toString()////////////////" + info.getId().toString());
            intent.putExtras(mbundle);
            //startActivity(intent);

        }

    };
    /**
     *专题点击
     */
    /*private void specialClick(View v,int postition) {
        Log.e(TAG,"topicId"+speciallist.get(postition).getId());
        Intent intent = null;
        switch (postition) {
            case 0:
                intent=new Intent(this,FriendsListActivity.class);
                intent.putExtra("topicId",speciallist.get(postition).getId()+"");
                intent.putExtra("flag","special");
                startActivity(intent);
                break;
            case 1:
                intent=new Intent(this,MeetingListActivity.class);
                intent.putExtra("meetingId",speciallist.get(postition).getId()+"");
                intent.putExtra("flag","special");
                startActivity(intent);
                break;
        }
        if (postition == 0){

        }
    }*/

    public void btnClick(View view) {
        int resId = view.getId();
        switch (resId) {
            case R.id.btn_friends:
                startActivity(new Intent(MainActivity.this, FriendsListActivity.class));
                break;
            case R.id.btn_meet:
                startActivity(new Intent(MainActivity.this, MeetingListActivity.class));
                break;
            case R.id.btn_pacs:
                startActivity(new Intent(MainActivity.this, PacsListActivity.class));
                break;
            case R.id.btn_msg:
                Intent intent = new Intent(MainActivity.this, FrescoMainActivity.class);
                intent.putExtra("tabId", 4);
                startActivity(intent);
                break;
            case R.id.tv_more_zl:
                startActivity(new Intent(MainActivity.this, ExpertActivity.class));
                break;
            case R.id.btn_fav:
                startActivity(new Intent(MainActivity.this, CollectionArticleListActivity.class));
                break;
            case R.id.btn_hos:
                startActivity(new Intent(MainActivity.this, AcceptAmountActivity.class));
                break;
            case R.id.btn_more:
                //startActivity(new Intent(MainActivity.this, MagicMainActivity.class));
                //startActivity(new Intent(MainActivity.this, IdCardMainActivity.class));
                break;
        }
    }

    /**
     * 首页头部标题栏左侧按钮点击事件
     *
     * @param view
     */
    /*public void homeTopLeftBtnClick(View view) {
        startActivity(new Intent(getBaseContext(), SelectCityActivity.class));
        //Toast.makeText(MainActivity.this, "首页标题栏左侧菜单被点击了!", Toast.LENGTH_SHORT).show();
    }*/

    /**
     * 首页头部标题栏右侧按钮点击事件
     *
     * @param view
     */
    public void getMapBtnClick(View view) {
        Intent intent = new Intent(getBaseContext(),SearchViewActivity.class);
        intent.putExtra("flag","1");//代表搜索全部
        //startActivity(intent);
    }

    /**
     * 菜单按钮点击事件
     * @param view
     */
    public void menuBtnClick(View view) {
        Log.i(TAG, "menuBtnClick: " + view.getTag(R.id.menu_bottom));
        Intent intent = null;
        Bundle mBundle = new Bundle();
        if (view!=null) {
            mBundle.putString("parMenuId", view.getTag(R.id.menu_bottom).toString());
        }
        //支持JDK 1.8
        if ("0".equals(view.getTag().toString())) {
            intent=new Intent(this, EquipmentActivity.class);
        }
        if ("1".equals(view.getTag().toString())) {
            intent=new Intent(getBaseContext(), EquipmentActivity.class);
        }
        if ("2".equals(view.getTag().toString())) {
            intent=new Intent(getBaseContext(), EquipmentActivity.class);
        }
        if ("3".equals(view.getTag().toString())) {
            intent=new Intent(getBaseContext(), EquipmentActivity.class);
        }
        if ("4".equals(view.getTag().toString())) {
            intent=new Intent(getBaseContext(), EquipmentActivity.class);
        }
        if ("5".equals(view.getTag().toString())) {
            intent=new Intent(getBaseContext(), EquipmentActivity.class);
        }
        if ("6".equals(view.getTag().toString())) {
            intent=new Intent(getBaseContext(), EquipmentActivity.class);
        }
        if ("7".equals(view.getTag().toString())) {
            intent=new Intent(getBaseContext(), EquipmentActivity.class);
        }

        if(intent!=null){
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

    public void navMenuBtnClick(View view) {
        if ("nav_find".equals(view.getTag())) {
            startActivity(new Intent(getBaseContext(), LearningActivity.class));
        }
        if ("nav_mine".equals(view.getTag())) {
            startActivity(new Intent(getBaseContext(), MineActivity.class));
        }
        if ("nav_pacs".equals(view.getTag())) {
            //startActivity(new Intent(getBaseContext(), FrescoMainActivity.class));
            startActivity(new Intent(getBaseContext(), PacsListActivity.class));
        }
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ********************************************");
        h.sendEmptyMessage(2);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        /* 上拉加载更多 */
        h.sendEmptyMessage(1);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
    /* 下拉刷新数据 */
        h.sendEmptyMessage(2);
        views.clear();
        views_zt.clear();
        infos.clear();
        speciallist.clear();
        items.clear();
        topnewsList.clear();
        initialize();
        initDoctorListGN();
        initDoctorListSN();
        initDoctorListZL();
        getTopnewsData();
    }

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                h.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.onFooterRefreshComplete();
                    }
                }, SystemClock.uptimeMillis() + 1000);
            } else if (msg.what == 2) {
                h.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.onHeaderRefreshComplete();
                    }
                }, SystemClock.uptimeMillis() + 1000);
            }
        }
    };

 /*   private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }*/

    public void judgeLocationCity() {
        try {
            choiceCityName = SharedUtils.getCityName(this);
            locationCityName = SharedUtils.getLocationCityName(this);
            if (!choiceCityName.isEmpty() && !choiceCityName.equals(locationCityName) && !locationCityName.isEmpty()) {
                new AlertDialog.Builder(this).setTitle("系统定位到您在" + locationCityName + "，需要切换到" + locationCityName + "吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //topCity.setText(locationCityName);
                                mToolbar.setTitle(locationCityName);
                                SharedUtils.putCityName(MainActivity.this, locationCityName);
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!choiceCityName.isEmpty()) {
                                    //topCity.setText(choiceCityName);
                                    mToolbar.setTitle(choiceCityName);
                                }
                            }
                        }).show();
            } else if ("".equals(choiceCityName)) {
                SharedUtils.putCityName(this, locationCityName);
                //topCity.setText(locationCityName);
                mToolbar.setTitle(locationCityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 监听返回键
     * */
    public void onBackPressed() {
        try{
            String nowExitTime = TimeHelper.getCurrentTime();
            if( TimeHelper.getPlusTotalTime(exitTime, nowExitTime) > 2000){
                exitTime = nowExitTime;
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            }else{
                //杀死进程
                ExitApplication.getInstance().exit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private  void getTopnewsData(){
        RequestParams params = new RequestParams();
        //params.addBodyParameter("pageNo", 1+"");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Contants.TOPNEWSLISTS, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("response");
                            JSONArray jsondata = response.getJSONArray("datas");
                            Article article=null;
                            for (int i=0;i<jsondata.size();i++){
                                article = new Article();
                                article.setTital(jsondata.getJSONObject(i).getString("prititle"));
                                article.setId(jsondata.getJSONObject(i).getString("topicid"));
                                article.setUrl(jsondata.getJSONObject(i).getString("topicurl"));
                                article.setCreateData(jsondata.getJSONObject(i).getString("createdate"));
                                article.setSceneTitle(jsondata.getJSONObject(i).getString("topicabstract"));
                                topnewsList.add(article);
                            }
                        }
                        topnewsListView.setAdapter(new TopnewsListAdapter(MainActivity.this,topnewsList));
                        setListViewHeightBasedOnChildren(topnewsListView);
                        topnewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
                                Bundle mbundle = new Bundle();
                                mbundle.putString("articeId", topnewsList.get(position).getId());
                                mbundle.putString("url", topnewsList.get(position).getUrl());
                                mbundle.putString("FLAG", 1+"");
                                intent.putExtras(mbundle);
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                    }
                }
        );
    }

    //和ScrollView嵌套时 设置影像头条listview的高度 否则只显示第1行数据
    //注意： 子ListView的每个Item必须是LinearLayout，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
    private void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);
        listView.setDividerHeight(0);
    }

}
