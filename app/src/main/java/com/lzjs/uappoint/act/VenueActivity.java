package com.lzjs.uappoint.act;

/**
 * @description 场地首页
 * @author shalei
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.myview.ChannelInfoBean;
import com.lzjs.uappoint.myview.GridViewGallery;
import com.lzjs.uappoint.util.CheckConnectionUtils;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VenueActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "VenueActivity";

    private GridViewGallery mGallery;//
    private LinearLayout mLayout;
    private List<ChannelInfoBean> list;//
    private List<Article> articleList;
    private ArrayList<String> items = new ArrayList<String>();
    private ListView listview;
    private ArticleAdapter adapter;
    //获取底部菜单栏首页对象
    private ImageView mainImageView;
    //获取底部菜单栏我的对象
    private ImageView mainMyMenu;

    //获取底部菜单栏我的对象
    @ViewInject(R.id.iv_menu_0)
    private ImageView findyMenu;
    private String parMenuId;
    private static OkHttpClient client = new OkHttpClient();

    //

    private int mIndex =1;
    private int mRefreshIndex = 1;

    private EditText search_et_input;
    private ImageView search_iv_delete;

    private int[]menus={R.drawable.blq,R.drawable.ymq,R.drawable.lq,R.drawable.yy};




    /**
     * 在这里直接设置连接超时，静态方法内，在构造方法被调用前就已经初始话了
     */
    /*static {
        client.newBuilder().connectTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(100, TimeUnit.SECONDS);
    }*/

    private Request request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(R.layout.venue);/*
        TextView textView = (TextView)findViewById(R.id.center_title_text);
        textView.setText(R.string.title_activity_venue);*/
        ViewUtils.inject(this);
        parMenuId=getIntent().getStringExtra("parMenuId");
        initView();
        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getApplicationContext())){
            //获取菜单
            getData();
            //获取推荐场景列表
            initArticle();
        }else {
            Toast.makeText(VenueActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
        }


    }
    private void initView(){
        mainImageView = (ImageView) findViewById(R.id.iv_menu_1);
        search_iv_delete= (ImageView) findViewById(R.id.search_iv_delete);
        search_et_input= (EditText) findViewById(R.id.search_et_input);
        listview= (ListView) findViewById(R.id.mainart_list_2);
        search_iv_delete.setOnClickListener(this);
        search_et_input.setOnClickListener(this);

        mainImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });

        mainMyMenu = (ImageView) findViewById(R.id.iv_menu_2);
        mainMyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MineActivity.class));
            }
        });
        findyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), FindActivity.class));
            }
        });



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
                    Log.i(TAG, "initialize menu: " + result);
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
                            mGallery = new GridViewGallery(VenueActivity.this, list);
                            mLayout = (LinearLayout) findViewById(R.id.ll_gallery_venue);
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
        mBundle.putString("menuId", id);
        mBundle.putString("menuName",name);
        intent=new Intent(getBaseContext(), VenueListActivity.class);
        if(intent!=null){
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }
    //初始化场景列表
    public void initArticle(){
        articleList=new ArrayList<Article>();

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
                    Log.i(TAG, "initialize menu: " + result);
                    if(!result.isEmpty() && result.indexOf("}")!=-1){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata=obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("datas");
                        int length = objArray.size();
                        if(!objArray .isEmpty() && length > 0){
                            Article info =null;
                            for(int i = 0; i < length; i++){
                                info = new Article();
                                info.setTital(objArray.getJSONObject(i).getString("merchantName"));
                                info.setId(objArray.getJSONObject(i).getString("merchantId"));
                                info.setImgUrl(objArray.getJSONObject(i).getString("merchantPic"));
                                articleList.add(info);
                            }
                        }else {
                            mIndex=--mRefreshIndex;
                             formBody = new FormBody.Builder()
                                    .add("merchant_type_id", parMenuId)
                                    .add("pageNo", mIndex + "")
                                    .add("cityName", SharedUtils.getCityName(getBaseContext()))
                                    .build();
                            request = new Request.Builder().url(Contants.GET_PUSH_MERCHANTS).post(formBody).build();
                            response = client.newCall(request).execute();
                            result = response.body().string();
                            if(!result.isEmpty() && result.indexOf("}")!=-1) {
                                 obj = JSONObject.parseObject(result);
                                objdata = obj.getJSONObject("Response");
                                objArray = objdata.getJSONArray("datas");
                                length = objArray.size();
                                if (objArray != null && length > 0) {
                                    Article info = null;
                                    for (int i = 0; i < length; i++) {
                                        info = new Article();
                                        info.setTital(objArray.getJSONObject(i).getString("merchantName"));
                                        info.setId(objArray.getJSONObject(i).getString("merchantId"));
                                        info.setImgUrl(objArray.getJSONObject(i).getString("merchantPic"));
                                        articleList.add(info);
                                    }
                                }
                            }

                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //listview=(ListView) findViewById(R.id.mainart_list_2);
                            adapter = new ArticleAdapter(getBaseContext(),articleList);
                            listview.setAdapter(adapter);
                            ScrollView sv = (ScrollView) findViewById(R.id.ven_home_sc);
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
     * 顶部的搜索点击
     * @param v
     */

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,SearchViewActivity.class);
        switch (v.getId()){
            case R.id.search_et_input:
                intent.putExtra("flag","2");//代表单一的专题的点击
                intent.putExtra("menuIds",parMenuId);//栏目ID
                startActivity(intent);
                break;
            case R.id.search_iv_delete:
                intent.putExtra("flag","2");//代表单一的专题的点击
                intent.putExtra("menuIds",parMenuId);//栏目ID
                startActivity(intent);
                break;
        }

    }


//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//        if(visibleItemCount+firstVisibleItem==totalItemCount){
//            mIndex++;
//            initArticle();
//        }
//    }

    private class ArticleAdapter extends BaseAdapter {

        private Context context;

        private List<Article> itemlists;

        public final class ListItemView {                //自定义控件集合
            public ImageView image;
            public TextView title;
        }

        public ArticleAdapter(Context context, List<Article> articles) {
            this.context = context;
            this.itemlists = articles;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return itemlists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            //自定义视图
            ListItemView listItemView = null;
            if (view == null) {
                listItemView = new ListItemView();
                view = LayoutInflater.from(context).inflate(R.layout.act_item, null);
                listItemView.image = (ImageView) view.findViewById(R.id.arcimgView);
                listItemView.title = (TextView) view.findViewById(R.id.arctital);
                //设置控件集到view
                view.setTag(listItemView);
            } else {
                listItemView = (ListItemView) view.getTag();
            }

            listItemView.image.setTag(itemlists.get(position).getId());
            ImageLoader.getInstance().displayImage(itemlists.get(position).getImgUrl(), listItemView.image);
            listItemView.title.setText(itemlists.get(position).getTital());


            //注册点击事件
            listItemView.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getBaseContext(), MerchantInfoActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("merchantId", v.getTag().toString());
                    mBundle.putString("flag","00A");
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    //Toast.makeText(VenueActivity.this, "场景列表被点击了!", Toast.LENGTH_SHORT).show();
                }
            });


            return view;
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
}
