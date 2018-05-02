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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.lzjs.uappoint.bean.Venue;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 场地详细信息
 * shalei
 * 2016/1/14.
 */
public class VenueViewActivity extends Activity {

    private ListView venueGridView;
    private Venue venueEntity;
    private List<String> venuePicList;
    private VqViewAdapter vqViewAdapter;
    private TextView venueName;//场地名称
    private TextView venueIntr;//简介
    private TextView venueType;//场地性质
    private TextView venuePrice;//原价
    private TextView venueNum;//场地数量
    private TextView venueOpenTime;//开放时间
    private TextView venueAddress;//地址
    private TextView phoneNumer;//电话
    private TextView nowPrice;//现价
    private String venueId;

    //返回按钮
    private ImageView backImageView;
    public static final String TAG = "VenueViewActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_view);

        //修改标题名称
        TextView textView = (TextView)findViewById(R.id.center_title_text);
        textView.setText(R.string.title_activity_venue_view);
        venueId=getIntent().getStringExtra("venueId");
        Log.i(TAG, "onCreate: "+venueId);
        venueName = (TextView) findViewById(R.id.venueName);
        venueIntr = (TextView) findViewById(R.id.venueIntr);
        venueType = (TextView) findViewById(R.id.venueType);
        venuePrice = (TextView) findViewById(R.id.venue_price_yuanjia);
        venueNum = (TextView) findViewById(R.id.venueNum);
        venueOpenTime = (TextView) findViewById(R.id.venueOpenTime);
        phoneNumer= (TextView) findViewById(R.id.venue_phoneNumber);
        nowPrice= (TextView) findViewById(R.id.venue_price_xianjia);
        venueAddress = (TextView) findViewById(R.id.venueAddress);
        venuePrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        getData();


        venueGridView = (ListView) findViewById(R.id.venue_view_pic);
        venueGridView.setAdapter(vqViewAdapter);

        backImageView = (ImageView) findViewById(R.id.title1_back);
        //返回事件
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置crollView滚动至最顶端
        ScrollView sv = (ScrollView) findViewById(R.id.venueViewScroll);
        sv.smoothScrollTo(0, 0);

    }

    /**
     * 获取数据
     */
    private void getData(){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("venueId", venueId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_VENUE_INFO_DETAIL,
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
                        Log.i(TAG, "onSuccess: " + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsonArray = response.getJSONObject("data");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                venueEntity = JSON.parseObject(jsonArray.toString(), Venue.class);
                            }
                            if (venueEntity != null&&!venueEntity.getVenuePic().isEmpty()) {
                                venuePicList = new ArrayList<>();
                                String pics[] = venueEntity.getVenuePic().split(",");
                                for (int i = 0; i<pics.length; i++) {
                                    venuePicList.add(pics[i]);
                                }
                                venueName.setText("场地名称："+venueEntity.getVenueName());
                                venueIntr.setText(venueEntity.getVenueIntr());
                                venuePrice.setText("￥"+venueEntity.getVenuePrice());
                                venueNum.setText("数量："+venueEntity.getVenueNum());
                                venueOpenTime.setText(venueEntity.getOpenTime());
                                phoneNumer.setText(venueEntity.getVenueTel());
                                nowPrice.setText("￥"+venueEntity.getVenueZkPrice());
                                venueAddress.setText(venueEntity.getVenueAddress());
                                vqViewAdapter = new VqViewAdapter(VenueViewActivity.this,venuePicList,venueEntity.getMerchantId());
                                venueGridView.setAdapter(vqViewAdapter);
                                if ("00A".equals(venueEntity.getVenueType())) {
                                    venueType.setText("性质："+"木地板");
                                }else if ("00B".equals(venueEntity.getVenueType())) {
                                    venueType.setText("性质："+"塑胶");
                                }else if ("00C".equals(venueEntity.getVenueType())) {
                                    venueType.setText("性质："+"水泥");
                                }else if ("00D".equals(venueEntity.getVenueType())) {
                                    venueType.setText("性质："+"土质");
                                }else if ("00E".equals(venueEntity.getVenueType())) {
                                    venueType.setText("性质："+"草地");
                                }else if ("00F".equals(venueEntity.getVenueType())) {
                                    venueType.setText("性质："+"沙地");
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(VenueViewActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();

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
            return piclist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageview;
            //获取预约按钮
            Button venueReserve = (Button) findViewById(R.id.venueReserve);
            if (convertView == null) {
                imageview = new ImageView(context);
                imageview.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
                imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                imageview.setPadding(8, 8, 8, 8);
            } else{
                imageview=(ImageView) convertView;
            }
            Log.e(TAG, "position:" + position);
            ImageLoader.getInstance().displayImage(piclist.get(position), imageview);
            //注册点击事件
            venueReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=null;
                    Bundle mbundle=new Bundle();

                    //判断用户是否登录
                    String userId=SharedUtils.getLoginUserId(getBaseContext());
                    if(userId!=null&&!userId.equals("")){
                        intent=new Intent(getBaseContext(), VenueOrderActivity.class);
                        mbundle.putString("venueId", venueId);
                        mbundle.putString("userId", userId);
                        mbundle.putString("merchantId", merchantId);
                        intent.putExtras(mbundle);
                    }else{
                        intent=new Intent(getBaseContext(), LoginActivity.class);
                        mbundle.putString("venueId", venueId);
                    }
                    if(intent!=null){
                        startActivity(intent);
                    }
                    //Toast.makeText(VenueViewActivity.this, "场地预约被点击了!", Toast.LENGTH_SHORT).show();
                }
            });
            return imageview;
        }
    }
}
