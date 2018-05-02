package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.lzjs.uappoint.util.SharedUtils;

public class UserDiscussActivity extends Activity implements View.OnClickListener{
    public static final String TAG=UserDiscussActivity.class.getSimpleName();


    private EditText et_discuss_content;
    private TextView discuss_btn;
    private TextView center_title_text;
    private ImageView title1_back;


    private String regiUserId;
    private String typeId;
    private String merchantIds;
    private String equipId;
    private String menuIds;
    private String evaluateContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_discuss);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_discuss_content= (EditText) findViewById(R.id.et_discuss_content);
        discuss_btn= (TextView) findViewById(R.id.discuss_btn);
        center_title_text= (TextView) findViewById(R.id.center_title_text);
        title1_back= (ImageView) findViewById(R.id.title1_back);
        discuss_btn.setOnClickListener(this);

        /**
         * 头部返回
         */
        title1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        center_title_text.setText(R.string.discuss_content);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent=getIntent();
        regiUserId = SharedUtils.getLoginUserId(this);
        typeId = intent.getStringExtra("typeId");
        merchantIds = intent.getStringExtra("merchantIds");
        equipId = intent.getStringExtra("equipId");
        menuIds = intent.getStringExtra("menuIds");
    }

    /**
     * 提交按钮的点击
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.discuss_btn:
                evaluateContent = et_discuss_content.getText().toString();

                RequestParams params = new RequestParams();
                params.addBodyParameter("regiUserId", regiUserId);
                params.addBodyParameter("merchantIds", merchantIds);
                params.addBodyParameter("typeId", typeId);
                params.addBodyParameter("equipId", equipId);
                params.addBodyParameter("menuIds", menuIds);
                params.addBodyParameter("evaluateContent", evaluateContent);

                Log.e("regiUserId", regiUserId);
                Log.e("merchantIds",merchantIds);
                Log.e("typeId",typeId);
                Log.e("equipId",equipId);
                Log.e("menuIds",menuIds);
                Log.e("evaluateContent",evaluateContent);


                HttpUtils http = new HttpUtils();
                http.send(HttpRequest.HttpMethod.POST,
                        Contants.USER_ADD_DISCUSS,
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
                                Log.e(TAG, "onSuccess>>>>>>>>>>>>" + responseInfo.result);
                                JSONObject object = JSON.parseObject(responseInfo.result);
                                if (object.getString("result").equals("200")) {
                                    JSONObject response = object.getJSONObject("Response");
                                    JSONObject jsonData = response.getJSONObject("data");
                                    Toast.makeText(UserDiscussActivity.this,jsonData.getString("desc"),Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserDiscussActivity.this,UserAppraiseActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Toast.makeText(UserDiscussActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }

    }
}
