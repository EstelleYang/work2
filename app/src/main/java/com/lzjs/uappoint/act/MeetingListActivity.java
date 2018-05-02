package com.lzjs.uappoint.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.lzjs.uappoint.adapter.MeetingListAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.Meeting;
import com.lzjs.uappoint.util.Contants;

import java.util.ArrayList;
import java.util.List;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;

/**
 * 会议/通告列表
 */
public class MeetingListActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private List<Meeting> datalist=new ArrayList<>();
    private Toolbar mToolbar;
    private TextView tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        listView= (ListView) findViewById(R.id.lv_meeting);

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

        getData();
        initView();
    }

    private  void initView(){
        /*tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BodyAuthenticateActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putBoolean("isImgShow", false);
                mbundle.putString("titleShow", "编辑内容");
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });*/
    }
    private  void getData(){
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", 1+"");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Contants.MEETINGLISTS, params,
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
                            Meeting meeting=null;
                            for (int i=0;i<jsondata.size();i++){
                                meeting = new Meeting();
                                meeting.setId(jsondata.getJSONObject(i).getString("meetingid"));
                                meeting.setTital(jsondata.getJSONObject(i).getString("title"));
                                meeting.setContent(jsondata.getJSONObject(i).getString("content"));
                                meeting.setRead(false);
                                meeting.setType(jsondata.getJSONObject(i).getString("meetingtype"));
                                meeting.setUrl(jsondata.getJSONObject(i).getString("meetingurl"));
                                meeting.setDate(jsondata.getJSONObject(i).getString("createdate"));
                                datalist.add(meeting);
                            }
                        }
                        listView.setAdapter(new MeetingListAdapter(MeetingListActivity.this,datalist));
                        Log.e("meetingid", "onItemClick: "+datalist.get(1).getId() );
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(),MeetingDetailActivity.class);
                                Bundle mbundle = new Bundle();
                                mbundle.putBoolean("isImgShow", false);
                                Log.e("meetingId", "onItemClick: "+datalist.get(1).getId() );
                                mbundle.putString("meetingId", datalist.get(position).getId());
                                mbundle.putString("type", datalist.get(position).getType());
                                mbundle.putString("url", datalist.get(position).getUrl());
                                intent.putExtras(mbundle);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(MeetingListActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
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
