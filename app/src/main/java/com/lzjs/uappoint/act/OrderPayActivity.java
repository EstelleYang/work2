package com.lzjs.uappoint.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.lzjs.uappoint.adapter.ProductListOrderAdapter;
import com.lzjs.uappoint.alipay.PayResult;
import com.lzjs.uappoint.alipay.SignUtils;
import com.lzjs.uappoint.bean.Address;
import com.lzjs.uappoint.bean.OrderDetail;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * 去支付页面
 * by wangdq 2016年3月8日14:47:59
 */
public class OrderPayActivity extends Activity {

    private static final int SDK_PAY_FLAG = 1;
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;
    private String eatMethod;
    private String orderId;
    private String merchantAdd;
    private Address addre;
    private OrderDetail orderDetail;
    @ViewInject(R.id.pay_addre_ps)
    private LinearLayout pay_addre_ps;
    @ViewInject(R.id.pay_addre_zq)
    private LinearLayout pay_addre_zq;
    @ViewInject(R.id.addre_merchan)
    private TextView addre_merchan;
    @ViewInject(R.id.addre_name)
    private TextView addre_name ;
    @ViewInject(R.id.addre_tel)
    private TextView addre_tel ;
    @ViewInject(R.id.pay_p_business)
    private TextView pay_p_business;
//    @ViewInject(R.id.pay_business_1)
//    private TextView pay_business_1;
//    @ViewInject(R.id.pay_price)
//    private TextView pay_price;
//    @ViewInject(R.id.pay_p_name)
//    private TextView pay_p_name;
//    @ViewInject(R.id.pay_buyNum)
//    private TextView pay_buyNum;
    @ViewInject(R.id.pay_total)
    private TextView pay_total;
    @ViewInject(R.id.addre_detail)
    private TextView addre_detail;
    @ViewInject(R.id.order_list_listView)
    private ListView order_list_listView;
//    @ViewInject(R.id.pay_order_pic)
//    private ImageView pay_order_pic;
    @ViewInject(R.id.btn_pay)
    private Button btn_pay;
    @ViewInject(R.id.tv_number)
    private TextView tv_number;
    private List<OrderDetail> data;
    private ProductListOrderAdapter mAdapter;

    private int number=0;
    private double totalPrice=0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        addre= (Address) getIntent().getSerializableExtra("addre");
        eatMethod=getIntent().getStringExtra("eatMethod");
        orderId=getIntent().getStringExtra("orderId");
        merchantAdd=getIntent().getStringExtra("merchantAdd");
        ViewUtils.inject(this);
        center_title_text.setText("确认支付");
        initData();
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(v);
            }
        });
    }
    private void initView(){
        if(eatMethod.equals("00B")){
            pay_addre_ps.setVisibility(View.VISIBLE);
            pay_addre_zq.setVisibility(View.GONE);
            RequestParams params=new RequestParams();
            params.addBodyParameter("orderId", orderId);
            params.addBodyParameter("regiUserId", SharedUtils.getLoginUserId(this));
            HttpUtils http=new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.GET_ORDER_ADDRESS,
                    params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("Response");
                                JSONObject jsonData = response.getJSONObject("data");

                                addre_name.setText(jsonData.getString("realName"));
                                addre_tel.setText(jsonData.getString("phoneNumber"));
                                addre_detail.setText(jsonData.getString("addRess"));
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });



        }else{
            pay_addre_ps.setVisibility(View.GONE);
            pay_addre_zq.setVisibility(View.VISIBLE);
            addre_merchan.setText(merchantAdd);
        }


     if(orderDetail!=null){
//            ImageLoader.getInstance().displayImage(orderDetail.getProductPic(), pay_order_pic);
            pay_p_business.setText(orderDetail.getMerchantName());
//            pay_business_1.setText(orderDetail.getProductName());
//            pay_price.setText("￥"+orderDetail.getVegetablePrice());
//            pay_buyNum.setText("x"+orderDetail.getVegetableNum());
//            pay_total.setText("￥"+orderDetail.getVegetableTotal());
        }
   }

    private void initData(){
        data=new ArrayList<>();
        mAdapter=new ProductListOrderAdapter(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("orderId", orderId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.ORDER_DETAIL,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("***","***"+responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsonData = response.getJSONObject("data");
                            JSONArray jsonArray=response.getJSONArray("datas");
                            //目前只取第一条记录
                            if(jsonArray.size()>0)
                                orderDetail = JSON.parseObject(jsonArray.getJSONObject(0).toString(), OrderDetail.class);
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<OrderDetail>>() {
                            }.getType();
                            data = gson.fromJson(jsonArray.toString(), type);
                            mAdapter.setList(data);
                            order_list_listView.setAdapter(mAdapter);

                            for (int i=0;i<data.size();i++){
                                number+=Integer.parseInt(data.get(i).getVegetableNum());
                                totalPrice+=Double.parseDouble(data.get(i).getVegetableTotal());
                            }
                            tv_number.setText("共"+number+"件");
                            pay_total.setText("￥："+totalPrice);
                            initView();
                        }
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(OrderPayActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        btn_pay.setText("支付成功");
                        btn_pay.setClickable(false);
                        Toast.makeText(OrderPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(Contants.PARTNER) || TextUtils.isEmpty(Contants.RSA_PRIVATE) || TextUtils.isEmpty(Contants.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        //totalPrice+""
        String orderInfo = getOrderInfo(orderDetail.getProductName(), "该测试商品的详细描述",totalPrice+"" ,orderDetail.getOrderId());

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderPayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price,String orderNo) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Contants.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Contants.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + Contants.NOTIFY_URL + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, Contants.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
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
