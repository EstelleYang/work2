package com.lzjs.uappoint.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.ProductListOrderAdapter;
import com.lzjs.uappoint.alipay.PayResult;
import com.lzjs.uappoint.alipay.SignUtils;
import com.lzjs.uappoint.bean.Order;
import com.lzjs.uappoint.bean.OrderDetail;
import com.lzjs.uappoint.util.Contants;
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
 * 装备订单详细信息页面
 */
public class EquipmentDetailsActivity extends Activity implements View.OnClickListener{
    public static final String TAG=EquipmentDetailsActivity.class.getSimpleName();

    private TextView consignee;//收货人
    private TextView consignee_phone;//收货人电话
    private TextView textView13;//收货地址
    private TextView today_order_unit_name2;//商家名称
    private TextView today_order_trading_state2;//订单状态
    private TextView today_order_address4;//收货地址
    private TextView textView10;//实付价格
    private TextView textView15;//创建时间
    private TextView textView17;//订单号
    private TextView textView19;//取货方式
    private TextView today_order_number2;
    private String remark;
    private ImageView title1_back;//返回键
    private TextView center_title_text;
    private ListView myorder_listview;
    private ProductListOrderAdapter mAdapter;
    private int number=0;
    private List<OrderDetail> data;
    private RelativeLayout equiment_details_address;
    private RelativeLayout self_get;
    private TextView textView11;
    private Button btn_pay_order;
    private Order order;
    private static final int SDK_PAY_FLAG = 1;
    private StringBuffer sb;
    private boolean flag;
    private String  orderId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_details);

        initView();
        initData();
    }

    /***
     * 初始化控件
     */
    private void initView() {
        center_title_text= (TextView) findViewById(R.id.center_title_text);
        title1_back= (ImageView) findViewById(R.id.title1_back);
        consignee= (TextView) findViewById(R.id.consignee);
        consignee_phone= (TextView) findViewById(R.id.consignee_phone);
        textView13= (TextView) findViewById(R.id.textView13);
        today_order_unit_name2= (TextView) findViewById(R.id.today_order_unit_name2);
        today_order_trading_state2= (TextView) findViewById(R.id.today_order_trading_state2);
        today_order_address4= (TextView) findViewById(R.id.today_order_address4);
        self_get= (RelativeLayout) findViewById(R.id.self_get);
        textView10= (TextView) findViewById(R.id.textView10);
        textView15= (TextView) findViewById(R.id.textView15);
        textView17= (TextView) findViewById(R.id.textView17);
        textView19= (TextView) findViewById(R.id.textView19);
        today_order_number2= (TextView) findViewById(R.id.today_order_number2);
        myorder_listview= (ListView) findViewById(R.id.myorder_listview);
        equiment_details_address= (RelativeLayout) findViewById(R.id.equiment_details_address);
        textView11= (TextView) findViewById(R.id.textView11);
        btn_pay_order= (Button) findViewById(R.id.btn_pay_order);
        btn_pay_order.setOnClickListener(EquipmentDetailsActivity.this);
        sb=new StringBuffer();

        center_title_text.setText("订单详情");
     /**
       * 头部返回
       */
        title1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EquipmentDetailsActivity.this.finish();
            }
        });

    }
    private void initData() {
        mAdapter=new ProductListOrderAdapter(this);
        data=new ArrayList<>();
        Intent intent=getIntent();

        orderId = intent.getStringExtra("orderId");
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("orderId", orderId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.QUERY__USER_QNUEN_INFO,
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
                        Log.e(TAG, "onSuccess:::: " + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsondata = response.getJSONObject("data");

                            order = JSON.parseObject(jsondata.toString(), Order.class);
                            com.alibaba.fastjson.JSONArray array = jsondata.getJSONArray("orderDetails");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<OrderDetail>>() {
                            }.getType();
                            data = gson.fromJson(array.toString(), type);
                            mAdapter.setList(data);
                            myorder_listview.setAdapter(mAdapter);

                            for (int i=0;i<data.size();i++){
                                number+=Integer.parseInt(data.get(i).getVegetableNum());
                                sb.append(data.get(i).getProductName()+"、");
                            }
                            textView10.setText("￥"+order.getOrderPrice());
                            today_order_number2.setText(number+"");
                            remark = order.getRemark();
                            today_order_address4.setText(order.getFriendAdd());
                            today_order_unit_name2.setText(order.getMerchantName());
                            textView15.setText(order.getOrderDate());



                            //订单状态的判断
                            if ("00A".equals(order.getOrderStatus())) {
                                today_order_trading_state2.setText("未支付");
                                btn_pay_order.setText("去支付");
                            } else if ("00B".equals(order.getOrderStatus()))
                            {
                                today_order_trading_state2.setText("已支付");
                                btn_pay_order.setText("申请退款");
                            }  else if ("00H".equals(order.getOrderStatus()))
                            {
                                today_order_trading_state2.setText("退款中");
                                btn_pay_order.setVisibility(View.GONE);
                            } else if ("00I".equals(order.getOrderStatus()))
                            {
                                today_order_trading_state2.setText("已退款");
                                btn_pay_order.setVisibility(View.GONE);
                            }else if ("00C".equals(order.getOrderStatus()))
                            {
                                today_order_trading_state2.setText("待商家确认退款");
                                btn_pay_order.setText("待商家确认退款中...");
                                btn_pay_order.setClickable(false);
                            }
                            else if ("00F".equals(order.getOrderStatus()))
                            {
                                today_order_trading_state2.setText("已确认");
                                btn_pay_order.setText("商家已确认");
                                btn_pay_order.setClickable(false);
                            }else if ("00G".equals(order.getOrderStatus()))
                            {
                                today_order_trading_state2.setText("取消");
                                btn_pay_order.setVisibility(View.GONE);
                            }

                            //取货方式的判断
                            if ("00A".equals(order.getEatMethod())) {
                                textView19.setText("用户自取");
                                equiment_details_address.setVisibility(View.GONE);
                                self_get.setVisibility(View.GONE);
                            } else if ("00B".equals(order.getEatMethod())) {
                                textView19.setText("配送");
                                textView11.setText("收货人:");
                            }

                            textView17.setText(order.getOrderCode());
                        }

                        getAddress(remark);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(EquipmentDetailsActivity.this, "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    public void getAddress(String remark) {
        RequestParams params = new RequestParams();
        Log.e("remark", "remark" + remark);
        params.addBodyParameter("orderId", orderId);
        params.addBodyParameter("regiUserId", SharedUtils.getLoginUserId(this));

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_ORDER_ADDRESS,
                params,
                new RequestCallBack<String>(){
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("remark", "remark" + responseInfo.result.toString());
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (!object.isEmpty() && object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsondata = response.getJSONObject("data");
                            String phoneNumber = jsondata.getString("phoneNumber");
                            consignee_phone.setText(phoneNumber);
                            String realname = jsondata.getString("realName");
                            consignee.setText(realname);
                            String detailedAddress = jsondata.getString("addRess");
                            textView13.setText(detailedAddress);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(EquipmentDetailsActivity.this, "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_pay_order:
                if (order!=null) {
                    if ("00A".equals(order.getOrderStatus())) {
                        pay(v);
                    } else if ("00B".equals(order.getOrderStatus()))
                    {
                        RequestParams params=new RequestParams();
                        params.addQueryStringParameter("orderId",order.getOrderId());
                        Log.e(TAG, "orderId:" + order.getOrderId());
                        HttpUtils http=new HttpUtils();
                        http.send(HttpRequest.HttpMethod.POST,
                                Contants.APPLICATION_FOR_DRAWBACK,
                                params,
                                new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        JSONObject object = JSON.parseObject(responseInfo.result);
                                        if (object.getString("result").equals("200")) {
                                            JSONObject response = object.getJSONObject("Response");
                                            Log.e(TAG,"-----------"+response.getString("orderState"));
                                            if ("true".equals(response.getString("success"))) {
                                                btn_pay_order.setText("待商家确认退款中...");
                                                btn_pay_order.setClickable(false);
                                                flag=true;
                                            } else {
                                                Toast.makeText(EquipmentDetailsActivity.this, "申请退款失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {

                                    }
                                });

                    }
                }
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("flag",flag);
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        flag=savedInstanceState.getBoolean("flag");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

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
        //order.getOrderPrice()
        String orderInfo = getOrderInfo(sb.toString(), "该测试商品的详细描述", order.getOrderPrice(),order.getOrderId());

        Log.e(TAG,""+order.getProductName());

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
                PayTask alipay = new PayTask(EquipmentDetailsActivity.this);
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
                        btn_pay_order.setText("支付成功");
                        btn_pay_order.setClickable(false);
                        Toast.makeText(EquipmentDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(EquipmentDetailsActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(EquipmentDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

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

}
