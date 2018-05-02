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
import android.widget.ImageView;
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

public class EditorAddressActivity extends Activity {
    public static final String TAG = "EditorAddressActivity";
/*
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;
    @ViewInject(R.id.addre_name_ed)
    private EditText addre_name;
    @ViewInject(R.id.addre_phone_ed)
    private EditText addre_phone;
    @ViewInject(R.id.addre_post_ed)
    private EditText addre_post;
    @ViewInject(R.id.addre_pro_ed)
    private Spinner addre_pro;

    @ViewInject(R.id.addre_city_ed)
    private Spinner addre_city;

    @ViewInject(R.id.addre_county_ed)
    private Spinner addre_county;
    @ViewInject(R.id.addre_detail_ed)
    private EditText addre_detail;
    @ViewInject(R.id.add_isdefault_ed)
    private ToggleButton add_isdefault;
    @ViewInject(R.id.body_submit_ed)
    private Button body_submit;
    @ViewInject(R.id.title1_back)
    private ImageView title_back;

    private String addressPro;
    private String addressCity;
    private String addressCounty;
    private String detailedAddress;
    private String postcode;
    private String realname;
    private String mobileNumber;
    private String isDefault;
    private String addressId;
    private static List<String> prom = new ArrayList<>();
    private static List<String> proids = new ArrayList<>();
    private static List<String> citym = new ArrayList<>();
    private static List<String> cityids = new ArrayList<>();
    private static List<String> countm = new ArrayList<>();
    private static List<String> countids = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String pro;
    private String city;
    private String county;


    private String flag;
    private String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_address);
        ViewUtils.inject(this);
        center_title_text.setText("编辑收货地址");
        Intent intent = getIntent();
        addressId = intent.getStringExtra("addressId");
        flag = intent.getStringExtra("flag");
        if ("editor".equals(flag)) {
            setDataToView(intent);
        }
    }


    //将数据设置到控件中
    private void setDataToView(Intent intent) {
        pro = intent.getStringExtra("addressProName");
        city = intent.getStringExtra("addressCityName");
        county = intent.getStringExtra("addressCountyName");
        String address=intent.getStringExtra("detailedAddress");
        String subAddress="";
        subAddress=address.replace(pro + city + county+"", "");
        addre_name.setText(intent.getStringExtra("realname"));
        addre_phone.setText(intent.getStringExtra("phoneNumber"));
        addre_post.setText(intent.getStringExtra("postcode"));
        addre_detail.setText(subAddress);
        setViewData("0", addre_pro, prom, proids, pro);
        addre_pro.setOnItemSelectedListener(new SslPro());
        addre_city.setOnItemSelectedListener(new SslCity());
        addre_county.setOnItemSelectedListener(new SslCounty());
        if ("001".equals(intent.getStringExtra("isDefault"))) {
            add_isdefault.setChecked(true);
        } else {
            add_isdefault.setChecked(false);
        }

        //添加监听事件
        body_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    saveData();
                }
            }
        });
        //头部返回
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditorAddressActivity.this.finish();
            }
        });

    }

    private void saveData() {
        RequestParams params = new RequestParams();
        String regiUserId = SharedUtils.getLoginUserId(this);
        params.addBodyParameter("regiUserId", regiUserId);
        params.addBodyParameter("addressPro", addressPro);
        params.addBodyParameter("addressCity", addressCity);
        params.addBodyParameter("addressCounty", addressCounty);
        params.addBodyParameter("detailedAddress", detailedAddress);
        params.addBodyParameter("postcode", postcode);
        params.addBodyParameter("realname", realname);
        params.addBodyParameter("phoneNumber", mobileNumber);
        params.addBodyParameter("isDefault", isDefault);
        params.addBodyParameter("addressId", addressId);
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
                            if (jsonData.getBoolean("success")) {
                                Toast.makeText(EditorAddressActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(EditorAddressActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
                                body_submit.setText("保存");
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(EditorAddressActivity.this, "保存失败!请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    //获取数据
    public void setViewData(String id, final Spinner spinner, final List<String> m, final List<String> ids, final String name) {

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
                            adapter = new ArrayAdapter<String>(EditorAddressActivity.this, android.R.layout.simple_spinner_item, m);

                            //设置下拉列表的风格
                            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                            //将adapter 添加到spinner中
                            spinner.setAdapter(adapter);

                            spinner.setVisibility(View.VISIBLE);
                            setSpinnerItemSelectedByValue(spinner, name);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(EditorAddressActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
/*
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {

        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象

        int k = apsAdapter.getCount();

        for (int i = 0; i < k; i++) {

            if (value.equals(apsAdapter.getItem(i).toString())) {

                spinner.setSelection(i, true);// 默认选中项

                break;

            }

        }

    }

    //使用数组形式操作
    public class SslPro implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //view.setText("你的血型是："+m[arg2]);
            addressPro = proids.get(arg2);
            citym.clear();
            cityids.clear();
            setViewData(addressPro, addre_city, citym, cityids, city);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //使用数组形式操作
    public class SslCity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            addressCity = cityids.get(arg2);
            countids.clear();
            countm.clear();
            setViewData(addressCity, addre_county, countm, countids, county);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //使用数组形式操作
    public class SslCounty implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            addressCounty = countids.get(arg2);


        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }

    }


    private boolean validateData() {

        detailedAddress = addre_detail.getText().toString();
        postcode = addre_post.getText().toString();
        realname = addre_name.getText().toString();
        mobileNumber = addre_phone.getText().toString();

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(mobileNumber);
        if (add_isdefault.isChecked()) {
            isDefault = "001";
        } else
            isDefault = "000";
        if (StringUtils.isEmpty(realname)) {
            Toast.makeText(EditorAddressActivity.this, "请输入收件人!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(mobileNumber)) {
            Toast.makeText(EditorAddressActivity.this, "请输入联系电话!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!matcher.matches()) {
            Toast.makeText(EditorAddressActivity.this, "请输入正确的电话号码!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(postcode)) {
            Toast.makeText(EditorAddressActivity.this, "请输入邮编!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(addressPro)) {
            Toast.makeText(EditorAddressActivity.this, "请选择省份!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(addressCity)) {
            Toast.makeText(EditorAddressActivity.this, "请选择地市!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(addressCounty)) {
            Toast.makeText(EditorAddressActivity.this, "请选择县区!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(detailedAddress)) {
            Toast.makeText(EditorAddressActivity.this, "请输入详细地址!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
*/
}