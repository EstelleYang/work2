package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.ArticleAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class SearchViewActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String TAG=SearchViewActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private EditText mSearchText;
    private Button mSearchBtn;
    private String keyWords;
    private ListView listView_search;
    private List<Article> articleList;
    private int mIndex;
    private String flag;
    private String menuIds;
    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        initview();
        initData();
    }

    private void initData() {
        Intent intent =getIntent();
        flag=intent.getStringExtra("flag");
        menuIds=intent.getStringExtra("menuIds");
        Log.e(TAG, ".............." + menuIds);
        Log.e(TAG, ".............." + flag);
    }

    private void initview() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mSearchText = (EditText)findViewById(R.id.toolbar_name);
        mSearchBtn = (Button)findViewById(R.id.toolbar_btn);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyWords=mSearchText.getText().toString();
                if (!StringUtils.isEmpty(keyWords)){
                    if ("1".equals(flag)){
                        initAllArticle();
                    }else if ("2".equals(flag)){
                        initOnlyArticle();
                    }
                }else{
                    Toast.makeText(SearchViewActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //iv_search= (ImageView) findViewById(R.id.iv_search);
        listView_search= (ListView) findViewById(R.id.listView_search);
        listView_search.setOnItemClickListener(this);
    }

    public void backBtnClick(View v){
        /*switch (v.getId()){
            case R.id.search_back:
                SearchViewActivity.this.finish();
                break;
            case R.id.iv_search:
               keyWords=mSearchText.getText().toString();
                if (!StringUtils.isEmpty(keyWords)){
                    if ("1".equals(flag)){
                        initAllArticle();
                    }else if ("2".equals(flag)){
                        initOnlyArticle();
                    }
                }else{
                    Toast.makeText(SearchViewActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }*/
    }

    //加载数据
    public void initAllArticle() {
        articleList = new ArrayList<>();
        RequestParams params=new RequestParams();
        params.addQueryStringParameter("keyWords",keyWords);
        params.addQueryStringParameter("pageNo", mIndex + "");
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                    Contants.SEARCH_RESULT,
                    params,
                    new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e(TAG, "#########:" + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject objdata = object.getJSONObject("Response");
                            JSONArray objArray = objdata.getJSONArray("data");
                            int length = objArray.size();
                            if (objArray != null && length > 0) {
                                Article info = null;
                                for (int i = 0; i < length; i++) {
                                    info = new Article();
                                    info.setTital(objArray.getJSONObject(i).getString("merchantName"));
                                    info.setId(objArray.getJSONObject(i).getString("merchantId"));
                                    info.setImgUrl(objArray.getJSONObject(i).getString("merchantPic"));
                                    articleList.add(info);
                                }
                                mAdapter=new ArticleAdapter(SearchViewActivity.this);
                                mAdapter.addList(articleList);
                                listView_search.setAdapter(mAdapter);
                            }else{
                                Toast.makeText(SearchViewActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                    }
        });
    }

    public void initOnlyArticle(){
        articleList = new ArrayList<>();
        RequestParams params=new RequestParams();
        params.addQueryStringParameter("merchantName",keyWords);
        params.addQueryStringParameter("pageNo",mIndex+"");
        params.addQueryStringParameter("menuIds",menuIds);
        Log.e(TAG, "$$$$$$$$$$$" + menuIds);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.SEARCH_CATEGORY_RESULT,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e(TAG, "#########:" + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject objdata = object.getJSONObject("Response");
                            JSONArray objArray = objdata.getJSONArray("data");
                            int length = objArray.size();
                            if (objArray != null && length > 0) {
                                Article info = null;
                                for (int i = 0; i < length; i++) {
                                    info = new Article();
                                    info.setTital(objArray.getJSONObject(i).getString("merchantName"));
                                    info.setId(objArray.getJSONObject(i).getString("merchantId"));
                                    info.setImgUrl(objArray.getJSONObject(i).getString("merchantPic"));
                                    articleList.add(info);
                                    mAdapter=new ArticleAdapter(SearchViewActivity.this);
                                    mAdapter.addList(articleList);
                                    listView_search.setAdapter(mAdapter);
                                }
                            }else{
                                Toast.makeText(SearchViewActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this, MerchantInfoActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("merchantId", articleList.get(position).getId());
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}
