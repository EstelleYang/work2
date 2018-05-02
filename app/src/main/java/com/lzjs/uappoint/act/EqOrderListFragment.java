package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.lzjs.uappoint.bean.Order;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lzjs.com.refreshandload.refreshandload.XListView;

/**
 * 装备列表页面
 * Created by wangdq on 2016/1/26.
 */
public class EqOrderListFragment extends Fragment implements XListView.IXListViewListener {

    private static final String TAG = EqOrderListFragment.class.getSimpleName();

    public Context context; // 存储上下文对象
    public Activity activity; // 存储上下文对象

    private XListView orderListView;

    private BaseAdapter mAdapter;

    private List<Order> orderList;
    private String orderState;
    private String regiUserId;

    private int mIndex = 0;
    private android.os.Handler mHandler;


    public static Fragment getInstance(String orderState) {

        EqOrderListFragment instance = new EqOrderListFragment();

        Bundle args = new Bundle();

        args.putString("orderState", orderState);

        //args.putString("url", murl);

        instance.setArguments(args);

        return instance;

    }


    public EqOrderListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        activity = getActivity();
        View view = inflater.inflate(R.layout.eq_order_list, container, false);
        orderListView = (XListView) view.findViewById(R.id.eq_order_mainart_list);
        orderListView.setPullRefreshEnable(true);
        orderListView.setPullLoadEnable(true);
        orderListView.setAutoLoadEnable(true);
        orderListView.setXListViewListener(this);
        orderListView.setRefreshTime(getTime());
        getData();
        mAdapter = new MyAdapter(activity, orderList);
        //setListAdapter(mAdapter);
        orderListView.setAdapter(mAdapter);


        return view;
    }

    /**
     * 获取数据
     */
    private void getData() {
        Bundle bundle = getArguments();
        orderState = bundle.getString("orderState");
        mHandler = new android.os.Handler();
        orderList = new ArrayList<>();
        regiUserId = SharedUtils.getLoginUserId(getActivity());
        requestHttp("1");

    }


    //1下拉刷新2.上拉加载
    private void requestHttp(final String flag) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("regiUserId", regiUserId);
        params.addQueryStringParameter("pageNo", mIndex + "");
        params.addQueryStringParameter("orderStatus", orderState);

        Log.e(TAG, "regiUserId:" + regiUserId + "pageNo:" + mIndex + "orderStatus:" + orderState + "regiUserId:" + regiUserId);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.QUERY_USER_QNUEN,
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
                            JSONObject response = object.getJSONObject("Response");
                            JSONArray jsonArray = response.getJSONArray("datas");
                            Log.e(TAG, jsonArray.toString());
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Order>>() {
                            }.getType();
                            ArrayList<Order> newList = gson.fromJson(jsonArray.toString(), type);
                            //判断订单的状态
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
                        Toast.makeText(getActivity(), "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
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


    private class MyAdapter extends BaseAdapter {

        private Context context;

        private List<Order> itemlists;

        //自定义控件集合
        public final class ListItemView {
            public TextView o_p_business;//商家名称
            public TextView o_status;//订单状态
            public TextView o_p_business_1;//装备名称
            public TextView o_p_price;//单价
            public TextView o_p_buyNum;//购买数量
            public TextView o_p_review;//评论
            public TextView actualpayment;//实际付款

            public ImageView image;//装备图片
            public TextView harvestAddress; //收货地址
            public TextView delete;
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
                view = LayoutInflater.from(context).inflate(R.layout.eq_order_list_item, null);
                listItemView.o_p_business = (TextView) view.findViewById(R.id.o_p_business);
                listItemView.o_status = (TextView) view.findViewById(R.id.o_status);
                listItemView.o_p_business_1 = (TextView) view.findViewById(R.id.o_p_business_1);
                listItemView.o_p_price = (TextView) view.findViewById(R.id.o_p_price);
                listItemView.o_p_buyNum = (TextView) view.findViewById(R.id.buyNum);
                listItemView.o_p_review = (TextView) view.findViewById(R.id.o_p_review);
                listItemView.image = (ImageView) view.findViewById(R.id.o_eq_order_pic);
                listItemView.harvestAddress = (TextView) view.findViewById(R.id.harvestAddress);
                listItemView.actualpayment = (TextView) view.findViewById(R.id.actualpayment);
                listItemView.delete = (TextView) view.findViewById(R.id.order_deldte);
                //设置控件集到view
                view.setTag(listItemView);
            } else {
                listItemView = (ListItemView) view.getTag();
            }


            ImageLoader.getInstance().displayImage(itemlists.get(position).getProductPic(), listItemView.image);

            listItemView.o_p_business.setText(itemlists.get(position).getMerchantName());
            listItemView.o_p_business_1.setText(itemlists.get(position).getProductName());
            listItemView.o_p_price.setText("￥:" + itemlists.get(position).getVegetablePrice());
            Log.e(TAG, "---------------------------------" + itemlists.get(position).getVegetablePrice());
            listItemView.o_p_buyNum.setText("共" + itemlists.get(position).getVegetableNum() + "件");
            listItemView.harvestAddress.setText(itemlists.get(position).getFriendAdd());
            listItemView.actualpayment.setText("实付：" + itemlists.get(position).getOrderPrice() + "元");


            Log.e(TAG, "getOrderStatus:" + itemlists.get(position).getOrderStatus());


            /***
             * 订单状态 00A 已提交 00B 已支付  00C 未发货 00D 已发货 00E 待收货  00F 已确认 00G 取消 00H 退款中 00I 已退款
             */
            if ("00A".equals(itemlists.get(position).getOrderStatus())) {
                listItemView.o_status.setText("未支付");
            } else if ("00B".equals(itemlists.get(position).getOrderStatus())) {
                listItemView.o_status.setText("已支付");
            } else if ("00H".equals(itemlists.get(position).getOrderStatus())) {
                listItemView.o_status.setText("退款中");
            } else if ("00I".equals(itemlists.get(position).getOrderStatus())) {
                listItemView.o_status.setText("已退款");
            } else if ("00C".equals(itemlists.get(position).getOrderStatus())) {
                listItemView.o_status.setText("待商家确认退款");
            } else if ("00F".equals(itemlists.get(position).getOrderStatus())) {
                listItemView.o_status.setText("已确认");
            } else if ("00G".equals(itemlists.get(position).getOrderStatus())) {
                listItemView.o_status.setText("取消");
            }


            //条目的点击监听
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EquipmentDetailsActivity.class);
                    String orderId = itemlists.get(position).getOrderId();
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("equiName", itemlists.get(position).getProductName());
                    startActivity(intent);
                }
            });


            if ("00B".equals(itemlists.get(position).getOrderStatus())) {
                //注册点击事件
                listItemView.o_p_review.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Intent intent = new Intent(getActivity(), UserDiscussActivity.class);
                                   intent.putExtra("equipId", itemlists.get(position).getProductId());
                                   intent.putExtra("typeId", "00A");
                                   intent.putExtra("merchantIds", itemlists.get(position).getMerchantId());
                                   intent.putExtra("menuIds", itemlists.get(position).getMenuIds());
                                   startActivity(intent);
                               }
                                                           }
                );
            } else {
                listItemView.o_p_review.setVisibility(View.GONE);
            }
            if ("00A".equals(itemlists.get(position).getOrderStatus())) {

                listItemView.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(itemlists.get(position).getOrderId(), position);
                    }
                });
            } else {
                listItemView.delete.setVisibility(View.GONE);
            }

            return view;
        }

        public void deleteOrder(String orderId, final int position) {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("orderId", orderId);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.DELETE_ORDER_EQUIMENT,
                    params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            JSONObject object = JSON.parseObject(responseInfo.result);

                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("Response");
                                Toast.makeText(getActivity(), response.getString("desc"), Toast.LENGTH_SHORT).show();
                                  itemlists.remove(position);
                                    notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(getActivity(), "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }


}
