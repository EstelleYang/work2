package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.lzjs.uappoint.bean.Product;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * 装备详细
 */
public class EqDetailActivity extends Activity {

    private static final String TAG ="EqDetailActivity" ;
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;
    @ViewInject(R.id.eq_detail_content)
    private TextView eq_detail_content;
    @ViewInject(R.id.eq_detail_productNum)
    private TextView eq_detail_productNum;
    @ViewInject(R.id.eq_detail_original_price)
    private TextView eq_detail_productPrice;
    @ViewInject(R.id.eq_detail_tel)
    private TextView eq_detail_tel;
    @ViewInject(R.id.eq_detail_addre)
    private TextView eq_detail_addre;
    @ViewInject(R.id.productName)
    private TextView productName;

    @ViewInject(R.id.eq_detail_ruling_Price)
    private TextView eq_detail_ruling_Price;

    private String productId;

    private Product product;
    private ArrayList<String> picList;
    @ViewInject(R.id.mainart_list_pic)
    private ListView picListView;

    private BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        productId=getIntent().getStringExtra("productId");
        setContentView(R.layout.activity_eq_detail);
        ViewUtils.inject(this);
        eq_detail_productPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        getData();
        center_title_text.setText("详情");
    }


    /**
     * 立即购买
     * @param view
     */
    public void BuyBtnClick(View view){
        Intent intent=null;
        Bundle mbundle=new Bundle();

        //判断用户是否登录
        String userId= SharedUtils.getLoginUserId(getBaseContext());
        if(userId!=null&&!userId.equals("")){
            intent=new Intent(getBaseContext(), Eq_BuyActivity.class);
            mbundle.putString("productId", productId);
            mbundle.putString("userId", userId);
            mbundle.putString("merchantId", product.getMerchantId());
            mbundle.putString("merchantAdd", product.getMerchantAdd());
            mbundle.putString("productNum",product.getProductNum());
            intent.putExtras(mbundle);
        }else{
            intent=new Intent(getBaseContext(), LoginActivity.class);
            mbundle.putString("productId", productId);
        }
        if(intent!=null){
            startActivity(intent);
        }
        //Toast.makeText(VenueViewActivity.this, "场地预约被点击了!", Toast.LENGTH_SHORT).show();
    }
    /**
     * 获取数据
     */
    private void getData(){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("productId", productId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_PRODUCT_INFO_DETAIL,
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
                        Log.e(TAG, "onSuccess: " + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsonArray = response.getJSONObject("data");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                product = JSON.parseObject(jsonArray.toString(), Product.class);
                            }
                            if (product != null) {
                                picList = new ArrayList<>();
                                String pics[] = product.getProductPic().split(",");
                                for (int i = 0; i < pics.length; i++) {
                                    picList.add(pics[i]);
                                    Log.e(TAG,"********************"+pics[i]);
                                }
                                productName.setText("商品名称："+product.getProductName());
                                eq_detail_content.setText("      " + product.getProductIntr());
                                eq_detail_productNum.setText("库存数量：" + product.getProductNum());
                                //eq_detail_productPrice.setText(product.getProductPrice());
                                eq_detail_productPrice.setText("￥"+product.getProductPrice());
                                eq_detail_ruling_Price.setText("￥"+product.getProductZkPrice());
                                eq_detail_tel.setText(product.getMerchantMobile());
                                eq_detail_addre.setText(product.getMerchantAdd());
                                baseAdapter = new VqViewAdapter(EqDetailActivity.this, picList, product.getMerchantId());
                                picListView.setAdapter(baseAdapter);
                                //vqViewAdapter.notifyDataSetChanged();
                            }
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(EqDetailActivity.this,"网络请求失败，请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class VqViewAdapter extends BaseAdapter {
        private Context context;
        private List<String> piclist;

        private String merchantId;


        public VqViewAdapter ( Context context, List<String> venuePicList,String merchantId){
            this.context = context;
            this.piclist = venuePicList;
            this.merchantId=merchantId;
        }
        @Override
        public int getCount() {
            return piclist.size();
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
            ImageView imageview;
            if (convertView == null) {
                imageview = new ImageView(context);
                imageview.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300));
                imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                imageview.setPadding(5, 5, 5, 5);
            } else{
                imageview=(ImageView) convertView;
            }
            Log.e(TAG, "position:" + piclist.get(position));
            ImageLoader.getInstance().displayImage(piclist.get(position), imageview);

            return imageview;
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
}
