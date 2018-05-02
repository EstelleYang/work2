package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户收货地址编辑
 * wangdq
 * 2016-03-05
 */
public class UserAddresActivity extends Activity {

    private static final String TAG = "UserAddresActivity";
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;
    @ViewInject(R.id.addre_name)
    private EditText addre_name;
    @ViewInject(R.id.addre_phone)
    private EditText addre_phone;
    @ViewInject(R.id.addre_post)
    private EditText addre_post;
    @ViewInject(R.id.addre_pro)
    private Spinner addre_pro;

    @ViewInject(R.id.addre_city)
    private Spinner addre_city;

    @ViewInject(R.id.addre_county)
    private Spinner addre_county;
    @ViewInject(R.id.addre_detail)
    private EditText addre_detail;
    @ViewInject(R.id.add_isdefault)
    private ToggleButton add_isdefault;
    @ViewInject(R.id.body_submit)
    private Button body_submit;

    private String addressPro;
    private String addressCity;
    private String addressCounty;
    private String detailedAddress;
    private String postcode;
    private String realname;
    private String mobileNumber;
    private String isDefault;
    private String addressId;
    private static List<String> prom=new ArrayList<>();
    private static List<String> proids=new ArrayList<>();
    private static List<String> citym=new ArrayList<>();
    private static List<String> cityids=new ArrayList<>();
    private static List<String> countm=new ArrayList<>();
    private static List<String> countids=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String pro;

    private String flag;

    private String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_addres);
        ViewUtils.inject(this);


        initView();

    }




    private void initView(){
        center_title_text.setText("新增收货地址");
        getData("0", addre_pro, prom, proids);


        //添加事件Spinner事件监听
        addre_pro.setOnItemSelectedListener(new SslPro());
        addre_city.setOnItemSelectedListener(new SslCity());
        addre_county.setOnItemSelectedListener(new SslCounty());
        body_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()){
                    saveData();
                }
            }
        });
    }





    //使用数组形式操作
   public class SslPro implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //view.setText("你的血型是："+m[arg2]);
            addressPro=proids.get(arg2);
            citym.clear();
            cityids.clear();
            getData(addressPro, addre_city, citym, cityids);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    //使用数组形式操作
    public class SslCity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            addressCity=cityids.get(arg2);
            countids.clear();
            countm.clear();
            getData(addressCity, addre_county,countm,countids);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    //使用数组形式操作
    public class SslCounty implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            addressCounty=countids.get(arg2);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    private boolean validateData(){

        detailedAddress=addre_detail.getText().toString();
        postcode=addre_post.getText().toString();
        realname=addre_name.getText().toString();
        mobileNumber=addre_phone.getText().toString();

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(mobileNumber);
        if (add_isdefault.isChecked()){
            isDefault="001";
        }else
            isDefault="000";
        if(StringUtils.isEmpty(realname)){
            Toast.makeText(UserAddresActivity.this, "请输入收件人!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(mobileNumber)){
            Toast.makeText(UserAddresActivity.this, "请输入联系电话!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!matcher.matches()){
            Toast.makeText(UserAddresActivity.this, "请输入正确的电话号码!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(postcode)){
            Toast.makeText(UserAddresActivity.this, "请输入邮编!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(addressPro)){
            Toast.makeText(UserAddresActivity.this, "请选择省份!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(addressCity)){
            Toast.makeText(UserAddresActivity.this, "请选择地市!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(addressCounty)){
            Toast.makeText(UserAddresActivity.this, "请选择县区!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(detailedAddress)){
            Toast.makeText(UserAddresActivity.this, "请输入详细地址!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveData(){
        RequestParams params = new RequestParams();
        String regiUserId= SharedUtils.getLoginUserId(this);
        params.addBodyParameter("regiUserId", regiUserId);
        params.addBodyParameter("addressPro", addressPro);
        params.addBodyParameter("addressCity", addressCity);
        params.addBodyParameter("addressCounty", addressCounty);
        params.addBodyParameter("detailedAddress", detailedAddress);
        params.addBodyParameter("postcode", postcode);
        params.addBodyParameter("realname", realname);
        params.addBodyParameter("phoneNumber", mobileNumber);
        params.addBodyParameter("isDefault", isDefault);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.SAVE_ADDRESS_INFO,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        body_submit.setText("保存中...");
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsonData = response.getJSONObject("data");
                            if(jsonData.getBoolean("success")){
                                Toast.makeText(UserAddresActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(UserAddresActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
                                body_submit.setText("保存");
                            }

                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(UserAddresActivity.this, "保存失败!请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void getData(String id, final Spinner spinner,final List<String> m,final List<String> ids){

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pid", id);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_AREAS,
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
                            JSONArray jsonArray = response.getJSONArray("datas");

                            for (int i = 0; i < jsonArray.size(); i++) {
                                m.add(jsonArray.getJSONObject(i).getString("areaName"));
                                ids.add(jsonArray.getJSONObject(i).getString("areaId"));
                            }
                            //将可选内容与ArrayAdapter连接起来
                            adapter = new ArrayAdapter<String>(UserAddresActivity.this,android.R.layout.simple_spinner_item,m);

                            //设置下拉列表的风格
                            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                            //将adapter 添加到spinner中
                            spinner.setAdapter(adapter);

                            spinner.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(UserAddresActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });

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
