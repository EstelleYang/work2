package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.lzjs.uappoint.adapter.FriendsListAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.myview.CommonAdapter;
import com.lzjs.uappoint.myview.ViewHolder;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DisplayUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;
import lzjs.com.picplayer.bean.ADInfo;

/**
 * 影像圈列表
 */
public class FriendsListActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private List<Article> datalist=new ArrayList<>();
    private Toolbar mToolbar;
    //private TextView tital;
    private TextView tv_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        listView= (ListView) findViewById(R.id.lv_friend);
        tv_comment=(TextView)findViewById(R.id.toolbar_edit);
        initView();
        getData();
    }

    private  void initView(){
        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BodyAuthenticateActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putBoolean("isImgShow", false);
                mbundle.putString("titleShow", "发表话题");
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private  void getData(){
        //http://101.201.120.196:8080/dmservice/appjson/refreshusertopics.do
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", 1+"");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Contants.FRIENDSLISTS, params,
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
                                article=new Article();
                                article.setId(jsondata.getJSONObject(i).getString("topicid"));
                                article.setAuthor(jsondata.getJSONObject(i).getString("username"));
                                article.setTital(jsondata.getJSONObject(i).getString("content"));
                                article.setReplyCount(jsondata.getJSONObject(i).getString("replyCount"));
                                article.setVoteCount(jsondata.getJSONObject(i).getString("voteCount"));
                                article.setViewCount(jsondata.getJSONObject(i).getString("viewCount"));
                                article.setCreateData(jsondata.getJSONObject(i).getString("createdate"));
                                article.setImgUrl(jsondata.getJSONObject(i).getString("headimage"));
                                List<String> images=new ArrayList<String>();
                                images= JSONArray.parseArray(jsondata.getJSONObject(i).getJSONArray("images").toJSONString(),String.class);//jsondata.getJSONObject(i).getJSONArray("images");
                                article.setImages(images);
                                datalist.add(article);
                            }
                        }
                        listView.setAdapter(new FriendsListAdapter(FriendsListActivity.this,datalist));
                        //Log.e("topicId", "onItemClick: "+datalist.get(1).getId() );
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(),TopicDetailActivity.class);
                                Bundle mbundle = new Bundle();
                                mbundle.putBoolean("isImgShow", false);
                                Log.e("topicId", "onItemClick: "+datalist.get(1).getId() );
                                mbundle.putString("topicId", datalist.get(position).getId());
                                intent.putExtras(mbundle);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(FriendsListActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
    }
}
