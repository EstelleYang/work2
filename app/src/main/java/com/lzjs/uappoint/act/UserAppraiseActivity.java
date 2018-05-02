package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.AEvaluate;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lzjs.com.refreshandload.refreshandload.XListView;

/**
 * 用户评价
 * Created by shalei on 2016/2/1.
 */
public class UserAppraiseActivity extends Activity implements XListView.IXListViewListener{
    public static final String TAG=UserAppraiseActivity.class.getSimpleName();

    private XListView listView;
    private List<AEvaluate> aEvaluateList;
    private AEvaluateAdapter aEvaluateAdapter;
    private int mIndex = 1;
    private String userId;
    private Handler mHandler;

    //返回按钮
    private ImageView backImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appraise);

        TextView textView = (TextView)findViewById(R.id.center_title_text);
        textView.setText(R.string.title_activity_my_assess);
        getData();

        listView = (XListView) findViewById(R.id.listView);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        listView.setAutoLoadEnable(true);
        listView.setXListViewListener(this);
        listView.setRefreshTime(getTime());


        //返回事件
        backImageView = (ImageView) findViewById(R.id.title1_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getBaseContext(), MineActivity.class));

                UserAppraiseActivity.this.finish();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData(){
        mHandler=new Handler();
        aEvaluateList = new ArrayList<>();
        userId = SharedUtils.getLoginUserId(this);
        requestHttp("1");
    }

    /**
     * flag:
     * 1.正常加载
     * 2.加载更多
     * @param flag
     */
    private void requestHttp(final String flag){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", userId);
        params.addQueryStringParameter("pageNo", mIndex+"");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.USER_DISCUSS_LIST,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e(TAG, "onSuccess" + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONArray jsondata = response.getJSONArray("datas");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<AEvaluate>>() {
                            }.getType();
                            List<AEvaluate> mList = gson.fromJson(jsondata.toString(), type);
                           // Log.e(TAG, aEvaluateList.get(0).getRegiUserPic() + "");
                            if ("1".equals(flag)) {
                                aEvaluateList.clear();
                                aEvaluateAdapter = new AEvaluateAdapter(UserAppraiseActivity.this, mList);
                                aEvaluateList.addAll(mList);
                            } else if ("2".equals(flag)&&mList.size()>0) {
                                aEvaluateList.addAll(mList);
                                aEvaluateAdapter = new AEvaluateAdapter(UserAppraiseActivity.this, aEvaluateList);
                            }
                            listView.setAdapter(aEvaluateAdapter);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(UserAppraiseActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIndex = 1;
                requestHttp("1");
                onLoad();
            }
        }, 1500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIndex++;
                requestHttp("2");
                onLoad();
            }
        }, 1500);


    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime(getTime());
    }
    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private class AEvaluateAdapter extends BaseAdapter {
        private Context context;
        List<AEvaluate> aEvaluateList;

        public final class ListItemView {                //自定义控件集合
            public ImageView image;
            public TextView merchantName;//商家名称
            public TextView appraiseTime;//发布时间
            public TextView appraiseContent;//评价内容
            public TextView replyContent;//商家回复内容
            public TextView replyTime;//商家回复时间
            public TextView my_appraise_content;
            public TextView my_appraise_time;


        }
        public AEvaluateAdapter ( Context context,List<AEvaluate> list){
            this.context = context;
            this.aEvaluateList = list;
        }
        @Override
        public int getCount() {
            return aEvaluateList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //自定义视图
            ListItemView listItemView = null;
            if(convertView == null){
                listItemView = new ListItemView();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_my_appraise_list_item,null);
                listItemView.image = (ImageView) convertView.findViewById(R.id.appraise_view_pic);
                listItemView.merchantName = (TextView) convertView.findViewById(R.id.merchantName);
                listItemView.appraiseTime = (TextView) convertView.findViewById(R.id.appraiseTime);
                listItemView.appraiseContent = (TextView) convertView.findViewById(R.id.appraiseContent);
                listItemView.replyContent = (TextView) convertView.findViewById(R.id.replyContent);
                listItemView.replyTime = (TextView) convertView.findViewById(R.id.replyTime);
                listItemView.my_appraise_content = (TextView) convertView.findViewById(R.id.my_appraise_content);
                listItemView.my_appraise_time = (TextView) convertView.findViewById(R.id.my_appraise_time);
                convertView.setTag(listItemView);
            }else {
                listItemView = (ListItemView) convertView.getTag();
            }
          //  ImageLoader.getInstance().displayImage(aEvaluateList.get(position).getRegiUserPic(), listItemView.image);

            listItemView.image.setVisibility(View.GONE);
            listItemView.merchantName.setText(aEvaluateList.get(position).getMerchantName());
            listItemView.appraiseTime.setText(aEvaluateList.get(position).getCreateTime());
            listItemView.appraiseContent.setText(aEvaluateList.get(position).getEvaluateContent());
            Log.e(TAG,"************"+aEvaluateList.get(position).getReplys().isEmpty());
            if (!aEvaluateList.get(position).getReplys().isEmpty()) {
                listItemView.replyContent.setText(aEvaluateList.get(position).getReplys().get(0).getReplyContent());
                listItemView.replyTime.setText(aEvaluateList.get(position).getReplys().get(0).getReplyTime());
            }else{
                listItemView.replyContent.setVisibility(View.GONE);
                listItemView.replyTime.setVisibility(View.GONE);
                listItemView.my_appraise_content.setVisibility(View.GONE);
                listItemView.my_appraise_time.setVisibility(View.GONE);

            }


            return convertView;
        }
    }

}
