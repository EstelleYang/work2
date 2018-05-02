package com.lzjs.uappoint.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.ChannelItem;
import com.lzjs.uappoint.bean.ChannelManage;
import com.lzjs.uappoint.myview.CategoryTabStrip;
import com.lzjs.uappoint.util.ExitApplication;

import java.util.ArrayList;
import java.util.List;

public class LearningActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView tv_search;
    private CategoryTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ImageView icon_category;
    /** 请求CODE */
    public final static int CHANNELREQUEST = 1;
    /** 调整返回的RESULTCODE */
    public final static int CHANNELRESULT = 10;
    /** 用户选择的新闻分类列表*/
    private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("学术", "onStart: ********************************************");
        setContentView(R.layout.activity_learning);

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_search=(TextView)findViewById(R.id.toolbar_edit);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),SearchViewActivity.class);
                intent.putExtra("flag","1");//代表搜索全部
                startActivity(intent);
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        icon_category= (ImageView) findViewById(R.id.icon_category);
        icon_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_channel = new  Intent(getApplicationContext(), ChannelActivity.class);
                startActivityForResult(intent_channel, CHANNELREQUEST);
                //startActivity(new Intent(getApplicationContext(), ChannelActivity.class));
            }
        });
        initColumnData();
        initFragment();
    }

    /** 获取Column栏目 数据*/
    private void initColumnData() {
        userChannelList =((ArrayList<ChannelItem>) ChannelManage.getManage(ExitApplication.getInstance().getSQLHelper()).getUserChannel());
    }
    private  void initFragment(){
        fragments.clear();//清空
        int count =  userChannelList.size();
        tabs.notifyDataSetChanged();
        for(int i = 0; i< count;i++){
            LearningFragment fragment=LearningFragment.newInstance(userChannelList.get(i).getId());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case CHANNELREQUEST:
                if(resultCode == CHANNELRESULT){
                    //userChannelList.clear();
                    initColumnData();
                    initFragment();
                    //adapter.notifyDataSetChanged();
                    //pager.getAdapter().notifyDataSetChanged();
                    //tabs.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void doBack(View view) {
        onBackPressed();
    }
}
