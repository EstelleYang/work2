package com.lzjs.uappoint.act;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.ArticleAdapter;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.myview.ChannelInfoBean;
import com.lzjs.uappoint.myview.GridViewGallery;
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
 * 装备二级页面
 * Created by wangdq on 2016/1/14.
 */
public class EquipmentActivity extends Activity implements PullToRefreshView.OnFooterRefreshListener,PullToRefreshView.OnHeaderRefreshListener,View.OnClickListener,AdapterView.OnItemClickListener{
    private static final String TAG = "EquipmentActivity";
    private GridViewGallery mGallery;//
    private LinearLayout mLayout;
    private List<ChannelInfoBean> list;//
    private List<Article> articleList;

    private ListView listview;
    private ArticleAdapter adapter;
    private String parMenuId;
    private static OkHttpClient client = new OkHttpClient();


    private PullToRefreshView mPullToRefreshView;
    private int mIndex = 1;


    private EditText search_et_input;
    private ImageView search_iv_delete;
    private int[]menus={R.drawable.blq,R.drawable.ymq,R.drawable.lq,R.drawable.yy};



    /**
     * 在这里直接设置连接超时，静态方法内，在构造方法被调用前就已经初始话了
     */


    private Request request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(R.layout.equipment);
        ViewUtils.inject(this);
        parMenuId=getIntent().getStringExtra("parMenuId");
        initView();
        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getApplicationContext())){
            initArticle(MyMode.MYDOWN);
            getData();
        }else {
            Toast.makeText(EquipmentActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
        }

    }



    private void initView() {
        search_et_input= (EditText) findViewById(R.id.search_et_input);
        search_iv_delete= (ImageView) findViewById(R.id.search_iv_delete);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.equipment_pull_view);
        listview=(ListView) findViewById(R.id.mainart_list_2);
        listview.setOnItemClickListener(this);
        search_et_input.setOnClickListener(this);
        search_iv_delete.setOnClickListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setLastUpdated(getTime());
        mPullToRefreshView.setEnablePullLoadMoreDataStatus(true);
        mPullToRefreshView.setEnablePullTorefresh(false);
        articleList=new ArrayList<>();
        articleList.clear();
        adapter = new ArticleAdapter(getBaseContext());
        adapter.updateList(articleList);
        listview.setAdapter(adapter);
        ScrollView sv = (ScrollView) findViewById(R.id.eq_sz);
        sv.smoothScrollTo(0, 0);
    }

    public void getData(){
        list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("parMenuId", parMenuId)
                            .build();
                    request = new Request.Builder().url(Contants.GET_MENUS_BYPARENT).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.e(TAG, "initialize menu: " + result);
                    if(!result.isEmpty() && result.indexOf("}")!=-1){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata=obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("datas");
                        int length = objArray.size();
                        if(objArray != null && length > 0){
                            ChannelInfoBean info =null;
                            for(int i = 0; i < length; i++){
                                info = new ChannelInfoBean();
                                info.setName(objArray.getJSONObject(i).getString("menuName"));
                                info.setIconUrl(objArray.getJSONObject(i).getString("menuIcon"));
                                info.setIconID(menus[i]);
                                info.setOrder(i);
                                info.setId(objArray.getJSONObject(i).getString("menuId"));
                                final ChannelInfoBean finalInfo = info;
                                info.setOnClickListener(new ChannelInfoBean.onGridViewItemClickListener() {
                                    @Override
                                    public void ongvItemClickListener(View v) {
                                        Log.i(TAG, "ongvItemClickListener: "+v.getTag());
                                        menuBtnClick(finalInfo.getId(),finalInfo.getName());
                                    }
                                });
                                list.add(info);
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGallery = new GridViewGallery(EquipmentActivity.this, list);
                            mLayout = (LinearLayout) findViewById(R.id.ll_gallery);
                            android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            mLayout.addView(mGallery, params);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void menuBtnClick(String id,String name) {
        Intent intent = null;
        Bundle mBundle = new Bundle();
        mBundle.putString("menuId",id);
        mBundle.putString("menuName",name);
        intent=new Intent(getBaseContext(), EquiListActivity.class);
        if(intent!=null){
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }


    //初始化商家列表
    public void initArticle(final MyMode mode){
        switch (mode){
            case MYDOWN:
                mIndex=1;
                break;
            case MYUP:
                mIndex++;
                break;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("merchant_type_id", parMenuId)
                            .add("pageNo", mIndex + "")
                            .add("cityName", SharedUtils.getCityName(getBaseContext()))
                            .build();
                    request = new Request.Builder().url(Contants.GET_PUSH_MERCHANTS).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.e(TAG, "initialize menu*********: " + result);
                    if(!result.isEmpty() && result.indexOf("}")!=-1){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata=obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("datas");

                        switch (mode){
                            case MYDOWN:
                                try{
                                    articleList.clear();
                                    int length = objArray.size();
                                    if(objArray != null && length > 0){
                                        Article info =null;
                                        for(int i = 0; i < length; i++){
                                            info = new Article();
                                            info.setTital(objArray.getJSONObject(i).getString("merchantName"));
                                            info.setId(objArray.getJSONObject(i).getString("merchantId"));
                                            info.setImgUrl(objArray.getJSONObject(i).getString("merchantPic"));
                                            Log.e(TAG, "initialize menu*********: " + info.getImgUrl());
                                            articleList.add(info);
                                        }
                                    }


                                }catch (Exception e){

                                }
                                break;
                            case MYUP:
                                try{
                                    int length = objArray.size();
                                    List<Article> data=new ArrayList<>();
                                    if(objArray != null && length > 0){
                                        Article info =null;
                                        for(int i = 0; i < length; i++){
                                            info = new Article();
                                            info.setTital(objArray.getJSONObject(i).getString("merchantName"));
                                            info.setId(objArray.getJSONObject(i).getString("merchantId"));
                                            info.setImgUrl(objArray.getJSONObject(i).getString("merchantPic"));

                                            data.add(info);
                                        }
                                        articleList.addAll(data);
                                    }else {
                                        h.sendEmptyMessage(3);
                                    }


                                }catch (Exception e){

                                }
                                break;
                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            switch (mode){
                                case MYDOWN:
                                    adapter.updateList(articleList);
                                    break;
                                case MYUP:
                                    adapter.addList(articleList);
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


    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        /* 上拉加载更多 */
        h.sendEmptyMessage(1);
        initArticle(MyMode.MYUP);

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
         /* 下拉刷新数据 */

        h.sendEmptyMessage(2);
        initArticle(MyMode.MYDOWN);
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
            }else if(msg.what == 3){
                Toast.makeText(EquipmentActivity.this, "没有更多了", Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Called when a view has been clicked.
     * 头部的搜索点击
     */
    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.search_et_input:
                intent=new Intent(this,SearchViewActivity.class);
                intent.putExtra("flag","2");
                intent.putExtra("menuIds",parMenuId);
                startActivity(intent);
                break;
            case R.id.search_iv_delete:
                intent=new Intent(this,SearchViewActivity.class);
                intent.putExtra("flag","2");
                intent.putExtra("menuIds",parMenuId);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this, MerchantInfoActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("merchantId", articleList.get(position).getId());
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    public  void navMenuBtnClick(View view){
        if("nav_mine".equals(view.getTag())){
            startActivity(new Intent(getBaseContext(), MineActivity.class));
        }
        if("nav_index".equals(view.getTag())){
            startActivity(new Intent(getBaseContext(), MainActivity.class));
        }
        if("nav_find".equals(view.getTag())){
            startActivity(new Intent(getBaseContext(), FindActivity.class));
        }
    }



    /**
     *头部返回
     * @param view
     */
    @OnClick(R.id.title_search_back)
    public void backBtnClick(View view){
        finish();
    }

    enum MyMode{
        MYDOWN,MYUP
    }
}
