package com.lzjs.uappoint.act;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.NoScrollGridAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.Doctor;
import com.lzjs.uappoint.myview.CircleImageView;
import com.lzjs.uappoint.myview.CollapsibleTextView;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DisplayUtil;
import com.lzjs.uappoint.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ExpertsDetailActivity extends BaseActivity {
    private Spinner spinner_mypacs;
   // private Spinner spinner_recommentname;
    private ArrayAdapter<String> mAdapter_pacs ;
    //private ArrayAdapter<String> mAdapter_recommentname ;
    private String [] mStringArray_pacs;
    private String [] mStringArray_recommentname;
    private TextView tv_recommentname;
    private Toolbar mToolbar;
    private TextView tv_title;
    private CollapsibleTextView collapsibleTextView;
    private CircleImageView cvExpertImg;
    private TextView tvExpertName;
    private TextView tvExpertZc;
    private TextView tvHisName;
    private RatingBar ratingBar;
    private String expertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts_detail);
        expertId = getIntent().getStringExtra("expertId");

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        tv_title=(TextView)findViewById(R.id.toolbar_name);

        cvExpertImg = (CircleImageView) findViewById(R.id.civ_experts_img);
        tvExpertName = (TextView) findViewById(R.id.tv_experts_name);
        tvExpertZc = (TextView) findViewById(R.id.tv_experts_zc);
        tvHisName = (TextView) findViewById(R.id.tv_hisName);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        collapsibleTextView = (CollapsibleTextView) findViewById(R.id.collapsible_textview);
        mToolbar.setTitle("");
        tv_title.setText("专家详情");
        //collapsibleTextView.setText("\u3000\u3000周俊林，男，医学博士，主任医师，教授，博士研究生导师。发表论文100多篇，其中国家级核心期刊90多篇，SCI论文6篇，国际会议录用（RSNA）8篇。完成科研课题20项，科研获奖13项，其中以第1完成人获甘肃省科技进步一等奖、兰州市科技奖一等奖及甘肃省医学奖一等奖。");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSpinner();
        getData();
    }

    private  void getData(){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", expertId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.HOME_ZJ_DETAIL,
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
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("response");
                            JSONObject jsonData = response.getJSONObject("data");
                            Doctor doctor = new Doctor();
                            doctor.setUserid(jsonData.getString("userid"));
                            doctor.setHeadimage(jsonData.getString("headimage"));
                            doctor.setUsername(jsonData.getString("username"));
                            doctor.setTitlename(jsonData.getString("titlename"));
                            doctor.setHisname(jsonData.getString("hisname"));
                            doctor.setSpecial(jsonData.getString("special"));
                            doctor.setIntroduce(jsonData.getString("introduce"));
                            doctor.setStarlevel(jsonData.getString("starlevel"));

                            /**根据请求到的数据来设置几个控件的状态*/
                            ImageLoader.getInstance().displayImage(doctor.getHeadimage(), cvExpertImg, DisplayUtil.getOptions());
                            tvExpertName.setText(doctor.getUsername());
                            tvExpertZc.setText(doctor.getTitlename());
                            tvHisName.setText(doctor.getHisname());
                            ratingBar.setRating(Float.parseFloat(doctor.getStarlevel()));
                            collapsibleTextView.setText(doctor.getIntroduce());
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getBaseContext(), "系统异常，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initSpinner(){
        spinner_mypacs=(Spinner) findViewById(R.id.spinner_mypacs);
        //spinner_recommentname=(Spinner) findViewById(R.id.spinner_recommentname);
        mStringArray_pacs=getResources().getStringArray(R.array.languages);
        mStringArray_recommentname=getResources().getStringArray(R.array.doctor);
        //使用自定义的ArrayAdapter
        mAdapter_pacs = new SpinnerAdapter(ExpertsDetailActivity.this,mStringArray_pacs);
        //mAdapter_recommentname = new SpinnerAdapter(ExpertsDetailActivity.this,mStringArray_recommentname);
        tv_recommentname=(TextView)findViewById(R.id.tv_recommentname);

        spinner_mypacs.setPrompt("请选择您上传的影像资料");
        //spinner_mypacs.setba
        //设置下拉列表风格
        mAdapter_pacs.setDropDownViewResource(android.R.layout.select_dialog_multichoice);
        spinner_mypacs.setAdapter(mAdapter_pacs);
        //spinner_recommentname.setAdapter(mAdapter_recommentname);

        //监听Item选中事件
        spinner_mypacs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("选中了:"+mStringArray_pacs[position]);
                //Toast.makeText(ExpertsDetailActivity.this, mStringArray_pacs[position], Toast.LENGTH_SHORT).show();
                tv_recommentname.setText(mStringArray_recommentname[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*spinner_recommentname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("选中了:"+mStringArray_recommentname[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }

    private class SpinnerAdapter extends ArrayAdapter<String> {
        private Context mContext;
        private String [] mStringArray;
        public SpinnerAdapter(Context context, String[] stringArray) {
            super(context, android.R.layout.simple_spinner_item, stringArray);
            mContext = context;
            mStringArray=stringArray;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            //修改Spinner展开后的字体颜色
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(android.R.layout.select_dialog_multichoice, parent,false);
            }
            //此处text1是Spinner默认的用来显示文字的TextView
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(mStringArray[position]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);   //默认是SP
            //tv.setTextColor(getResources().getColor(R.color.gray));
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 修改Spinner选择后结果的字体颜色
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }

            //此处text1是Spinner默认的用来显示文字的TextView
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(mStringArray[position]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);   //默认是SP
            //tv.setTextColor(getResources().getColor(R.color.gray));
            return convertView;
        }
    }
}
