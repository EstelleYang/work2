package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.MyAdapter;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.myview.ExpandTabView;
import com.lzjs.uappoint.myview.ViewLeft;
import com.lzjs.uappoint.myview.ViewRight;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import me.next.tagview.*;
/***
 * 发现页面
 * by wangdq
 * on 2016年3月9日16:44:11
 *
 */
public class FindActivity extends Activity implements PullToRefreshBase.OnRefreshListener2<ListView>,AdapterView.OnItemClickListener, TagCloudView.OnTagClickListener {
    private static final String TAG ="FindActivity" ;
    private ListView mListView;

    private ArrayAdapter<String> mAdapter;
    private MyAdapter adapter;
    private List<Article> articleList;
    private ArrayList<String> items = new ArrayList<>();
    private Handler mHandler;
    private int mIndex = 0;
    private int mRefreshIndex = 0;

    private String choiceCityName;

    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;

    @ViewInject(R.id.expandtab_view_are_ve)
    private ExpandTabView expandTabView_are;
    @ViewInject(R.id.expandtab_view_sort_ve)
    private ExpandTabView expandTabView_stor;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ArrayList<View> sViewArray = new ArrayList<View>();//默认排序
    private ViewLeft viewLeft;
    private ViewRight viewLeft_stor;
    private String[] items_stor;//显示字段
    private String[] itemsVaule ;//隐藏id
    private String areId;
    //    private String menuId;
    private Request request;
    private String[] areItems;
    private String[] areItemsVal;
    private static OkHttpClient client = new OkHttpClient();
    private String menuId = null ;
    private String topicId=null;
    private String flag=null;
    private PullToRefreshListView pullToRefreshListView;
    private String senceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ViewUtils.inject(this);
        center_title_text.setText("学术");
        choiceCityName = SharedUtils.getCityName(this);

        initView();
        initIntentData();
        if ("special".equals(flag)){
            initSpecialData(Mode.DOWN,"","");

        }else{
            initArticle(Mode.DOWN,"","");
        }
        getSearchData();
        getMenuData();
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tags.add("标签" + i);
        }

        TagCloudView tagCloudView1 = (TagCloudView) findViewById(R.id.tag_cloud_view);
        tagCloudView1.setTags(tags);
        tagCloudView1.setOnTagClickListener(this);
        tagCloudView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "TagView onClick",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initIntentData() {
        Intent intent=getIntent();
        topicId=intent.getStringExtra("topicId");
        flag=intent.getStringExtra("flag");
    }

    /**
     * 数据查询条件框赋值
     */
    private void initVauleLeft() {
        mViewArray.add(viewLeft);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("区域");
        expandTabView_are.setValue(mTextArray, mViewArray);

    }
    private void initVauleRight() {
        sViewArray.add(viewLeft_stor);
        ArrayList<String> sTextArray = new ArrayList<String>();
        sTextArray.add("栏目");
        expandTabView_stor.setValue(sTextArray, sViewArray);
    }

    private void initListener() {

        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefreshview(viewLeft, showText);


            }
        });


    }

    private void initListenerRight() {

        viewLeft_stor.setOnSelectListener(new ViewRight.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefreshview(viewLeft_stor, showText);

            }
        });

    }

    private void onRefreshview(View view, String showText) {

        expandTabView_are.onPressBack();
        expandTabView_stor.onPressBack();

        int position = getPositon(view,mViewArray);
        int position2 = getPositon2(view, sViewArray);
        int pos=getPos(showText,items_stor);
        if (position >= 0 && !expandTabView_are.getTitle(position).equals(showText)) {
            expandTabView_are.setTitle(showText, position);
            areId=showText;
            if ("special".equals(flag)){
                initSpecialData(Mode.DOWN,areId,"");
            }else{
                initArticle(Mode.DOWN,areId,"");
            }

        }
        if (position2 >= 0 && !expandTabView_stor.getTitle(position2).equals(showText)) {
            expandTabView_stor.setTitle(showText, position2);
            menuId=itemsVaule[pos];
            if ("special".equals(flag)){
                initSpecialData(Mode.DOWN,areId,menuId);
            }else{
                initArticle(Mode.DOWN,areId,menuId);
            }

        }


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
    private int getPos(String showText,String[] items_stor) {
        for (int j = 0; j < items_stor.length; j++) {
            if (showText.equals(items_stor[j])) {
                return j;
            }
        }

        return -1;
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
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    if(!result.isEmpty() && result.indexOf("}")!=-1){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objdata=obj.getJSONObject("Response");
                        JSONArray objArray = objdata.getJSONArray("datas");
                        int length = objArray.size();
                        if(objArray != null && length > 0){
                            areItems=new String[length];
                            areItemsVal=new String[length];
                            for(int i = 0; i < length; i++){
                                areItems[i]=objArray.getJSONObject(i).getString("areaName");
                                areItemsVal[i]=objArray.getJSONObject(i).getString("areaId");
                            }
                            viewLeft = new ViewLeft(getBaseContext(), areItems, areItemsVal);
                        }else{
                            areItems=new String[length];
                            areItemsVal=new String[length];
                            viewLeft = new ViewLeft(getBaseContext(), areItems, areItemsVal);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initVauleLeft();
                            initListener();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getMenuData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestParams params = new RequestParams();

                    HttpUtils http = new HttpUtils();
                    http.send(HttpRequest.HttpMethod.POST,
                            Contants.GET_search,
                            params, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    JSONObject object = JSON.parseObject(responseInfo.result);

                                    if (object.getString("result").equals("200")) {
                                        JSONObject response = object.getJSONObject("Response");
                                        JSONArray jsonArray = response.getJSONArray("data");
                                        int length = jsonArray.size();

                                        if (jsonArray != null && length > 0) {
                                            items_stor = new String[length];
                                            itemsVaule = new String[length];
                                            for (int i = 0; i < length; i++) {
                                                items_stor[i] = jsonArray.getJSONObject(i).getString("menuName");
                                                itemsVaule[i] = jsonArray.getJSONObject(i).getString("menuId");
                                            }
                                            viewLeft_stor = new ViewRight(getBaseContext(), items_stor, itemsVaule);
                                        }

                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            initVauleRight();
                                            initListenerRight();
                                        }
                                    });
                                }


                                @Override
                                public void onFailure(HttpException e, String s) {
                               Toast.makeText(FindActivity.this, "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
                                }
                            });


                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        }).start();
    }



    private void initView() {
        mHandler = new Handler();
        articleList=new ArrayList<>();
        pullToRefreshListView= (PullToRefreshListView) findViewById(R.id.find_list_view);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView=pullToRefreshListView.getRefreshableView();
        //1.ImageLoader实例 2. 滑动的时候是否暂停加载  3.当飞起来的时候是否暂停加载
        mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        adapter = new MyAdapter(getBaseContext());
        mListView.setOnItemClickListener(this);
        adapter.updateList(articleList);


    }
    public void initArticle(final Mode mode,String areId,String menuId) {
        switch (mode){
            case DOWN:
                mIndex=1;
                break;
            case UP:
                mIndex++;
                break;
        }

            //articleList = new ArrayList<Article>();
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("cityName", choiceCityName);
            params.addQueryStringParameter("infoCounty", areId);
            params.addQueryStringParameter("menuId",menuId);
            params.addQueryStringParameter("pageNo", mIndex + "");
            Log.e("-----------------------",mIndex+"");
            Log.e(TAG,"mindex："+mIndex);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.GET_FIND_INFO,
                    params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {

                            Log.e(TAG,"************"+responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("Response");
                                JSONArray jsonArray = response.getJSONArray("datas");
                                switch (mode){

                                    case DOWN:
                                        try {
                                            articleList.clear();
                                            int length = jsonArray.size();
                                            List<Article> data=new ArrayList<>();
                                            if (jsonArray != null && length > 0) {
                                                Article article = null;
                                                for (int i = 0; i < length; i++) {
                                                    article = JSON.parseObject(jsonArray.getJSONObject(i).toString(), Article.class);
                                                    articleList.add(article);
                                                }
                                            }
                                            adapter.updateList(articleList);
                                            mListView.setAdapter(adapter);
                                            if (pullToRefreshListView.isRefreshing()) {
                                                pullToRefreshListView.onRefreshComplete();
                                            }
                                        }catch (Exception e){

                                        }
                                        break;

                                    case UP:
                                        try {
                                            int length = jsonArray.size();
                                            List<Article> data=new ArrayList<>();
                                            if (jsonArray != null && length > 0) {
                                                Article article = null;
                                                for (int i = 0; i < length; i++) {
                                                    article = JSON.parseObject(jsonArray.getJSONObject(i).toString(), Article.class);
                                                    data.add(article);
                                                }
                                                articleList.addAll(data);
                                                adapter.addList(data);
                                                mListView.setAdapter(adapter);
                                                if (pullToRefreshListView.isRefreshing()) {
                                                    pullToRefreshListView.onRefreshComplete();
                                                }
                                            }else if (data.size()==0){
                                                Toast.makeText(FindActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                                                if(pullToRefreshListView.isRefreshing()) {
                                                    pullToRefreshListView.onRefreshComplete();
                                                }
                                            }
                                        }catch (Exception e){

                                        }
                                        break;
                                }



                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(FindActivity.this, "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    });

    }
    private void initSpecialData(final Mode mode,String areId,String menuId){
        switch (mode){
            case DOWN:
                mIndex=1;
                break;
            case UP:
                mIndex++;
                break;
        }
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("cityName", choiceCityName);
        params.addQueryStringParameter("infoCounty", areId);
        params.addQueryStringParameter("topicId", topicId);
        params.addQueryStringParameter("pageNo", mIndex + "");
        params.addQueryStringParameter("menuId", menuId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.SPECIAL_LIST,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("***************", "******************" + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONArray jsonArray = response.getJSONArray("datas");
                           switch (mode){
                               case DOWN:
                                   try {
                                       articleList.clear();
                                       int length = jsonArray.size();
                                       List<Article> data=new ArrayList<>();
                                       if (jsonArray != null && length > 0) {
                                           Article article = null;
                                           for (int i = 0; i < length; i++) {
                                               article = JSON.parseObject(jsonArray.getJSONObject(i).toString(), Article.class);
                                               articleList.add(article);
                                           }
                                       }
                                       adapter.updateList(articleList);
                                       mListView.setAdapter(adapter);
                                       if (pullToRefreshListView.isRefreshing()) {
                                           pullToRefreshListView.onRefreshComplete();
                                       }
                                   }catch (Exception e){

                                   }
                                   break;

                               case UP:
                                   try {
                                       int length = jsonArray.size();
                                       List<Article> data=new ArrayList<>();
                                       if (jsonArray != null && length > 0) {
                                           Article article = null;
                                           for (int i = 0; i < length; i++) {
                                               article = JSON.parseObject(jsonArray.getJSONObject(i).toString(), Article.class);
                                               data.add(article);
                                           }
                                           articleList.addAll(data);
                                           adapter.addList(data);
                                           mListView.setAdapter(adapter);
                                           if (pullToRefreshListView.isRefreshing()) {
                                               pullToRefreshListView.onRefreshComplete();
                                           }
                                       }else if (data.size()==0){
                                           Toast.makeText(FindActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                                           if(pullToRefreshListView.isRefreshing()) {
                                               pullToRefreshListView.onRefreshComplete();
                                           }
                                       }
                                   }catch (Exception e){

                                   }
                                   break;
                           }
                            pullToRefreshListView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(FindActivity.this, "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }

                });

    }


    /**
     * 下拉刷新
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        if ("special".equals(flag)){
            initSpecialData(Mode.DOWN,"","");
        }else{
            initArticle(Mode.DOWN,"","");
        }

    }

    /**
     * 上拉加载
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if ("special".equals(flag)){
            initSpecialData(Mode.UP,"","");
        }else{
            initArticle(Mode.UP,"","");
        }

    }


    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    public void navMenuBtnClick(View view) {
        if (view.getTag().equals("nav_index")) {
            finish();
            MainActivity.MainActivity_instence.finish();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
        }
        if (view.getTag().equals("nav_mine")) {
            finish();
            startActivity(new Intent(getBaseContext(), MineActivity.class));
        }
    }

    /**
     *头部返回
     * @param view
     */
    @OnClick(R.id.title1_back)
    public void backBtnClick(View view){
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,PublishInfoDetailActivity.class);
        Bundle mbundle=new Bundle();
        mbundle.putString("sceneId",articleList.get(position-1).getSceneId());
        intent.putExtras(mbundle);
        startActivity(intent);
    }

    @Override
    public void onTagClick(int position) {
        if (position == -1) {
            Toast.makeText(getApplicationContext(), "点击末尾文字",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "点击 position : " + position,
                    Toast.LENGTH_SHORT).show();
        }
    }

    enum Mode{
        DOWN,UP
    }
}
