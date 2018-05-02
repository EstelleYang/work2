package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.lzjs.uappoint.bean.UserInfo;
import com.lzjs.uappoint.util.AllCapTransformationMethod;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.StringUtils;
import com.lzjs.uappoint.util.ToolsUtil;

public class LoginActivity extends Activity implements View.OnClickListener{

    private static final String TAG ="LoginActivity";
    private TextView tv_userRegi;
    private EditText loginid;
    private EditText loginpwd;
    private TextView tv_findPwd;
    private TextView tv_login;
    private TextView tv_tooltip;
    //private TextView center_title_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        initView();
    }

    private void initView() {
        //center_title_text=(TextView)findViewById(R.id.center_title_text);
        //center_title_text.setText(R.string.login_activity_tital);
        tv_userRegi= (TextView) findViewById(R.id.tv_userRegi);
        loginid= (EditText) findViewById(R.id.loginid);
        loginpwd= (EditText) findViewById(R.id.loginpwd);
        tv_findPwd= (TextView) findViewById(R.id.tv_findPwd);
        tv_login= (TextView) findViewById(R.id.tv_login);
        tv_findPwd.setOnClickListener(this);
        tv_userRegi.setOnClickListener(this);
        tv_tooltip=(TextView) findViewById(R.id.tv_tooltip) ;

        /*loginid.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X' };
                return numberChars;
            }
        });
        loginid.setTransformationMethod(new AllCapTransformationMethod(true));  //自动转换小写为大写*/

    }

    /**
     * 登录
     */
    public void getUserLoginBtnClick(View view){
        String id=loginid.getText().toString();
        String pwd=loginpwd.getText().toString();
        if (!StringUtils.isEmpty(id)&&!StringUtils.isEmpty(pwd)){
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("userid", id);
            params.addQueryStringParameter("userpwd", ToolsUtil.md5Encode(id+pwd));
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.USER_LOGIN,
                    params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onStart() {
                            tv_login.setText("登录中...");
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {

                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            try {
                                Log.i(TAG, "onSuccess: " + responseInfo.result);
                                JSONObject object = JSON.parseObject(responseInfo.result);
                                if (object.getString("result").equals("200")) {
                                    JSONObject response = object.getJSONObject("response");
                                    JSONObject jsondata = response.getJSONObject("data");
                                    String signature=jsondata.getString("signature");
                                    UserInfo user=JSON.parseObject(jsondata.getString("responseData"),UserInfo.class);
                                    if (user!=null && !TextUtils.isEmpty(signature)){
                                        Toast.makeText(LoginActivity.this, "登录成功！"+signature, Toast.LENGTH_SHORT).show();
                                        SharedUtils.putLoginUserId(LoginActivity.this,user.getUserid());
                                        SharedUtils.putSignature(LoginActivity.this,signature);
                                        SharedUtils.putLoginUserName(LoginActivity.this,user.getUsername());
                                        SharedUtils.putHeadimage(LoginActivity.this,user.getHeadimage());
                                        ExitApplication.getInstance().setUserid(user.getUserid());
                                        ExitApplication.getInstance().setUsername(user.getUsername());
                                        ExitApplication.getInstance().setSignature(signature);
                                        ExitApplication.getInstance().setHeadimage(user.getHeadimage());

                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        LoginActivity.this.finish();
                                        //ExitApplication.getInstance().finishAll();
                                    }else {
                                        tv_login.setText("登录");
                                        //Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                        tv_tooltip.setText("用户名或密码错误");
                                    }

                                }else{
                                    tv_login.setText("登录");
                                    //Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                    tv_tooltip.setText("用户名或密码错误");
                                }

                            }catch (Exception e){
                                tv_login.setText("登录");
                                //Toast.makeText(LoginActivity.this, "登录失败，系统错误！", Toast.LENGTH_SHORT).show();
                                tv_tooltip.setText("登录失败，系统错误！");
                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            tv_login.setText("登录");
                            Toast.makeText(LoginActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this, "身份证或密码不能为空", Toast.LENGTH_SHORT).show();
            tv_tooltip.setText("身份证或密码不能为空");
        }
    }

    /**
     * Called when a view has been clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 注册
             */
            case R.id.tv_userRegi:
                startActivity(new Intent(getBaseContext(),RegisterActivity.class));
                break;
            /**
             * 找回密码
             */
            case R.id.tv_findPwd:
                startActivity(new Intent(getBaseContext(),FindPassWordActivity.class));
                break;
            case R.id.title1_back:
                this.finish();
                break;
        }
    }
}
