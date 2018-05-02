package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.lzjs.uappoint.bean.Address;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DatePickDialogUtil;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.TimePickDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 物品购买页面
 * Created by wangdq on 2016/1/14.
 */
public class Eq_BuyActivity extends Activity implements View.OnClickListener {

    private TextView center_title_text;
    @ViewInject(R.id.buy_ddzq)
    private ToggleButton buy_ddzq;
    @ViewInject(R.id.buy_wlps)
    private ToggleButton buy_wlps;
    @ViewInject(R.id.buy_addre)
    private TextView buy_addre;
    @ViewInject(R.id.buy_num_equiment)
    private EditText buy_num_equiment;
    @ViewInject(R.id.buy_img)
    private ImageView buy_img;

    @ViewInject(R.id.select_img)
    private ImageView select_img;

    @ViewInject(R.id.buy_btn)
    private TextView buy_btn;

    @ViewInject(R.id.add_imagebutton)
    private ImageButton add_imagebutton;

    @ViewInject(R.id.sub_imagebutton)
    private ImageButton sub_imagebutton;
    @ViewInject(R.id.is_appoint_time)
    private ToggleButton is_appoint_time;
    @ViewInject(R.id.linear_uppiot_time)
    private LinearLayout linear_uppiot_time;
    @ViewInject(R.id.uppiont_date)
    private EditText uppiont_date;
    @ViewInject(R.id.uppiont_time)
    private EditText uppiont_time;

    @ViewInject(R.id.say_to_merchane)
    private EditText say_to_merchane;
    private String productId;
    private String userId;
    private String merchantId;
    private String merchantAdd;

    private Address address = null;

    private String eatMethod;
    private String result_value;
    private String productNum;

    private String user_upiont_time;//指定的时间
    private String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq_buy);
        ViewUtils.inject(this);
        productId = getIntent().getStringExtra("productId");
        userId = getIntent().getStringExtra("userId");
        merchantId = getIntent().getStringExtra("merchantId");
        merchantAdd = getIntent().getStringExtra("merchantAdd");
        productNum = getIntent().getStringExtra("productNum");
        center_title_text = (TextView) findViewById(R.id.center_title_text);
        center_title_text.setText("购买");
        buy_addre.setText("店铺地址：" + merchantAdd);
        eatMethod = "00A";//自取
        intiView();
        initData();
    }

    private void intiView() {
        buy_ddzq.setOnCheckedChangeListener(mtoggle);
        buy_wlps.setOnCheckedChangeListener(wltoggle);
        is_appoint_time.setOnCheckedChangeListener(is_uppiont);
        is_appoint_time.setChecked(false);
        linear_uppiot_time.setVisibility(View.GONE);
        select_img.setVisibility(View.GONE);
        select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Eq_BuyActivity.this, UserAddresListActivity.class);
                intent.putExtra("flag", "1");
                startActivityForResult(intent, 1000);
            }
        });
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_appoint_time.isChecked()) {
                    user_upiont_time=uppiont_date.getText().toString().trim()+" "+uppiont_time.getText().toString().trim();
                    saveData(user_upiont_time.trim());
                }else{
                    Date dateDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateString = formatter.format(dateDate);
                    saveData(dateString);
                }
            }
        });
        sub_imagebutton.setOnClickListener(this);
        add_imagebutton.setOnClickListener(this);

        uppiont_date.setOnClickListener(this);
        uppiont_date.setInputType(InputType.TYPE_NULL);
        uppiont_time.setOnClickListener(this);
        uppiont_time.setInputType(InputType.TYPE_NULL);
    }

    private ToggleButton.OnCheckedChangeListener mtoggle = new ToggleButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                select_img.setVisibility(View.GONE);
                buy_wlps.setChecked(false);
                buy_addre.setText("店铺地址：" + merchantAdd);
                eatMethod = "00A";//自取
            } else {
            }
        }

    };
    private ToggleButton.OnCheckedChangeListener wltoggle = new ToggleButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                buy_ddzq.setChecked(false);
                eatMethod = "00B";//配送
                if (address != null) {
                    buy_addre.setText("配送地址：" + address.getAddress());
                } else
                    buy_addre.setText("配送地址：" + result_value);
                select_img.setVisibility(View.VISIBLE);
            } else {
            }
        }

    };

    private ToggleButton.OnCheckedChangeListener is_uppiont = new ToggleButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                linear_uppiot_time.setVisibility(View.VISIBLE);
            } else {
                linear_uppiot_time.setVisibility(View.GONE);
            }
        }

    };


    private void initData() {
        RequestParams params = new RequestParams();
        String regiUserId = SharedUtils.getLoginUserId(this);
        params.addQueryStringParameter("regiUserId", regiUserId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_ADDRESS_DEFAULT,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsonData = response.getJSONObject("data");
                            address = JSON.parseObject(jsonData.toString(), Address.class);
                            addressId=address.getAddressId();


                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(Eq_BuyActivity.this, "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * 保存订单
     */
    private void saveData(String user_upiont_time) {
        RequestParams params = new RequestParams();
        String regiUserId = SharedUtils.getLoginUserId(this);
        params.addBodyParameter("regiUserId", regiUserId);
        params.addBodyParameter("merchantId", merchantId);
        params.addBodyParameter("orderPrice", "");
        params.addBodyParameter("eatMethod", eatMethod);
        params.addBodyParameter("payType", "00A");//默认00A 为支付宝
        params.addBodyParameter("friendAdd", buy_addre.getText().toString());
        params.addBodyParameter("favMoney", "");
        params.addBodyParameter("favMethod", "");
        params.addBodyParameter("remark",say_to_merchane.getText().toString().trim());//给商家的留言
        params.addBodyParameter("addressId",addressId);
        params.addBodyParameter("orderDate",user_upiont_time);
        String productlistInfo = productId + "@" + buy_num_equiment.getText().toString() + ",";
        params.addBodyParameter("productlistInfo", productlistInfo);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.SAVE_ORDER,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        buy_btn.setText("提交中.....");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsonData = response.getJSONObject("data");
                            if (jsonData.getBoolean("success")) {
                                Toast.makeText(Eq_BuyActivity.this, "下单成功!", Toast.LENGTH_SHORT).show();
                                String orderId = jsonData.getString("orderId");
                                Intent intent = new Intent(Eq_BuyActivity.this, OrderPayActivity.class);
                                Bundle mbundle = new Bundle();
                                mbundle.putString("eatMethod", eatMethod);
                                mbundle.putString("orderId", orderId);
                                mbundle.putString("merchantAdd", merchantAdd);
                                mbundle.putSerializable("addre", address);

                                intent.putExtras(mbundle);
                                buy_btn.setText("提交");
                                startActivity(intent);

                            } else {
                                Toast.makeText(Eq_BuyActivity.this, "下单失败!", Toast.LENGTH_SHORT).show();
                                buy_btn.setText("确认购买");
                            }

                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(Eq_BuyActivity.this, "提交失败!", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 200) {

            result_value = data.getStringExtra("result");
            addressId=data.getStringExtra("addressId");
            Log.e("result_value", result_value);
            buy_addre.setText("配送地址：" + result_value);
        }
    }

    /**
     * 头部返回
     *
     * @param view
     */
    @OnClick(R.id.title1_back)
    public void backBtnClick(View view) {
        finish();
    }

    /**
     * 数量按钮的点击监听
     */
    @Override
    public void onClick(View v) {

        int num = Integer.parseInt(buy_num_equiment.getText().toString());

        switch (v.getId()) {
            case R.id.add_imagebutton:
                if (Integer.parseInt(productNum) > num) {
                    num = num + 1;
                    buy_num_equiment.setText(num + "");
                } else {

                    Toast.makeText(Eq_BuyActivity.this, "抱歉，库存数量不足！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sub_imagebutton:
                if (num > 0) {
                    v.setClickable(true);
                    num = num - 1;
                    buy_num_equiment.setText(num + "");
                }
                break;
            case R.id.uppiont_date:
                chooseDate();
                break;
            case R.id.uppiont_time:
                chooseTime();
                break;

        }

    }

    private void chooseTime() {

        Calendar c1 = Calendar.getInstance();
        InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm1.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm1.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
        new TimePickDialogUtil(Eq_BuyActivity.this, 0, new TimePickDialogUtil.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker startTimePicker, int hour, int minute) {
                if(minute<10) {
                    String textString = String.format("%d:%d%d\n", hour, 0, minute);
                    uppiont_time.setText(textString);
                }else if(hour<10){
                    String textString = String.format("%d%d:%d\n",0, hour,minute);
                    uppiont_time.setText(textString);
                }else if(hour<10&&minute<10){
                    String textString = String.format("%d%d:%d%d\n",0, hour,0,minute);
                    uppiont_time.setText(textString);
                }else{
                    String textString = String.format("%d:%d\n", hour,minute);
                    uppiont_time.setText(textString);
                }
            }
        }, c1.get(Calendar.HOUR), c1.get(Calendar.MINUTE), true).show();
    }

    private void chooseDate() {
        Calendar c = Calendar.getInstance();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
        new DatePickDialogUtil(Eq_BuyActivity.this, 0, new DatePickDialogUtil.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                  int startDayOfMonth) {
                if (startMonthOfYear<9&&startDayOfMonth<10){
                    String textString = String.format("%d-%d%d-%d%d\n", startYear,
                            0,startMonthOfYear + 1, 0,startDayOfMonth);
                    uppiont_date.setText(textString);
                }else if (startMonthOfYear<9){
                    String textString = String.format("%d-%d%d-%d\n", startYear,
                            0,startMonthOfYear + 1,startDayOfMonth);
                    uppiont_date.setText(textString);
                }else if (startDayOfMonth<10){
                    String textString = String.format("%d-%d-%d%d\n", startYear,
                            startMonthOfYear + 1,0,startDayOfMonth);
                    uppiont_date.setText(textString);
                }else{
                    String textString = String.format("%d-%d-%d\n", startYear,
                            startMonthOfYear + 1,startDayOfMonth);
                    uppiont_date.setText(textString);
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
    }
}
