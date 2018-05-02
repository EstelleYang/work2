package com.lzjs.uappoint.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.ChannelItem;
import com.lzjs.uappoint.myview.CategoryTabStrip;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExpertActivity extends BaseActivity {

    private Toolbar mToolbar;
    private CategoryTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    /** 请求CODE */
    public final static int CHANNELREQUEST = 1;
    /** 调整返回的RESULTCODE */
    public final static int CHANNELRESULT = 10;
    /** 用户选择的新闻分类列表*/
    private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
    private ArrayList<ChannelItem> userChannelList_put;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private Request request_tabs;
    private static OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("学术", "onStart: ********************************************");
        setContentView(R.layout.activity_expert);

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        initTabData();
        initFragment();
    }

    /** 获取Column栏目 数据*/
    private void initTabData() {
        //userChannelList =((ArrayList<ChannelItem>) ChannelManage.getManage(ExitApplication.getInstance().getSQLHelper()).getUserChannel());
        //userChannelList.add(new ChannelItem("0", "全部", 0, 1));
        userChannelList = SharedUtils.loadDepartChannelList(ExpertActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request_tabs = new Request.Builder().url(Contants.DEPART_LISTS).get().build();
                    Response response = client.newCall(request_tabs).execute();
                    String res = response.body().string();
                    if (response.isSuccessful() && !res.isEmpty() && res.indexOf("}") != -1) {
                        JSONObject obj = JSONObject.parseObject(res);
                        if (obj.getString("result").equals("200")) {
                            JSONObject objdata=obj.getJSONObject("response");
                            JSONArray objArray=objdata.getJSONArray("datas");
                            int length = objArray.size();
                            if (objArray != null && length > 0){
                                ChannelItem item=null;
                                userChannelList_put = new ArrayList<ChannelItem>();
                                for(int i = 0; i < length; i++){
                                    item = new ChannelItem();
                                    item.setId(objArray.getJSONObject(i).getString("channelId"));
                                    item.setName(objArray.getJSONObject(i).getString("channelName"));
                                    item.setOrderId(i+1);
                                    item.setSelected(0);
                                    userChannelList_put.add(item);
                                }
                                //存储
                                SharedUtils.putDepartChannelList(ExpertActivity.this,userChannelList_put);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private  void initFragment(){
        fragments.clear();//清空
        int count =  userChannelList.size();
        tabs.notifyDataSetChanged();
        for (int i = 0; i< count;i++) {
            ExpertFragment fragment=ExpertFragment.newInstance(userChannelList.get(i).getId());
            fragments.add(fragment);
            tabs.addTab(i,userChannelList.get(i).getName());
        }
        adapter = new MyPagerAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private FragmentManager fm;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm=fm;
        }
        public MyPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
            super(fm);
            this.fm=fm;
            this.fragments=fragments;
        }
        @Override
        public int getCount() {
            return fragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return userChannelList.get(position).getName();
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Object obj = super.instantiateItem(container, position);
            return obj;
        }
    }

    public void doBack(View view) {
        onBackPressed();
    }
}
