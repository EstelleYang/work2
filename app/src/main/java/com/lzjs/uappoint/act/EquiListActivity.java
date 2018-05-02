package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.EqlistAdapter;
import com.lzjs.uappoint.bean.Venue;
import com.lzjs.uappoint.myview.ExpandTabView;
import com.lzjs.uappoint.myview.ViewLeft;
import com.lzjs.uappoint.myview.ViewMiddle;
import com.lzjs.uappoint.myview.ViewRight;
import com.lzjs.uappoint.util.CheckConnectionUtils;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;

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


/**
 * 装备列表页面
 * Created by wangdq on 2016/1/14.
 */
public class EquiListActivity extends Activity implements PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {


    //@Bind(R.id.eq_list_home_gridView)
    private ListView eqgridView;
    private List<Venue> eqlist ;
    private EqlistAdapter eqlistAdapter;
    public static final String TAG = "EquiListActivity";
    private ExpandTabView expandTabView_are;
    private ExpandTabView expandTabView_stor;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ArrayList<View> sViewArray = new ArrayList<View>();//默认排序
    private ViewLeft viewLeft;
    private ViewRight viewLeft_stor;
    private ViewMiddle viewMiddle;
    private ViewRight viewRight;
    private String menuId;
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;
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
    private String[] items = new String[]{"销量从高到低", "销量从低到高"};//显示字段
    private String[] itemsVaule = new String[]{"00A", "00B"};//隐藏id

    private String infoCounty = "";
    private String storId;
    private String cityName;
    private String menuName;

    //
    @ViewInject(R.id.equipment_list_pull_view)
    private PullToRefreshView mPullToRefreshView;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_list_home);
        ViewUtils.inject(this);
        menuId = getIntent().getStringExtra("menuId");
        menuName = getIntent().getStringExtra("menuName");
        center_title_text.setText(menuName);
        Log.i(TAG, "onCreate: " + menuId);
        //筛选条件
        initView();
        cityName = SharedUtils.getCityName(getBaseContext());
        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getApplicationContext())) {
            getSearchData();
            getStorData();
            getData(Mode.DOWN, "", "");
        } else {
            Toast.makeText(EquiListActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取数据
     */
    private void getData(final Mode mode, final String infoCounty, final String numOrderBy) {

        switch (mode) {
            case UP:
                mIndex++;
                break;
            case DOWN:
                mIndex = 0;
                break;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("menuids", menuId)
                            .add("pageNo", mIndex + "")
                            .add("cityName", cityName)
                            .add("infoCounty", infoCounty)
                            .add("orderNum", numOrderBy)
                            .build();
                    request = new Request.Builder().url(Contants.GET_MERCHANT_INFO_LIST).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.e("result", "result" + result);
                    Log.e(TAG, "initialize menu: 、、、、、、、、、、" + result);
                    if (!result.isEmpty() && result.indexOf("}") != -1) {
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata = obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("data");

                        switch (mode){

                            case DOWN:
                                eqlist.clear();
                                if (!objArray.isEmpty()) {
                                    Venue venue1 = null;
                                    for (int i = 0; i < objArray.size(); i++) {
                                        venue1 = JSON.parseObject(objArray.get(i).toString(), Venue.class);
                                        eqlist.add(venue1);
                                    }
                                }

                                break;
                            case UP:
                                List<Venue> data=new ArrayList<>();
                                if (!objArray.isEmpty()) {
                                    Venue venue1 = null;
                                    for (int i = 0; i < objArray.size(); i++) {
                                        venue1 = JSON.parseObject(objArray.get(i).toString(), Venue.class);
                                        data.add(venue1);
                                    }
                                    eqlist.addAll(data);

                                }else{

                                    h.sendEmptyMessage(3);
                                }
                                break;
                        }




                    }
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    switch (mode){
                                        case DOWN:
                                            eqlistAdapter.updateList(eqlist);
                                            break;
                                        case UP:
                                            eqlistAdapter.addList(eqlist);
                                            break;
                                    }

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getSearchData() {
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
                    if (!result.isEmpty() && result.indexOf("}") != -1) {
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata = obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("datas");
                        if (!objArray.isEmpty()) {
                            int length = objArray.size();
                            areItems = new String[length];
                            areItemsVal = new String[length];
                            for (int i = 0; i < length; i++) {
                                areItems[i] = objArray.getJSONObject(i).getString("areaName");
                                areItemsVal[i] = objArray.getJSONObject(i).getString("areaId");
                            }
                        } else {
                            areItems = new String[0];
                            areItemsVal = new String[0];
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewLeft = new ViewLeft(getBaseContext(), areItems, areItemsVal);
                            initVauleLeft();
                            initListenerleft();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView() {
        expandTabView_are = (ExpandTabView) findViewById(R.id.expandtab_view_are);
        expandTabView_stor = (ExpandTabView) findViewById(R.id.expandtab_view_sort);
        //
        mPullToRefreshView.setOnFooterRefreshListener(this);
        // mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setLastUpdated(getTime());
        mPullToRefreshView.setEnablePullTorefresh(false);
        mPullToRefreshView.setEnablePullLoadMoreDataStatus(true);
        eqgridView = (ListView) findViewById(R.id.eq_list_home_gridView);
        eqgridView.setOnItemClickListener(this);
        eqlistAdapter = new EqlistAdapter(getBaseContext());
        eqlist = new ArrayList<>();
        eqlistAdapter.updateList(eqlist);
        eqgridView.setAdapter(eqlistAdapter);


    }

    private void getStorData() {
        viewLeft_stor = new ViewRight(this, items, itemsVaule);
        initVauleRight();
        initListenerRight();
    }

    private void initVauleLeft() {
        mViewArray.add(viewLeft);

        ArrayList<String> mTextArray = new ArrayList<String>();


        mTextArray.add("区域");

        expandTabView_are.setValue(mTextArray, mViewArray);


    }

    private void initVauleRight() {

        sViewArray.add(viewLeft_stor);

        ArrayList<String> sTextArray = new ArrayList<String>();


        sTextArray.add("默认排序");

        expandTabView_stor.setValue(sTextArray, sViewArray);


    }

    private void initListenerleft() {

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

    private void onRefreshLeft(View view, String showText) {
        mIndex = 0;
        expandTabView_are.onPressBack();

        int position = getPositon(view, mViewArray);
        if (position >= 0 && !expandTabView_are.getTitle(position).equals(showText)) {
            expandTabView_are.setTitle(showText, position);
            infoCounty = showText;
            getData(Mode.DOWN,infoCounty, "");
        }


    }

    private void onRefreshRight(View view, String showText) {
        mIndex = 0;
        expandTabView_stor.onPressBack();
        int position2 = getPositon(view, sViewArray);
        if (position2 >= 0 && !expandTabView_stor.getTitle(position2).equals(showText)) {
            expandTabView_stor.setTitle(showText, position2);
            if ("销量从高到低".equals(showText)) {
                storId = "00A";
            } else if ("销量从低到高".equals(showText)) {
                storId = "00B";
            }
            getData(Mode.DOWN,infoCounty, storId);
        }


    }

    private int getPositon(View tView, ArrayList<View> arrayList) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (arrayList.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    public void navMenuBtnClick(View view) {
        if ("nav_find".equals(view.getTag())) {
            finish();
            startActivity(new Intent(getBaseContext(), EquipmentActivity.class));
        }
        if ("nav_mine".equals(view.getTag())) {
            finish();
            startActivity(new Intent(getBaseContext(), MineActivity.class));
        }
    }

    @OnClick(R.id.title3_back)
    public void backBtnClick(View view) {
        finish();
    }

    @OnClick(R.id.title3_map)
    public void mapBtnClick(View view) {
        Intent intent=new Intent(getBaseContext(), MerchantMapActivity.class);
        intent.putExtra("menuId",menuId);
        intent.putExtra("menuName",menuName);
        startActivity(intent);
        // Toast.makeText(getApplicationContext(),"你点击了地图",Toast.LENGTH_SHORT).show();
//
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        h.sendEmptyMessage(1);
        getData(Mode.UP,infoCounty, "");
    }

    //    @Override
//    public void onHeaderRefresh(PullToRefreshView view) {
//        h.sendEmptyMessage(2);
//    }
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
        h.postAtTime(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();
            }
        }, SystemClock.uptimeMillis() + 1000);

    }else if (msg.what==3){
        Toast.makeText(EquiListActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
    }
}
};


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MerchantInfoActivity.class);
        Bundle mbundle = new Bundle();
        mbundle.putString("merchantId", eqlist.get(position).getMerchantId());
        intent.putExtras(mbundle);
        startActivity(intent);
    }


    enum Mode {
        DOWN, UP
    }
}
