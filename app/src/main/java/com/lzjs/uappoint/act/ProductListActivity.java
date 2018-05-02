package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.ProductlistAdapter;
import com.lzjs.uappoint.adapter.VqlistAdapter;
import com.lzjs.uappoint.bean.NewProduct;
import com.lzjs.uappoint.bean.Product;
import com.lzjs.uappoint.bean.Venue;
import com.lzjs.uappoint.calback.MyListener;
import com.lzjs.uappoint.util.CheckConnectionUtils;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductListActivity extends Fragment implements MyListener, View.OnClickListener, Handler.Callback, PullToRefreshBase.OnRefreshListener2<ListView> {

    private static final String TAG = "ProductListActivity";


    public Context context; // 存储上下文对象
    public Activity activity; // 存储上下文对象

    private ListView mListView;

    private VqlistAdapter mAdapter;

    private List<Product> productList;
    private List<NewProduct> eqProductList;
    private List<Venue> venueList;
    private ProductlistAdapter eqAdapter;

    private static OkHttpClient client = new OkHttpClient();

    private static String merchantId;//商家Id

    private static String flag;

    private Button btn_balance;
    private static String merchantAdd;
    private Handler mHandler;
    private int mIndex = 1;
    private PullToRefreshListView pullToRefreshListView;


    private Request request;

    public ProductListActivity() {
        super();
    }

    public static ProductListActivity newInstance(String merchantId, String flag) {
        ProductListActivity fragment = new ProductListActivity();
        //fragment.title=title;
        Bundle args = new Bundle();
        args.putString("merchantId", merchantId);
        args.putString("flag", flag);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        activity = getActivity();
        ExitApplication.getInstance().addActivity(activity);
        View view = inflater.inflate(R.layout.activity_product_list, container, false);
        btn_balance = (Button) view.findViewById(R.id.btn_balance);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview_pro);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView = pullToRefreshListView.getRefreshableView();
        eqProductList = new ArrayList<>();
        eqAdapter = new ProductlistAdapter(context);
        mAdapter = new VqlistAdapter(context, merchantId);
        eqAdapter.setListener(this);
        productList = new ArrayList<>();
        venueList = new ArrayList<>();
        btn_balance.setOnClickListener(this);
        mHandler = new Handler(this);
        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getActivity())) {
            getData(Mode.DOWN);
        } else {
            Toast.makeText(getActivity(), "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    /**
     * 获取数据
     */
    private void getData(final Mode mode) {


        switch (mode) {
            case DOWN:
                mIndex = 1;
                break;
            case UP:
                mIndex++;
                break;
        }
        Log.e(TAG, "flag:" + flag);
        Bundle bundle = getArguments();
        merchantId = bundle.getString("merchantId");
        flag = bundle.getString("flag");


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if ("00A".equals(flag)) {
                        FormBody formBody = new FormBody.Builder()
                                .add("merchantId", merchantId)
                                .add("pageNo", mIndex + "")
                                .add("cityName", SharedUtils.getLocationCityName(context))
                                .add("priceOrderBy", "")
                                .add("numOrderBy", "")
                                .build();
                        request = new Request.Builder().url(Contants.GET_PRODUCT_INFOS).post(formBody).build();
                        Response response = client.newCall(request).execute();
                        String result = response.body().string();
                        Log.e(TAG,"result:"+result);
                        if (!result.isEmpty() && result.indexOf("}") != -1) {
                            JSONObject obj = JSONObject.parseObject(result);
                            JSONObject objdata = obj.getJSONObject("Response");
                            JSONArray objArray = objdata.getJSONArray("datas");
                            switch (mode) {
                                case DOWN:
                                    try {
                                        productList.clear();
                                        int length = objArray.size();
                                        if (objArray != null && length > 0) {
                                            Product product = null;
                                            for (int i = 0; i < length; i++) {
                                                product = new Product();
                                                product.setProductId(objArray.getJSONObject(i).getString("productId"));
                                                product.setProductPic(objArray.getJSONObject(i).getString("productPic"));
                                                product.setProductPrice(objArray.getJSONObject(i).getString("productPrice"));
                                                product.setProductName(objArray.getJSONObject(i).getString("productName"));
                                                product.setMerchantName(objArray.getJSONObject(i).getString("merchantName"));
                                                product.setMerchantId(objArray.getJSONObject(i).getString("menuName"));
                                                product.setMerchantAdd(objArray.getJSONObject(i).getString("merchantAdd"));
                                                product.setProductNum(objArray.getJSONObject(i).getString("productNum"));
                                                product.setProductIntr(objArray.getJSONObject(i).getString("productIntr"));
                                                product.setProductZkPrice(objArray.getJSONObject(i).getString("productZkPrice"));
                                                product.setNum(0);
                                                merchantAdd = objArray.getJSONObject(i).getString("merchantAdd");
                                                productList.add(product);
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                    break;
                                case UP:
                                    try {
                                        int length = objArray.size();
                                        List<Product> data = new ArrayList<>();
                                        if (objArray != null && length > 0) {
                                            Product product = null;
                                            for (int i = 0; i < length; i++) {
                                                product = new Product();
                                                product.setProductId(objArray.getJSONObject(i).getString("productId"));
                                                product.setProductPic(objArray.getJSONObject(i).getString("productPic"));
                                                product.setProductPrice(objArray.getJSONObject(i).getString("productPrice"));
                                                product.setProductName(objArray.getJSONObject(i).getString("productName"));
                                                product.setMerchantName(objArray.getJSONObject(i).getString("merchantName"));
                                                product.setMerchantId(objArray.getJSONObject(i).getString("menuName"));
                                                product.setMerchantAdd(objArray.getJSONObject(i).getString("merchantAdd"));
                                                product.setProductNum(objArray.getJSONObject(i).getString("productNum"));
                                                product.setProductIntr(objArray.getJSONObject(i).getString("productIntr"));
                                                product.setProductZkPrice(objArray.getJSONObject(i).getString("productZkPrice"));
                                                product.setNum(0);
                                                merchantAdd = objArray.getJSONObject(i).getString("merchantAdd");
                                                data.add(product);
                                            }
                                            productList.addAll(data);

                                        } else {
                                            mHandler.sendEmptyMessage(1);
                                        }
                                    } catch (Exception e) {

                                    }
                                    break;
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (mode) {
                                        case DOWN:
                                            eqAdapter.updateList(productList);
                                            break;
                                        case UP:
                                            eqAdapter.addList(productList);
                                            break;
                                    }
                                    mListView.setAdapter(eqAdapter);
                                    if (pullToRefreshListView.isRefreshing()) {
                                        pullToRefreshListView.onRefreshComplete();
                                    }
                                }
                            });
                        }
                    } else if ("00B".equals(flag)) {
                        mHandler.sendEmptyMessage(0);
                        FormBody formBody = new FormBody.Builder()
                                .add("merchantId", merchantId)
                                .add("pageNo", mIndex + "")
                                .add("cityName", SharedUtils.getLocationCityName(context))
                                .add("priceOrderBy", "")
                                .add("numOrderBy", "")
                                .build();
                        request = new Request.Builder().url(Contants.GET_VENULIST_BY_ID).post(formBody).build();
                        Response responseVenue = client.newCall(request).execute();
                        String resultVenue = responseVenue.body().string();
                        Log.e("00A", "**********" + resultVenue);
                        if (!resultVenue.isEmpty() && resultVenue.indexOf("}") != -1) {
                            JSONObject obj = JSONObject.parseObject(resultVenue);
                            JSONObject objdata = obj.getJSONObject("Response");
                            JSONArray objArray = objdata.getJSONArray("datas");

                            switch (mode) {
                                case DOWN:
                                    try {
                                        venueList.clear();
                                        int length = objArray.size();
                                        if (objArray != null && length > 0) {
                                            Venue venue1 = null;
                                            for (int i = 0; i < length; i++) {
                                                venue1 = JSON.parseObject(objArray.get(i).toString(), Venue.class);
                                                venueList.add(venue1);
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                    break;
                                case UP:
                                    try {
                                        int length = objArray.size();
                                        List<Venue> data = new ArrayList<>();
                                        if (objArray != null && length > 0) {
                                            Venue venue1 = null;

                                            for (int i = 0; i < length; i++) {
                                                venue1 = JSON.parseObject(objArray.get(i).toString(), Venue.class);
                                                data.add(venue1);
                                            }
                                            venueList.addAll(data);
                                        }else{
                                            mHandler.sendEmptyMessage(1);
                                        }
                                    } catch (Exception e) {

                                    }
                                    break;
                            }


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (mode) {
                                        case DOWN:
                                           mAdapter.updateList(venueList);
                                            break;
                                        case UP:
                                            mAdapter.addList(venueList);
                                            break;
                                    }
                                    if (pullToRefreshListView.isRefreshing()) {
                                        pullToRefreshListView.onRefreshComplete();
                                    }
                                    mListView.setAdapter(mAdapter);
                                }
                            });
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 接口回调传过来的值
     */
    @Override
    public void onItemClick(int position, int id) {
        int buynum = 0;
        switch (id) {
            case 1:

                buynum = productList.get(position).getNum();
                buynum = buynum + 1;
                eqAdapter.getItem(position).setNum(buynum);
                Log.e(TAG, "+:" + (buynum));
                eqAdapter.notifyDataSetChanged();
                break;
            case 2:
                buynum = productList.get(position).getNum();
                buynum = buynum - 1;
                eqAdapter.getItem(position).setNum(buynum);
                Log.e(TAG, "-:" + (buynum));
                eqAdapter.notifyDataSetChanged();
                break;
        }

        NewProduct product = new NewProduct();
        product.setNum(buynum);
        product.setProductId(productList.get(position).getProductId());
        product.setProductPrice(productList.get(position).getProductZkPrice());
        product.setProductName(productList.get(position).getProductName());
        product.setProductPic(productList.get(position).getProductPic());


        if (buynum > 0) {
            for (int i = 0; i < eqProductList.size(); i++) {
                if (eqProductList.get(i).getProductId().equals(product.getProductId())) {
                    eqProductList.remove(i);
                }
            }
            eqProductList.add(product);
        } else {

            for (int i = 0; i < eqProductList.size(); i++) {
                if (eqProductList.get(i).getProductId().equals(product.getProductId())) {
                    eqProductList.remove(i);
                }
            }
        }

    }

    /**
     * Called when a view has been clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_balance:
                int size = eqProductList.size();
                String regiId = SharedUtils.getLoginUserId(getActivity());
                if (size > 0) {
                    //判断是否登录
                    if (regiId.isEmpty()) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Intent intent = new Intent(getActivity(), MoreProductActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("list", (ArrayList<NewProduct>) eqProductList);
                        Log.e(TAG, "*************" + eqProductList.get(0).getProductName());
                        bundle.putString("mer", merchantId);
                        bundle.putString("add", merchantAdd);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(getActivity(), "请选择商品", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                btn_balance.setVisibility(View.GONE);
                break;
            case 1:
                Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
     * 下拉刷新
     *
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getData(Mode.DOWN);
    }

    /**
     * 上拉加载
     *
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getData(Mode.UP);
    }

//
//    private class VqlistAdapter extends BaseAdapter {
//        private Context context;
//        List<Venue> venueList;
//        private String VenueId;
//
//        public final class ListItemView {                //自定义控件集合
//            public ImageView image;
//            public TextView venueIntr;//简介
//            public TextView venue_yuanjia;//原价
//            public TextView venue_xianjia;//现价
//            public TextView venue_name;//场地名称
//            public Button orderButton;
//        }
//
//        public VqlistAdapter(Context context, List<Venue> list) {
//            this.context = context;
//            this.venueList = list;
//        }
//
//        @Override
//        public int getCount() {
//            return venueList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            //自定义视图
//            ListItemView listItemView = null;
//            if (convertView == null) {
//                listItemView = new ListItemView();
//                convertView = LayoutInflater.from(context).inflate(R.layout.venue_listitem, null);
//                listItemView.image = (ImageView) convertView.findViewById(R.id.venueItemView1);
//                listItemView.venueIntr = (TextView) convertView.findViewById(R.id.venue_Intr);
//                listItemView.venue_yuanjia = (TextView) convertView.findViewById(R.id.venue_yuanjia);
//                listItemView.venue_xianjia = (TextView) convertView.findViewById(R.id.venue_xianjia);
//                listItemView.venue_name = (TextView) convertView.findViewById(R.id.venue_name);
//
//
//                listItemView.orderButton = (Button) convertView.findViewById(R.id.orderButton);
//                convertView.setTag(listItemView);
//            } else
//                listItemView = (ListItemView) convertView.getTag();
//            Log.e(TAG, "position:" + position);
//            ImageLoader.getInstance().displayImage(venueList.get(position).getVenuePic(), listItemView.image, DisplayUtil.getOptions());
//            listItemView.venueIntr.setText(venueList.get(position).getVenueIntr());
//            listItemView.venue_name.setText(venueList.get(position).getVenueName());
//            listItemView.venue_yuanjia.setText("￥" + venueList.get(position).getVenuePrice());
//            listItemView.venue_xianjia.setText("￥" + venueList.get(position).getVenueZkPrice());
//            listItemView.venue_yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            final int pos = position;
//            //注册点击事件
//            listItemView.image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    VenueId = venueList.get(pos).getVenueId();
//
//                    Intent intent = new Intent(context, VenueViewActivity.class);
//                    Bundle mbundle = new Bundle();
//                    mbundle.putString("venueId", VenueId);
//                    intent.putExtras(mbundle);
//                    Log.i(TAG, "onClick: VenueId" + VenueId);
//                    startActivity(intent);
//                }
//            });
//
//            listItemView.venueIntr.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    VenueId = venueList.get(pos).getVenueId();
//                    Intent intent = new Intent(context, VenueViewActivity.class);
//                    Bundle mbundle = new Bundle();
//                    mbundle.putString("venueId", VenueId);
//                    intent.putExtras(mbundle);
//                    Log.i(TAG, "onClick: VenueId" + VenueId);
//                    startActivity(intent);
//                }
//            });
//
//            //预约按钮点击事件
//            listItemView.orderButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = null;
//                    Bundle mbundle = new Bundle();
//                    //判断用户是否登录
//                    String userId = SharedUtils.getLoginUserId(getActivity());
//                    VenueId = venueList.get(pos).getVenueId();
//                    if (userId != null && !userId.equals("")) {
//                        intent = new Intent(getActivity(), VenueOrderActivity.class);
//                        Log.e(TAG, "VenueId:" + VenueId);
//                        mbundle.putString("venueId", VenueId);
//                        mbundle.putString("userId", userId);
//                        mbundle.putString("merchantId", merchantId);
//                        intent.putExtras(mbundle);
//                    } else {
//                        intent = new Intent(getActivity(), LoginActivity.class);
//                        mbundle.putString("venueId", VenueId);
//                    }
//                    if (intent != null) {
//                        startActivity(intent);
//                    }
//
//                }
//
//            });
//
//            return convertView;
//        }
//    }

    enum Mode {
        DOWN, UP
    }
}
