package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.util.Contants;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VenueDetailActivity extends Activity implements View.OnClickListener{

    private TextView merchaneName_venue;
    private TextView merchantPhone_venue;
    private TextView address_venue;
    private TextView name_venue;
    private TextView price_venue;
    private TextView num_venue;
    private ImageView pic_venue;
    private TextView start_date_venue;
    private TextView end_date_venue;
    private TextView title;
    private ImageView title1_back;
    private TextView order_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);
        initView();
        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        final String orderId=intent.getStringExtra("orderId");
        RequestParams params=new RequestParams();
        params.addQueryStringParameter("orderId",orderId);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Contants.VENUE_ORDER_DETIAL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject object = JSON.parseObject(responseInfo.result);
                Log.e("&&&&&&&&&&&","&&&&&&&&&&&"+responseInfo.result);
                if (object.getString("result").equals("200")) {
                    JSONObject response = object.getJSONObject("Response");
                    JSONObject jsondata = response.getJSONObject("data");
                    merchaneName_venue.setText(jsondata.getString("merchantName"));
                    name_venue.setText(jsondata.getString("venueName"));
                    merchantPhone_venue.setText(jsondata.getString("merchantMobile"));
                    address_venue.setText(jsondata.getString("merchantAddress"));
                    price_venue.setText("￥"+jsondata.getString("venueZkPrice"));
                    start_date_venue.setText(jsondata.getString("orderSDate")+" "+jsondata.getString("orderSTime"));
                    end_date_venue.setText(jsondata.getString("orderEDate")+" "+jsondata.getString("orderETime"));
                    num_venue.setText("x1");
                    ImageLoader.getInstance().displayImage(jsondata.getString("merchantPic"),pic_venue);
                    order_info.setText("订单已提交，等待商家确认中。。。");


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(VenueDetailActivity.this, "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        merchaneName_venue= (TextView) findViewById(R.id.merchantName_venue);
        merchantPhone_venue= (TextView) findViewById(R.id.merchantPhone_venue);
        address_venue= (TextView) findViewById(R.id.address_venue);
        name_venue= (TextView) findViewById(R.id.name_venue);
        price_venue= (TextView) findViewById(R.id.price_venue);
        num_venue= (TextView) findViewById(R.id.num_venue);
        pic_venue= (ImageView) findViewById(R.id.pic_venue);
        start_date_venue= (TextView) findViewById(R.id.start_date_venue);
        end_date_venue= (TextView) findViewById(R.id.end_date_venue);
        title= (TextView) findViewById(R.id.center_title_text);
        title1_back= (ImageView) findViewById(R.id.title1_back);
        order_info= (TextView) findViewById(R.id.order_info);
        title1_back.setOnClickListener(this);
        title.setText("详情");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title1_back:
                finish();
                break;
        }

    }
}
