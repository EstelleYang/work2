package com.lzjs.uappoint.act;

import android.app.Activity;
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

public class UserPassWordActivity extends Activity implements View.OnClickListener,Handler.Callback{
    public static final String TAG=UserPassWordActivity.class.getSimpleName();
    private Handler mHandler=null;

    //获取验证码
    private Button verification_number_button;
    //输入手机号码
    private EditText change_password_input_phone_number;
    //输入验证码
    private EditText input_verification_number;
    //输入旧密码
    private EditText input_old_pwd;
    //输入新密码
    private EditText set_password;
    //确认修改
    private Button user_info_change_password_confirm;

    //返回按钮
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pass_word);
        TextView titalTv= (TextView) findViewById(R.id.center_title_text);
        titalTv.setText("修改密码");

        initView();
    }
    /**初始化控件*/
    private void initView() {
        mHandler=new Handler(this);
        verification_number_button= (Button) findViewById(R.id.verification_number_button);
        change_password_input_phone_number= (EditText) findViewById(R.id.change_password_input_phone_number);
        input_verification_number= (EditText) findViewById(R.id.input_verification_number);
        input_old_pwd= (EditText) findViewById(R.id.input_old_pwd);
        set_password= (EditText) findViewById(R.id.set_password);
        user_info_change_password_confirm= (Button) findViewById(R.id.user_info_change_password_confirm);

        verification_number_button.setOnClickListener(this);
        user_info_change_password_confirm.setOnClickListener(this);

        backImageView = (ImageView) findViewById(R.id.title1_back);
        //返回事件
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPassWordActivity.this.finish();
            }
        });
    }

    /**
     * 获取验证码以及确认修改的点击监听
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.verification_number_button:
                verification_number_button.setClickable(false);
                verification_number_button.setTextColor(0x88000000);
                IdentifyingCode();
                break;

            case R.id.user_info_change_password_confirm:

                check();

                break;
        }

    }


    /**
     * 获取短信验证码
     */
    public void IdentifyingCode(){
        String mobile=change_password_input_phone_number.getText().toString();
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
                            Log.e(TAG, "onSuccess: " + responseInfo.result);
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
                            Toast.makeText(UserPassWordActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else
            Toast.makeText(UserPassWordActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断验证码
     * @return
     */
    boolean flag=false;
    private void check(){
        String mobile=change_password_input_phone_number.getText().toString();
        String vcode=input_verification_number.getText().toString();
        if (TextUtils.isEmpty(mobile) ||TextUtils.isEmpty(vcode)){
            Toast.makeText(UserPassWordActivity.this, "手机号或验证码不能为空！", Toast.LENGTH_SHORT).show();
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
                            user_info_change_password_confirm.setText("修改中...");

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
                                JSONObject jsondata = response.getJSONObject("data");
                                if(jsondata.getBoolean("success")){
                                    revisePwd();
                                }else{
                                    Toast.makeText(UserPassWordActivity.this, "手机号或验证码错误！", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(UserPassWordActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    /**
     * 修改密码
     */
    public void revisePwd(){

        String mobile=change_password_input_phone_number.getText().toString();
        String oldPwd=input_old_pwd.getText().toString();
        String newPwd=set_password.getText().toString();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("oldPwd", oldPwd);
        params.addQueryStringParameter("newPwd",newPwd);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.USER_RESIVE_PWD,
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
                            JSONObject jsondata = response.getJSONObject("data");
                            if(jsondata.getBoolean("success")){
                                Toast.makeText(UserPassWordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                finish();
                            }else
                                Toast.makeText(UserPassWordActivity.this, "修改失败，旧密码错误", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(UserPassWordActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                if (msg.arg1 == 0) {
                    verification_number_button.setText("重新获取");
                    verification_number_button.setClickable(true);
                    verification_number_button.setTextColor(0xff000000);
                } else {

                    verification_number_button.setText(msg.arg1 + "秒后重新获取");
                  // verification_number_button.setBackground(getResources().getDrawable(R.drawable.button));
                }

                break;
        }
        return true;
    }
}
