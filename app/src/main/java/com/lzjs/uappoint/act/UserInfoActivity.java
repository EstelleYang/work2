package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.StringUtils;


/**
 * 用户信息
 * wangdq
 * 2016/01/28
 */
public class UserInfoActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = UserInfoActivity.class.getSimpleName();
    /**
     * 年龄段
     */
    private Spinner spinner;
    /**
     * 昵称
     */
    private EditText user_name;
    /**
     * 职业
     */
    private EditText user_job;
    /**
     * 性别
     */
    private RadioGroup radioGroup;
    /**
     * 保存按钮
     */
    private ImageView image_save;

    private RadioButton sex_m;
    private RadioButton sex_w;


    //返回按钮
    private ImageView backImageView;


    /**
     * 分别对应以上控件拿到的数据
     */
    private String regiNice;
    private String regiJob;
    private String regiAge;
    private String regiSex;
    private RadioButton radioButton;
    private String regiId;
    private static final String[] m = {"00后", "90后", "80后", "70后", "60后", "其他"};
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        //修改标题名称
        TextView textView = (TextView)findViewById(R.id.center_title_text);
        textView.setText(R.string.title_activity_my_info);

        initView();
        initCtrl();

    }

    /**
     * 初始化数据
     */
    private void initCtrl() {

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        String regiId=SharedUtils.getLoginUserId(getBaseContext());
        if (regiId.length()>0){
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("regiId", regiId);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.QUERY_USER_INFO,
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
                                JSONObject response = object.getJSONObject("Response");
                                JSONObject jsonData = response.getJSONObject("data");
                                String sex=jsonData.getString("regiSex");
                                String name=jsonData.getString("regiNice");
                                String job=jsonData.getString("regiJob");
                                String  age=jsonData.getString("regiAge");

                                /**根据请求到的数据来设置几个控件的状态*/

                                /**设置姓名*/
                                if (!StringUtils.isEmpty(name)){
                                    user_name.setText(name);
                                }

                                /**设置性别*/
                                if ("00M".equals(sex)){
                                    sex_m.setChecked(true);
                                }else if ("00W".equals(sex)){
                                    sex_w.setChecked(true);
                                }else {
                                    sex_m.setChecked(true);
                                }

                                /**设置职业*/
                                if (!StringUtils.isEmpty(job)){
                                    user_job.setText(job);
                                }

                                /**设置年龄*/

                                Log.e(TAG,"------------>"+age);
                                if (!StringUtils.isEmpty(age)&&"000".equals(age)){
                                    spinner.setSelection(0);

                                }else if (!StringUtils.isEmpty(age)&&"090".equals(age)){
                                    spinner.setSelection(1);

                                }else if (!StringUtils.isEmpty(age)&&"080".equals(age)){
                                    spinner.setSelection(2);

                                }else if (!StringUtils.isEmpty(age)&&"070".equals(age)){
                                    spinner.setSelection(3);

                                }else if (!StringUtils.isEmpty(age)&&"060".equals(age)){
                                    spinner.setSelection(4);
                                }else if (!StringUtils.isEmpty(age)&&"050".equals(age)){
                                    spinner.setSelection(5);

                                }



                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(getBaseContext(), "系统异常，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    });



        }


    }

    /**
     * 初始化控件
     */
    private void initView() {
        spinner = (Spinner) findViewById(R.id.user_age);
        user_name = (EditText) findViewById(R.id.user_name);
        user_job = (EditText) findViewById(R.id.user_job);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        image_save = (ImageView) findViewById(R.id.image_save);
        sex_m = (RadioButton) findViewById(R.id.sex_m);
        sex_w = (RadioButton) findViewById(R.id.sex_w);

        image_save.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        backImageView = (ImageView) findViewById(R.id.title1_back);
        //返回事件
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UserInfoActivity.this.finish();
            }
        });




    }

    /**
     * Called when a view has been clicked.
     * 保存按钮的点击监听
     */
    @Override
    public void onClick(View v) {

        regiNice = user_name.getText().toString();
        regiJob = user_job.getText().toString();
        regiId = SharedUtils.getLoginUserId(this);

        if (!StringUtils.isEmpty(regiAge)&& !StringUtils.isEmpty(regiSex) && !StringUtils.isEmpty(regiJob) && !StringUtils.isEmpty(regiNice) && !StringUtils.isEmpty(regiNice)) {

            RequestParams params = new RequestParams();
            params.addBodyParameter("regiNice", regiNice);
            params.addBodyParameter("regiJob", regiJob);


            params.addBodyParameter("regiId", regiId);

            if ("00后".equals(regiAge)){
                params.addBodyParameter("regiAge", "000");
            }else if ("90后".equals(regiAge)){
                params.addBodyParameter("regiAge", "090");
            }else if ("80后".equals(regiAge)){
                params.addBodyParameter("regiAge", "080");
            }else if ("70后".equals(regiAge)){
                params.addBodyParameter("regiAge", "070");
            }else if ("60后".equals(regiAge)){
                params.addBodyParameter("regiAge", "060");
            }else if("其他".equals(regiAge)){
                params.addBodyParameter("regiAge", "050");
            }

            if ("男".equals(regiSex)){
                params.addBodyParameter("regiSex", "00M");
            }else if("女".equals(regiSex)){
                params.addBodyParameter("regiSex", "00W");
            }
            Log.e(TAG, regiNice);
            Log.e(TAG, regiJob);
            Log.e(TAG, regiAge);
            Log.e(TAG, regiSex);
            Log.e(TAG, regiId);

            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, Contants.USER_INFO_EDITOR, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {

                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.e(TAG, "onSuccess");
                            Log.e(TAG, responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("Response");
                                JSONObject jsondata = response.getJSONObject("data");
                                String desc = jsondata.getString("desc");
                                Log.e(TAG, desc);

                                if (jsondata.getBoolean("success")) {
                                    Toast.makeText(UserInfoActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(UserInfoActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();

                                }

                                Intent intent =new Intent();
                                intent.putExtra("name",regiNice);
                                intent.putExtra("sex",regiSex);
                                UserInfoActivity.this.setResult(200,intent);
                                UserInfoActivity.this.finish();

                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(UserInfoActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();

                        }
                    }

            );


        } else {

            Toast.makeText(UserInfoActivity.this, "个人资料未全部完善，请检查！", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 性别的RadioGroup的点击监听
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioButton = (RadioButton) findViewById(checkedId);
        regiSex = (String) radioButton.getText();
    }


    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //view.setText("你的血型是："+m[arg2]);
            regiAge = spinner.getSelectedItem().toString();

        }

        public void onNothingSelected(AdapterView<?> arg0) {

        }
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
