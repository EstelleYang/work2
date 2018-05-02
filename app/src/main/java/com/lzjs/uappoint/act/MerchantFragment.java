package com.lzjs.uappoint.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.util.CheckConnectionUtils;
import com.lzjs.uappoint.util.Contants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @description 商家信息
 * @author wangdq
 */
public class MerchantFragment extends Fragment implements Handler.Callback{

    private static  String TAG="MerchantFragment";

    private static String merchantId;

    private static OkHttpClient client = new OkHttpClient();
    /**
     * 在这里直接设置连接超时，静态方法内，在构造方法被调用前就已经初始话了
     */
    /*static {
        client.newBuilder().connectTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(100, TimeUnit.SECONDS);
    }*/

    private Request request;
    private TextView tvMname;//商家名称
    private TextView tvMaddre;//商家地址
    private TextView tvMmobile;//商家电话
    private TextView tvActivity;//活动内容
    private TextView tvState;//商家状态
    private TextView tvFavorable;//商家优惠
    private TextView tvInfo;//商家简介
    private TextView merchane_special;//商家特色内容
    private ImageView imageView_card1;//商家证书
    private ImageView imageView_card2;
    private ImageView imageView_card3;

    private ImageView imageView1;//商家上传的剩余的俩张图片
    private ImageView imageView2;
    private String merchanePic;//商家图片
    private String merchaneCard;

    private Handler mHandler;




    private JSONObject objdata;
    public static MerchantFragment newInstance(String merchantId){
        MerchantFragment fragment = new MerchantFragment();
        //fragment.title=title;
        MerchantFragment.merchantId=merchantId;
        return  fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.activity_merchant_fragment,container,false);
        tvMname= (TextView) view.findViewById(R.id.merchantName);
        tvMaddre=(TextView)view.findViewById(R.id.merchant_addre);
        tvMmobile=(TextView)view.findViewById(R.id.merchant_mobile);
        tvActivity= (TextView) view.findViewById(R.id.activity_content);
        tvState= (TextView) view.findViewById(R.id.merchant_state);
        tvFavorable= (TextView) view.findViewById(R.id.favorable);
        tvInfo= (TextView) view.findViewById(R.id.merchan_infomation);
        imageView1= (ImageView) view.findViewById(R.id.imageView_1);
        imageView2= (ImageView) view.findViewById(R.id.imageView_2);
        merchane_special= (TextView) view.findViewById(R.id.merchane_special);
        imageView_card1= (ImageView) view.findViewById(R.id.imageView_card1);
        imageView_card2= (ImageView) view.findViewById(R.id.imageView_card2);
        imageView_card3= (ImageView) view.findViewById(R.id.imageView_card3);

        mHandler=new Handler(this);

        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getActivity())){
            getData();
        }else {
            Toast.makeText(getActivity(), "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void getData(){
        Log.e(TAG,"************"+merchantId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder()
                            .add("merchantId", merchantId)
                            .build();
                    request = new Request.Builder().url(Contants.GET_MERCHANT_INFO).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.e(TAG, "*********** " + result);
                    Log.e(TAG, "*********** " + merchantId);

                    if(!result.isEmpty() && result.indexOf("}")!=-1){
                        JSONObject obj = JSONObject.parseObject(result);
                        JSONObject objres=obj.getJSONObject("Response");
                        objdata = objres.getJSONObject("data");
                        if(objdata != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvMaddre.setText(objdata.getString("merchantAdd").trim());
                                    tvMmobile.setText(objdata.getString("merchantMobile").trim());
                                    tvMname.setText(objdata.getString("merchantName").trim());
                                    tvInfo.setText(objdata.getString("merchantIntr").trim());
                                   tvFavorable.setText(objdata.getString("merchantActivDesc").trim());
                                    tvActivity.setText(objdata.getString("merchantActivName").trim());
                                   tvState.setText(objdata.getString("merchantStatus").trim());
                                    merchanePic=objdata.getString("merchantPic").trim();
                                    merchane_special.setText(objdata.getString("merchantFeat"));
                                    merchaneCard=objdata.getString("merchantIdqualCert");
                                    String pic[]=merchanePic.split(",");
                                    String cards[]=merchaneCard.split(",");
                                    if (cards.length>0){
                                        switch (cards.length){
                                            case 1:
                                                ImageLoader.getInstance().displayImage(cards[0],imageView_card1);
                                                mHandler.sendEmptyMessage(3);
                                                break;
                                            case 2:
                                                ImageLoader.getInstance().displayImage(cards[0],imageView_card1);
                                                ImageLoader.getInstance().displayImage(cards[1],imageView_card2);
                                                mHandler.sendEmptyMessage(4);
                                                break;
                                            case 3:
                                                ImageLoader.getInstance().displayImage(cards[0],imageView_card1);
                                                ImageLoader.getInstance().displayImage(cards[1],imageView_card2);
                                                ImageLoader.getInstance().displayImage(cards[2],imageView_card3);

                                                break;
                                        }
                                    }else{
                                        mHandler.sendEmptyMessage(5);
                                    }
                                    if (pic.length>1){
                                        if (pic.length==2){
                                            ImageLoader.getInstance().displayImage(pic[1],imageView1);
                                            mHandler.sendEmptyMessage(0);
                                        }else if (pic.length==3){
                                            ImageLoader.getInstance().displayImage(pic[1],imageView1);
                                            ImageLoader.getInstance().displayImage(pic[2],imageView2);
                                        }

                                    }else {
                                        mHandler.sendEmptyMessage(1);
                                    }
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

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what){
            case 0:
                imageView2.setVisibility(View.GONE);
                break;
            case 1:
                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                break;
            case 3:
                imageView_card2.setVisibility(View.GONE);
                imageView_card3.setVisibility(View.GONE);
                break;
            case 4:
                imageView_card3.setVisibility(View.GONE);
                break;
            case 5:
                imageView_card1.setVisibility(View.GONE);
                imageView_card2.setVisibility(View.GONE);
                imageView_card3.setVisibility(View.GONE);
                break;
        }
        return true;
    }
}
