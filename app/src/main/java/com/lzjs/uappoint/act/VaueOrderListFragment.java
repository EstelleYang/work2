package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.lzjs.uappoint.bean.Order;
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
 * 场地订单列表页面
 */
public class VaueOrderListFragment extends Fragment implements XListView.IXListViewListener {
    public static final String TAG = VaueOrderListFragment.class.getSimpleName();

    public Context context; // 存储上下文对象
    public Activity activity; // 存储上下文对象

    private XListView orderListView;

    private BaseAdapter mAdapter;

    private List<Order> orderList;

    private int mIndex = 1;
    private Handler mHandler;

    private String orderState;
    private String regiUserId;

    public static Fragment getInstance(String orderState) {

        VaueOrderListFragment instance = new VaueOrderListFragment();

        Bundle args = new Bundle();

        args.putString("orderState", orderState);

        //args.putString("url", murl);

        instance.setArguments(args);

        return instance;

    }

    public VaueOrderListFragment() {
        super();
    }

    public VaueOrderListFragment(String orderState) {
        this.orderState = orderState;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        activity = getActivity();
        View view = inflater.inflate(R.layout.vaue_order_list, container, false);
        orderListView = (XListView) view.findViewById(R.id.vaue_list);
        orderListView.setPullRefreshEnable(true);
        orderListView.setPullLoadEnable(true);
        orderListView.setAutoLoadEnable(true);
        orderListView.setXListViewListener(this);
        orderListView.setRefreshTime(getTime());
        mHandler = new Handler();
        getData();
        return view;
    }

    /**
     * 获取数据
     */
    private void getData() {
        Bundle bundle = getArguments();
        orderState = bundle.getString("orderState");
        orderList = new ArrayList<>();
        regiUserId = SharedUtils.getLoginUserId(getActivity());
        requestHttp("1");

    }

    /**
     * flag:  1:正常加载  2：加载更多
     */
    private void requestHttp(final String flag) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("regiUserId", regiUserId);
        params.addQueryStringParameter("pageNo", mIndex + "");
        params.addQueryStringParameter("orderState", orderState);
        Log.e(TAG, "requestHttp: regiUserId " + regiUserId);
        Log.e(TAG, "requestHttp: mIndex " + "*********" + mIndex);
        Log.e(TAG, "requestHttp: orderState " + "*********" + orderState);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.QUERY_USER_VENUE,
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
                        Log.e(TAG, "onSuccess++++++++++ " + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONArray jsonArray = response.getJSONArray("datas");
                            Log.e(TAG, jsonArray.toString());
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Order>>() {
                            }.getType();
                            List<Order> newList = gson.fromJson(jsonArray.toString(), type);
                            if ("1".equals(flag)) {
                                orderList.clear();
                                mAdapter = new MyAdapter(activity, newList);
                                orderList.addAll(newList);
                            } else if ("2".equals(flag)) {
                                mAdapter = new MyAdapter(activity, orderList);
                            }

                            orderListView.setAdapter(mAdapter);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getActivity(), "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
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
        orderListView.stopRefresh();
        orderListView.stopLoadMore();
        orderListView.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }


    private class MyAdapter extends BaseAdapter {

        private Context context;

        private List<Order> itemlists;

        //自定义控件集合
        public final class ListItemView {
            public TextView site_name;//商家名称
            public TextView order_type;//交易状态
            public TextView discuss;//评论
            public TextView orderDate;//订单日期
            public TextView venue_name;//场地名称
            private TextView username;//用户名称

        }

        public MyAdapter(Context context, List<Order> articles) {
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
            ListItemView listItemView = null;
            if (view == null) {
                listItemView = new ListItemView();
                view = LayoutInflater.from(context).inflate(R.layout.vaue_order_list_item, null);
                listItemView.site_name = (TextView) view.findViewById(R.id.site_name);
                listItemView.order_type = (TextView) view.findViewById(R.id.order_type);
                listItemView.discuss = (TextView) view.findViewById(R.id.discuss);
                listItemView.venue_name = (TextView) view.findViewById(R.id.venue_name);
                listItemView.username = (TextView) view.findViewById(R.id.username);
                listItemView.orderDate = (TextView) view.findViewById(R.id.orderDate);
                //  listItemView.image=(ImageView) view.findViewById(R.id.o_eq_order_pic);
                // listItemView.orderDate=(TextView) view.findViewById(R.id.orderTime);
                //设置控件集到view
                view.setTag(listItemView);
            } else {
                listItemView = (ListItemView) view.getTag();
            }


            /**
             * 订单状态 00A 待确认  00B 成功 00C 失败
             */
            // ImageLoader.getInstance().displayImage("drawable://" + R.drawable.tupian, listItemView.image);
            listItemView.site_name.setText(itemlists.get(position).getMerchantName());
            if ("00A".equals(itemlists.get(position).getOrderState())) {
                listItemView.order_type.setText(getResources().getString(R.string.no_sure));
            } else if ("00B".equals(itemlists.get(position).getOrderState())) {
                listItemView.order_type.setText(getResources().getString(R.string.success));
            }else if ("00C".equals(itemlists.get(position).getOrderState())) {
                listItemView.order_type.setText(getResources().getString(R.string.fail));
            }


            listItemView.venue_name.setText(itemlists.get(position).getVenueName());
            listItemView.username.setText(itemlists.get(position).getRegiUserName());
            listItemView.orderDate.setText(itemlists.get(position).getOrderTime());

            if ("00B".equals(itemlists.get(position).getOrderState())) {
                //注册点击事件
                listItemView.discuss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), UserDiscussActivity.class);
                            intent.putExtra("equipId", itemlists.get(position).getVenueId());
                            intent.putExtra("typeId", "00A");
                            intent.putExtra("merchantIds", itemlists.get(position).getMerchantId());
                            intent.putExtra("menuIds", itemlists.get(position).getMenuIds());

                            startActivity(intent);
                        }
                    }
                );
            } else {
                listItemView.discuss.setVisibility(View.GONE);
            }


            return view;
        }


    }
}
