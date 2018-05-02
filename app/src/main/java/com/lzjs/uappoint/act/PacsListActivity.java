package com.lzjs.uappoint.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.lzjs.uappoint.adapter.PacsListAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.PacsInfo;
import com.lzjs.uappoint.fresco.FrescoMainActivity;
import com.lzjs.uappoint.util.Contants;

import java.util.ArrayList;
import java.util.List;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;

/**
 * 影像列表
 */
public class PacsListActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private List<PacsInfo> datalist=new ArrayList<>();
    private Toolbar mToolbar;
    private TextView tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacs_list);

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
        listView= (ListView) findViewById(R.id.lv_pacs);
    }
    private  void getData(){
        //http://101.201.120.196:8080/dmservice/appjson/refreshusertopics.do
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", 1+"");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Contants.PACSLISTS, params,
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
                            PacsInfo pacs=null;
                            for (int i=0;i<jsondata.size();i++){
                                pacs=new PacsInfo();
                                pacs.setUploadid(jsondata.getJSONObject(i).getString("uploadid"));
                                pacs.setUserid(jsondata.getJSONObject(i).getString("userid"));
                                pacs.setUsername(jsondata.getJSONObject(i).getString("username"));
                                pacs.setHeadimage(jsondata.getJSONObject(i).getString("headimage"));
                                pacs.setHisname(jsondata.getJSONObject(i).getString("hisname"));
                                pacs.setRecommentname(jsondata.getJSONObject(i).getString("recommentname"));
                                pacs.setCreatedate(jsondata.getJSONObject(i).getString("createdate"));
                                pacs.setPayfeedate(jsondata.getJSONObject(i).getString("payfeedate"));
                                pacs.setPacsstatus(jsondata.getJSONObject(i).getString("pacsstatus"));
                                pacs.setPacstype(jsondata.getJSONObject(i).getString("pacstype"));
                                pacs.setRemarks(jsondata.getJSONObject(i).getString("remarks"));
                                datalist.add(pacs);
                            }

                        }
                        listView.setAdapter(new PacsListAdapter(PacsListActivity.this,datalist));
                        Log.e("topicId", "onItemClick: "+datalist.get(1).getUploadid() );
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(),FrescoMainActivity.class);
                                Bundle mbundle = new Bundle();
                                mbundle.putBoolean("isImgShow", false);
                                Log.e("topicId", "onItemClick: "+datalist.get(1).getUploadid() );
                                mbundle.putString("topicId", datalist.get(position).getUploadid());
                                intent.putExtras(mbundle);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(PacsListActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
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
