package com.lzjs.uappoint.act;

/**
 * @description 场地列表
 * @author shalei
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.Venue;
import com.lzjs.uappoint.myview.ExpandTabView;
import com.lzjs.uappoint.myview.ViewLeft;
import com.lzjs.uappoint.myview.ViewRight;
import com.lzjs.uappoint.util.CheckConnectionUtils;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VenueListActivity extends Activity  implements PullToRefreshView.OnFooterRefreshListener{

    public static final String TAG = "VenueListActivity";
    private GridView venueGridView;
    private List<Venue> venueList=new ArrayList<>();
    private VqlistAdapter vqlistAdapter;
    private ExpandTabView expandTabView_are;
    private ExpandTabView expandTabView_stor;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ArrayList<View> sViewArray = new ArrayList<View>();//默认排序
    private ViewLeft viewLeft;
    private ViewRight viewLeft_stor;
    //返回按钮
    private ImageView backImageView;
    //预约按钮
    private Button orderButton;

    private String menuId;
    private String infoCounty="";
    private static OkHttpClient client = new OkHttpClient();


    /**
     * 在这里直接设置连接超时，静态方法内，在构造方法被调用前就已经初始话了
     */
    /*static {
        client.newBuilder().connectTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(100, TimeUnit.SECONDS);
    }*/

    private Request request;
    private String[] areItems;
    private String[] areItemsVal;
    private String[] items = new String[] { "销量从高到低", "销量从低到高"};//显示字段
    private String[] itemsVaule = new String[] {  "00A", "00B"};//隐藏id

    private String storId;
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;

    //
    @ViewInject(R.id.venue_list_pull_view)
    private PullToRefreshView mPullToRefreshView;
    private int mIndex = 0;
    private int mRefreshIndex = 1;
    private String cityName1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_list);
        ViewUtils.inject(this);
        menuId=getIntent().getStringExtra("menuId");

        String cityName=getIntent().getStringExtra("menuName");
        center_title_text.setText(cityName);
        initView();
        cityName1=SharedUtils.getCityName(getBaseContext());
        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getApplicationContext())){
            getSearchData();
            getStorData();
            getData("", "");
        }else {
            Toast.makeText(VenueListActivity.this, "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
        }
        //
//        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(getTime());
        mPullToRefreshView.setEnablePullTorefresh(false);
        mPullToRefreshView.setEnablePullLoadMoreDataStatus(true);

        vqlistAdapter = new VqlistAdapter(this,venueList);
        venueGridView = (GridView) findViewById(R.id.venue_list_home_gridView);
        venueGridView.setNumColumns(1);
        venueGridView.setAdapter(vqlistAdapter);

        backImageView = (ImageView) findViewById(R.id.title3_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 数据查询条件框初始化
     */
    private void initView() {
        expandTabView_are = (ExpandTabView) findViewById(R.id.expandtab_view_are_ve);
        expandTabView_stor = (ExpandTabView) findViewById(R.id.expandtab_view_sort_ve);


    }

    private void getStorData(){
        viewLeft_stor=new ViewRight(this,items,itemsVaule);
        initVauleRight();
        initListenerRight();
    }

    /**
     * 数据查询条件框赋值
     */
    private void initVauleLeft() {
        mViewArray.add(viewLeft);
        // sViewArray.add(viewLeft_stor);
        ArrayList<String> mTextArray = new ArrayList<String>();
        // ArrayList<String> sTextArray = new ArrayList<String>();

        mTextArray.add("区域");
        //sTextArray.add("默认排序");
        expandTabView_are.setValue(mTextArray, mViewArray);
        //expandTabView_stor.setValue(sTextArray, sViewArray);


    }
    private void initVauleRight() {
        // mViewArray.add(viewLeft);
        sViewArray.add(viewLeft_stor);
        // ArrayList<String> mTextArray = new ArrayList<String>();
        ArrayList<String> sTextArray = new ArrayList<String>();

        //mTextArray.add("区域");
        sTextArray.add("默认排序");
        //expandTabView_are.setValue(mTextArray, mViewArray);
        expandTabView_stor.setValue(sTextArray, sViewArray);


    }


    /**
     * 获取数据
     */
    private void getData(final String infoCounty, final String numOrderBy){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("menuids", menuId)
                            .add("pageNo", mIndex+"")
                            .add("infoCounty", infoCounty)
                            .add("orderNum", numOrderBy)
                            .add("cityName",cityName1)
                            .build();
                    Log.e(TAG,"mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"+menuId);
                    request = new Request.Builder().url(Contants.GET_MERCHANT_INFO_LIST).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.e(TAG, "initialize menu: " + result);
                    if(!result.isEmpty() && result.indexOf("}")!=-1){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata=obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("data");
                        if(!objArray.isEmpty()) {
                            Venue venue1 = null;
                            for (int i = 0; i < objArray.size(); i++) {
                                venue1 = JSON.parseObject(objArray.get(i).toString(), Venue.class);
                                venueList.add(venue1);
                            }
//                      
                        }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    vqlistAdapter.notifyDataSetChanged();
                                }

                            });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getSearchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("cityName", SharedUtils.getCityName(getBaseContext()))
                            .build();
                    request = new Request.Builder().url(Contants.GET_AREAS_BYCITY).post(formBody).build();
//                    request = new Request.Builder().url(Contants.GET_AREA_TYPE).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.i(TAG, "initialize menu: -----------------" + result);
                    if(!result.isEmpty() && result.indexOf("}")!=-1){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata=obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("datas");
                        if(!objArray.isEmpty()){
                            int length = objArray.size();
                            areItems=new String[length];
                            areItemsVal=new String[length];
                            for(int i = 0; i < length; i++){
                                areItems[i]=objArray.getJSONObject(i).getString("areaName");
                                areItemsVal[i]=objArray.getJSONObject(i).getString("areaId");
                            }

                        }else{
                            areItems=new String[0];
                            areItemsVal=new String[0];
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewLeft = new ViewLeft(getBaseContext(),areItems,areItemsVal);
                            initVauleLeft();
                            initListenerLeft();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initListenerLeft() {

        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefreshLeft(viewLeft, showText);
            }
        });

    }
    private void initListenerRight() {


        viewLeft_stor.setOnSelectListener(new ViewRight.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefreshRight(viewLeft_stor, showText);
            }
        });

    }

    private void onRefreshRight(View view, String showText) {
        mIndex=0;

        expandTabView_stor.onPressBack();

        int position2 = getPositon2(view,sViewArray);
        if (position2 >= 0 && !expandTabView_stor.getTitle(position2).equals(showText)) {
            expandTabView_stor.setTitle(showText, position2);
            storId=itemsVaule[position2];

            if("销量从高到低".equals(showText)){
                storId="00A";
            }else if ("销量从低到高".equals(showText)){
                storId="00B";
            }

            Log.e(TAG,"444444444444444"+storId);
            venueList.clear();
            getData(infoCounty, storId);
        }
        //Toast.makeText(MainActivity.this, showText, Toast.LENGTH_SHORT).show();

    }
    private void onRefreshLeft(View view, String showText) {
        mIndex=0;
        expandTabView_are.onPressBack();

        int position = getPositon(view, mViewArray);

        if (position >= 0 && !expandTabView_are.getTitle(position).equals(showText)) {
            expandTabView_are.setTitle(showText, position);
            infoCounty=showText;
            venueList.clear();
            getData(infoCounty, "");

        }
        //Toast.makeText(MainActivity.this, showText, Toast.LENGTH_SHORT).show();

    }

    private int getPositon(View tView,ArrayList<View> arrayList) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (arrayList.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }
    private int getPositon2(View tView,ArrayList<View> arrayList) {
        for (int j = 0; j < sViewArray.size(); j++) {
            if (arrayList.get(j) == tView) {
                return j;
            }
        }

        return -1;
    }
    public  void navMenuBtnClick(View view){
        if("nav_find".equals(view.getTag())){
            startActivity(new Intent(getBaseContext(), EquipmentActivity.class));
        }
        if("nav_mine".equals(view.getTag())){
            startActivity(new Intent(getBaseContext(), MineActivity.class));
        }
    }

    private String getTime(){
        return  new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        h.sendEmptyMessage(1);
        mIndex=++mRefreshIndex;
        getData(infoCounty, "");
    }

//    @Override
//    public void onHeaderRefresh(PullToRefreshView view) {
//        h.sendEmptyMessage(2);
//    }
    Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                h.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.onFooterRefreshComplete();
                    }
                }, SystemClock.uptimeMillis() + 1000);
//            } else if (msg.what == 2) {
//                h.postAtTime(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPullToRefreshView.onHeaderRefreshComplete();
//                    }
//                }, SystemClock.uptimeMillis() + 1000);
            }
        }
    };

    private class VqlistAdapter extends BaseAdapter {
        private Context context;
        List<Venue> venueList;
        private String merchantId;

        public final class ListItemView {                //自定义控件集合
            public ImageView image;
            public TextView venueIntr;//
        }
        public VqlistAdapter ( Context context,List<Venue> list){
            this.context = context;
            this.venueList = list;
        }
        @Override
        public int getCount() {
            return venueList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //自定义视图
            ListItemView listItemView = null;
            if(convertView == null){
                listItemView = new ListItemView();
                convertView = LayoutInflater.from(context).inflate(R.layout.act_item,null);
                listItemView.image = (ImageView) convertView.findViewById(R.id.arcimgView);
                listItemView.venueIntr = (TextView) convertView.findViewById(R.id.arctital);
                convertView.setTag(listItemView);
            }else
                listItemView = (ListItemView) convertView.getTag();
            Log.e(TAG, "position:" + position);
            ImageLoader.getInstance().displayImage(venueList.get(position).getMerchantPic(), listItemView.image);
            listItemView.venueIntr.setText(venueList.get(position).getMerchantName() + "");

            //注册点击事件
            listItemView.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    merchantId=venueList.get(position).getMerchantId();
                    Intent intent = new Intent(getApplicationContext(), MerchantInfoActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("merchantId", merchantId);
                    Log.e("2222222222222222", "merchantId" + merchantId);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

}
