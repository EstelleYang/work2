package com.lzjs.uappoint.act;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lzjs.uappoint.R;

/**
 * 用户登录 on 2016/1/19.
 */
public class UserLoginActivity extends Activity{
    private TextView tv_findPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        TextView textView = (TextView)findViewById(R.id.center_title_text);
        textView.setText(R.string.title_activity_login_code);
        initView();
    }

    private void initView() {
       // tv_findPwd= (TextView) findViewById(R.id.tv_findPwd);
    }
}
