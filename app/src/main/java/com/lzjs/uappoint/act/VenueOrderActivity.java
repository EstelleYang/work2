package com.lzjs.uappoint.act;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.lzjs.uappoint.util.DatePickDialogUtil;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.StringUtils;
import com.lzjs.uappoint.util.TimeHelper;
import com.lzjs.uappoint.util.TimePickDialogUtil;

import java.util.Calendar;

/**
* 场地订单确认
* shalei 2016-01-18
*/

public class VenueOrderActivity extends Activity implements Handler.Callback{

    private  static  String TAG="VenueOrderActivity";

    private FrameLayout venueOrderConfirm;
    private EditText sDate;
    private EditText eDate;
    private EditText sTime;
    private EditText eTime;
    private EditText orderContent;
    //定义获取到的页面输入信息
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String content;

    private String venueId;
    private String merchantId;
    private String orderId;
    private Handler mHandler;

    //返回按钮
    private ImageView backImageView;
    private String initStartDate = TimeHelper.getCurrentDate(); // 初始化开始时间
    private String initEndDate = TimeHelper.getCurrentDate(); // 初始化结束时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_order);

        venueId=getIntent().getStringExtra("venueId");
        merchantId=getIntent().getStringExtra("merchantId");
        TextView textView = (TextView)findViewById(R.id.center_title_text);
        textView.setText(R.string.title_activity_venue_order);
        mHandler=new Handler(this);
        venueOrderConfirm = (FrameLayout) findViewById(R.id.venueOrderConfirm);
        //设置事件触发
        venueOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDate();
            }
        });
        //返回事件
        backImageView = (ImageView) findViewById(R.id.title1_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               VenueOrderActivity.this.finish();
            }
        });
        sDate = (EditText) findViewById(R.id.sDate);
        sDate.setInputType(InputType.TYPE_NULL);
        eDate = (EditText) findViewById(R.id.eDate);
        eDate.setInputType(InputType.TYPE_NULL);
        eDate.clearFocus();
        sTime = (EditText) findViewById(R.id.sTime);
        sTime.setInputType(InputType.TYPE_NULL);
        eTime = (EditText) findViewById(R.id.eTime);
        eTime.setInputType(InputType.TYPE_NULL);
        orderContent = (EditText) findViewById(R.id.orderContent);
        //日期弹出层
        sDate.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                //取消键盘事件
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()&&getCurrentFocus()!=null){
                    if (getCurrentFocus().getWindowToken()!=null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DatePickDialogUtil(VenueOrderActivity.this, 0, new DatePickDialogUtil.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        if (startMonthOfYear<9&&startDayOfMonth<10){
                            String textString = String.format("%d-%d%d-%d%d\n", startYear,
                                    0,startMonthOfYear + 1, 0,startDayOfMonth);
                            sDate.setText(textString);
                        }else if (startMonthOfYear<9){
                            String textString = String.format("%d-%d%d-%d\n", startYear,
                                    0,startMonthOfYear + 1,startDayOfMonth);
                            sDate.setText(textString);
                        }else if (startDayOfMonth<10){
                            String textString = String.format("%d-%d-%d%d\n", startYear,
                                    startMonthOfYear + 1,0,startDayOfMonth);
                            sDate.setText(textString);
                        }else{
                            String textString = String.format("%d-%d-%d\n", startYear,
                                    startMonthOfYear + 1,startDayOfMonth);
                            sDate.setText(textString);
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                //取消键盘事件
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()&&getCurrentFocus()!=null){
                    if (getCurrentFocus().getWindowToken()!=null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DatePickDialogUtil(VenueOrderActivity.this, 0, new DatePickDialogUtil.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        if (startMonthOfYear<9&&startDayOfMonth<10){
                            String textString = String.format("%d-%d%d-%d%d\n", startYear,
                                    0,startMonthOfYear + 1, 0,startDayOfMonth);
                            eDate.setText(textString);
                        }else if (startMonthOfYear<9){
                            String textString = String.format("%d-%d%d-%d\n", startYear,
                                    0,startMonthOfYear + 1,startDayOfMonth);
                            eDate.setText(textString);
                        }else if (startDayOfMonth<10){
                            String textString = String.format("%d-%d-%d%d\n", startYear,
                                    startMonthOfYear + 1,0,startDayOfMonth);
                            eDate.setText(textString);
                        }else{
                            String textString = String.format("%d-%d-%d\n", startYear,
                                    startMonthOfYear + 1,startDayOfMonth);
                            eDate.setText(textString);
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
            }
        });

        sTime.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                //取消键盘事件
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()&&getCurrentFocus()!=null){
                    if (getCurrentFocus().getWindowToken()!=null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new TimePickDialogUtil(VenueOrderActivity.this, 0, new TimePickDialogUtil.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker startTimePicker, int hour, int minute) {
                        if(minute<10) {
                            String textString = String.format("%d:%d%d\n", hour, 0, minute);
                            sTime.setText(textString);
                        }else if(hour<10){
                            String textString = String.format("%d%d:%d\n",0, hour,minute);
                            sTime.setText(textString);
                        }else if(hour<10&&minute<10){
                            String textString = String.format("%d%d:%d%d\n",0, hour,0,minute);
                            sTime.setText(textString);
                        }else{
                            String textString = String.format("%d:%d\n", hour,minute);
                            sTime.setText(textString);
                        }
                    }
                }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true).show();
            }
        });

        eTime.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                //取消键盘事件
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()&&getCurrentFocus()!=null){
                    if (getCurrentFocus().getWindowToken()!=null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new TimePickDialogUtil(VenueOrderActivity.this, 0, new TimePickDialogUtil.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker startTimePicker, int hour, int minute) {
                        if(minute<10) {
                            String textString = String.format("%d:%d%d\n", hour, 0, minute);
                            eTime.setText(textString);
                        }else if(hour<10){
                            String textString = String.format("%d%d:%d\n",0, hour,minute);
                            eTime.setText(textString);
                        }else if(hour<10&&minute<10){
                            String textString = String.format("%d%d:%d%d\n",0, hour,0,minute);
                            eTime.setText(textString);
                        }else{
                            String textString = String.format("%d:%d\n", hour,minute);
                            eTime.setText(textString);
                        }
                    }
                }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true).show();
            }
        });
    }

    /**
     * 保存数据
     */
    public void saveDate(){
        startDate = sDate.getText().toString();
        endDate = eDate.getText().toString();
        startTime = sTime.getText().toString();
        endTime = eTime.getText().toString();
        content = orderContent.getText().toString();
        String regiUserId= SharedUtils.getLoginUserId(VenueOrderActivity.this);
        if(validateData()) {
            //调用接口保存订单
            RequestParams params = new RequestParams();
            params.addBodyParameter("venueId", venueId);
            params.addBodyParameter("regiUserId", regiUserId);
            params.addBodyParameter("merchantId", merchantId);
            params.addBodyParameter("orderSDate", startDate.trim());
            params.addBodyParameter("orderSTime", startTime.trim());
            params.addBodyParameter("orderEDate", endDate.trim());
            params.addBodyParameter("orderETime", endTime.trim());
            params.addBodyParameter("orderContent", content.trim());
            Log.e("************","orderSDate"+startDate);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.SAVE_VENUE_ORDER,
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
                            Log.e("-----------", "onSuccess:---------" + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("Response");
                                JSONObject jsondata = response.getJSONObject("data");

                                orderId = response.getString("orderId");
                                if(jsondata.getBoolean("success")){
                                    Toast.makeText(VenueOrderActivity.this, "场地预定成功!", Toast.LENGTH_SHORT).show();

                                    mHandler.sendEmptyMessage(0);
                                }else{
                                    Toast.makeText(VenueOrderActivity.this, "场地预定失败!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(VenueOrderActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{
            Toast.makeText(VenueOrderActivity.this, "请输入正确的信息!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 数据验证
     * @return
     */
    public boolean validateData(){
        if(StringUtils.isEmpty(startDate)){
            Toast.makeText(VenueOrderActivity.this, "开始日期不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(endDate)){
            Toast.makeText(VenueOrderActivity.this, "结束日期不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(startTime)){
            Toast.makeText(VenueOrderActivity.this, "开始时间不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(endTime)){
            Toast.makeText(VenueOrderActivity.this, "结束时间不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 0:
                Intent intent=new Intent(VenueOrderActivity.this,VenueDetailActivity.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
