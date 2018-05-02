package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.bean.Order;
import com.lzjs.uappoint.bean.TopicInfo;
import com.lzjs.uappoint.myview.NoScrollGridView;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DisplayUtil;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lzjs.com.refreshandload.refreshandload.XListView;

public class ReplyListActivity extends Fragment implements XListView.IXListViewListener {
    private static final String  TAG = ReplyListActivity.class.getSimpleName();
    public Context context; // 存储上下文对象
    public Activity activity; // 存储上下文对象

    private XListView orderListView;

    private BaseAdapter mAdapter;

    private android.os.Handler mHandler;

    private List<TopicInfo> replyList;

    private int mIndex = 0;

    private static String topicId;
    public static Fragment getInstance(String topicId) {
        ReplyListActivity instance = new ReplyListActivity();
        Bundle args = new Bundle();
        args.putString("topicId", topicId);
        //args.putString("url", murl);
        ReplyListActivity.topicId=topicId;
        instance.setArguments(args);
        return instance;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        activity = getActivity();
        View view = inflater.inflate(R.layout.activity_replay_list, container, false);
        //topicId=getIntent().getStringExtra("topicId");
        orderListView = (XListView) view.findViewById(R.id.topic_reply_list);
        orderListView.setPullRefreshEnable(true);
        orderListView.setPullLoadEnable(true);
        orderListView.setAutoLoadEnable(true);
        orderListView.setXListViewListener(this);
        orderListView.setRefreshTime(getTime());
        getData();
        mAdapter = new ReplyListActivity.MyAdapter(activity, replyList);
        //setListAdapter(mAdapter);
        orderListView.setAdapter(mAdapter);


        return view;
    }
    /**
     * 获取数据
     */
    private void getData() {
        Bundle bundle = getArguments();
        topicId = bundle.getString("topicId");
        mHandler = new android.os.Handler();
        replyList = new ArrayList<>();
        //regiUserId = SharedUtils.getLoginUserId(getActivity());
        requestHttp("1");

    }

    //1下拉刷新2.上拉加载
    private void requestHttp(final String flag) {
        RequestParams params = new RequestParams();
       // params.addQueryStringParameter("regiUserId", regiUserId);
        params.addQueryStringParameter("pageNo", mIndex + "");
        params.addQueryStringParameter("id", topicId);

        //Log.e(TAG, "regiUserId:" + regiUserId + "pageNo:" + mIndex + "orderStatus:" + orderState + "regiUserId:" + regiUserId);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.REPLY_LIST,
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
                        Log.e("myorder", "-------------->" + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("response");
                            JSONArray jsonArray=response.getJSONArray("datas");
                            Log.e(TAG, jsonArray.toString());


                            Gson gson = new Gson();
                            Type type = new TypeToken<List<TopicInfo>>() {
                            }.getType();
                            ArrayList<TopicInfo> newList = gson.fromJson(jsonArray.toString(), type);
                            //判断订单的状态
                            if ("1".equals(flag)) {
                                replyList.clear();
                                mAdapter = new ReplyListActivity.MyAdapter(activity, newList);
                                replyList.addAll(newList);
                            } else if ("2".equals(flag)) {
                                mAdapter = new ReplyListActivity.MyAdapter(activity, replyList);
                            }

                            orderListView.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getActivity(), "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class MyAdapter extends BaseAdapter {

        private Context context;

        private List<TopicInfo> itemlists;

        //自定义控件集合
        public final class ListItemView {
            private ImageView civ_head_item;
            private TextView tv_fdata_item;
            private TextView content_item;
            private  TextView tv_user_name_item;

        }

        public MyAdapter(Context context, List<TopicInfo> articles) {
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
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            //自定义视图
            ReplyListActivity.MyAdapter.ListItemView listItemView = null;
            if (view == null) {
                listItemView = new ReplyListActivity.MyAdapter.ListItemView();
                view = LayoutInflater.from(context).inflate(R.layout.act_reply_item, null);
                listItemView.tv_fdata_item = (TextView) view.findViewById(R.id.tv_fdata_item);
                listItemView.content_item = (TextView) view.findViewById(R.id.content_item);
                listItemView.tv_user_name_item = (TextView) view.findViewById(R.id.tv_user_name_item);
                listItemView.civ_head_item = (ImageView) view.findViewById(R.id.civ_head_item);
                //设置控件集到view
                view.setTag(listItemView);
            } else {
                listItemView = (ReplyListActivity.MyAdapter.ListItemView) view.getTag();
            }
            TopicInfo itemEntity = itemlists.get(position);
            listItemView.tv_fdata_item.setText(itemEntity.getCreatedate());
            listItemView.content_item.setText(itemEntity.getCommentcontent());
            listItemView.tv_user_name_item.setText(itemEntity.getUsername());
            ImageLoader.getInstance().displayImage(itemlists.get(position).getHeadimage(), listItemView.civ_head_item, DisplayUtil.getOptions());
            return view;
        }
    }


    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIndex = 0;
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
        orderListView.stopRefresh();
        orderListView.stopLoadMore();
        orderListView.setRefreshTime(getTime());
    }
    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
