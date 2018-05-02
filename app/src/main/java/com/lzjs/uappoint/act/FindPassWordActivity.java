package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.lzjs.uappoint.util.StringUtils;

public class FindPassWordActivity extends Activity implements View.OnClickListener,Handler.Callback{
    public static final String TAG=FindPassWordActivity.class.getSimpleName();

    private EditText input_phone_number_find;//输入电话号码
    private Button verification_number_button_find;//获取验证码
    private EditText input_verification_number_find;//输入验证码
    private EditText set_password_find;//设置新密码
    private TextView user_info_change_password_confirm_find;//确认
    private Handler mHandler=null;
    private ImageView title1_back;
    private TextView center_title_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        input_phone_number_find= (EditText) findViewById(R.id.input_phone_number_find);
        verification_number_button_find= (Button) findViewById(R.id.verification_number_button_find);
        input_verification_number_find= (EditText) findViewById(R.id.input_verification_number_find);
        set_password_find= (EditText) findViewById(R.id.set_password_find);
        user_info_change_password_confirm_find= (TextView) findViewById(R.id.user_info_change_password_confirm_find);
        title1_back= (ImageView) findViewById(R.id.title1_back);
        center_title_text= (TextView) findViewById(R.id.center_title_text);
        verification_number_button_find.setOnClickListener(this);
        user_info_change_password_confirm_find.setOnClickListener(this);
        title1_back.setOnClickListener(this);
        mHandler=new Handler(this);
        center_title_text.setText("找回密码");


    }

    /**
      *点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

             //获取验证码
            case R.id.verification_number_button_find:
                verification_number_button_find.setClickable(false);
                verification_number_button_find.setTextColor(0x88000000);
                IdentifyingCode();
                break;
            //确认
            case R.id.user_info_change_password_confirm_find:
                check();
                break;
            //头部返回
            case R.id.title1_back:
                finish();
                break;
        }
    }
    /**
     * 获取短信验证码
     */
    public void IdentifyingCode(){
        String mobile=input_phone_number_find.getText().toString();
        if(StringUtils.isMobileNO(mobile)){
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("mobile", mobile);
            params.addQueryStringParameter("userType", "00B");
            params.addQueryStringParameter("smsSendType", "00C");
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.GET_VALIDATE_CODE,
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
                            Log.e(TAG, "onSuccess:+++++ " + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 60; i >= 0; i--) {
                                            Message message = Message.obtain();
                                            message.arg1 = i;
                                            message.what = 1;
                                            mHandler.sendMessage(message);
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(FindPassWordActivity.this, "系统异常，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else
            Toast.makeText(FindPassWordActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
    }


    /**
     * 判断验证码
     * @return
     */
    boolean flag=false;
    private void check(){
        String mobile=input_phone_number_find.getText().toString();
        String vcode=input_verification_number_find.getText().toString();
        if (TextUtils.isEmpty(mobile) ||TextUtils.isEmpty(vcode)){
            Toast.makeText(FindPassWordActivity.this, "手机号或验证码不能为空！", Toast.LENGTH_SHORT).show();
        }
        else{
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("mobile", mobile);
            params.addQueryStringParameter("vcode", vcode);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.JUDGMENT_VALIDATE_CODE,
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
                            Log.e(TAG, "onSuccess:----- " + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("Response");
                                JSONObject jsondata = response.getJSONObject("data");
                                if(jsondata.getBoolean("success")){
                                    findPassWord();
                                }else{
                                    Toast.makeText(FindPassWordActivity.this, "手机号或验证码错误！", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(FindPassWordActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
    /**
     * 找回密码
     */
    public void findPassWord(){

        String mobile=input_phone_number_find.getText().toString();
        String newPwd=set_password_find.getText().toString();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("pwd",newPwd);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.FIND_USER_PASSWORD,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        user_info_change_password_confirm_find.setText("正在找回...");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i(TAG, "onSuccess:===== " + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsondata = response.getJSONObject("data");
                            if(jsondata.getBoolean("success")){
                                Toast.makeText(FindPassWordActivity.this, "找回密码成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                finish();
                            }else
                                Toast.makeText(FindPassWordActivity.this, "找回密码失败", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(FindPassWordActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                if (msg.arg1 == 0) {
                    verification_number_button_find.setText("重新获取");
                    verification_number_button_find.setClickable(true);
                    verification_number_button_find.setTextColor(0xff000000);
                } else {

                    verification_number_button_find.setText(msg.arg1+ "秒后重新获取");
                }

                break;
        }

        return true;
    }
}
