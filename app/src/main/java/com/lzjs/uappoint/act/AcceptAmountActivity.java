package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.AcceptAmountAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.DoctorAcceptAmount;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;

import static java.security.AccessController.getContext;

/**
 * 肿瘤医院 查看 当天，最近一周，最近一月，最近半年 院内院外医生接诊量
 */
public class AcceptAmountActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener{
    private Spinner spinner;
    private TextView hosInner,hosOuter;
    private ListView listView;
    private ArrayAdapter dateAdapter;
    private int flagTimeChoose = 0;
    //接收接诊量信息
    private List<DoctorAcceptAmount> doctorAcceptAmountList = new ArrayList<>();
    private static final String TAG = "AcceptAmountActivity";
    private AcceptAmountAdapter acceptAmountAdapter;
    private Toolbar toolbar;
    private TextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start****************************************************");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_accept_amount);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        Resources resource = getBaseContext().getResources();
        Log.d(TAG, "onCreate: *******************"+resource);
        toolbar =(Toolbar)findViewById(R.id.accept_amount_toolbar) ;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView = (TextView)findViewById(R.id.toolbar_name) ;
        textView.setText("肿瘤医院");
        textView.setTextSize(18);

        //接诊量数据展示列表
        initAccAmount();
        //院内院外选择
        hosInner = (TextView)findViewById(R.id.hospital_inner);
        hosOuter = (TextView)findViewById(R.id.hospital_outer);
        //时间选择下拉列表
        spinner = (Spinner)findViewById(R.id.spinner);
        dateAdapter = ArrayAdapter.createFromResource(this,R.array.timeChoose,android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dateAdapter);

        //院外医生接诊量查看
        hosOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorStateList textColor = getResources().getColorStateList(R.color.drawer_item_bg);
                ColorStateList textColorAfterClick = getResources().getColorStateList(R.color.inner_outer_text);
                if (textColor != null && textColorAfterClick != null) {
                    hosInner.setTextColor(textColor);
                    hosInner.setBackgroundResource(R.color.inner_outer_text);
                    hosOuter.setTextColor(textColorAfterClick);
                    hosOuter.setBackgroundResource(R.color.app_pri_color);
                }
               // flagHos = 1;
                if (flagTimeChoose==0||flagTimeChoose==1){

                    initAccAmount();
                   // Toast.makeText(AcceptAmountActivity.this, "当天", Toast.LENGTH_SHORT).show();
                }else if (flagTimeChoose==2){
                    weekOuterAccAmount();
                   // Toast.makeText(AcceptAmountActivity.this,"最近一周",Toast.LENGTH_SHORT).show();
                }else if (flagTimeChoose==3){
                    monthOuterAccAmount();
                   // Toast.makeText(AcceptAmountActivity.this, "最近一月", Toast.LENGTH_SHORT).show();
                }else{
                    yearOuterAccAmount();
                    //Toast.makeText(AcceptAmountActivity.this,"最近半年",Toast.LENGTH_SHORT).show();
                }

            }
        });
        hosInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorStateList textColor = getResources().getColorStateList(R.color.drawer_item_bg);
                ColorStateList textColorAfterClick = getResources().getColorStateList(R.color.inner_outer_text);
                if(textColor!=null && textColorAfterClick!=null){
                    hosOuter.setTextColor(textColor);
                    hosOuter.setBackgroundResource(R.color.inner_outer_text);
                    hosInner.setTextColor(textColorAfterClick);
                    hosInner.setBackgroundResource(R.color.app_pri_color);
                }
               // flagHos = 2;
                if (flagTimeChoose==0||flagTimeChoose==1){
                    initAccAmountInner();
                   // Toast.makeText(AcceptAmountActivity.this, "当天s", Toast.LENGTH_SHORT).show();
                }else if (flagTimeChoose==2){
                    weekInnerAccAmount();
                   // Toast.makeText(AcceptAmountActivity.this,"最近一周s",Toast.LENGTH_SHORT).show();
                }else if (flagTimeChoose==3){
                    monthInnerAccAmount();
                   // Toast.makeText(AcceptAmountActivity.this, "最近一月s", Toast.LENGTH_SHORT).show();
                }else{
                    yearInnerAccAmount();
                   // Toast.makeText(AcceptAmountActivity.this,"最近半年s",Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("pos*********", position + "*********************");
                try {
                    //以下三行代码是解决问题所在
                    Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);  //设置mOldSelectedPosition可访问
                    field.setInt(spinner, AdapterView.INVALID_POSITION); //设置mOldSelectedPosition的值
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (position){
                    case 0:
                        flagTimeChoose = 1;
                        initAccAmount();
                        break;
                    case 1:
                        flagTimeChoose = 2;
                        break;
                    case 2:
                        flagTimeChoose = 3;
                        break;
                    case 3:
                        flagTimeChoose = 4;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

    }

    /**
     * 初始化接诊量相关信息
     *
     * 可能接受map类型
     */
    private void initAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",167);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",106);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",202);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",110);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",212);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",168);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",116);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",242);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",115);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",292);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);

        setAmountListAdapter(doctorAcceptAmountList);

    }
    private void initAccAmountInner(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",123);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",111);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",212);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",210);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",212);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",118);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",113);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",142);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",115);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",292);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);

        setAmountListAdapter(doctorAcceptAmountList);

    }

    private void nowOuterAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",29);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",33);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",30);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",11);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",21);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",16);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",11);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",24);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",15);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",29);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);

        setAmountListAdapter(doctorAcceptAmountList);

    }
    private void weekOuterAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",130);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",277);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",300);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",299);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",212);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",216);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",211);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",224);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",125);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",109);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);
        setAmountListAdapter(doctorAcceptAmountList);
    }
    private void weekInnerAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",300);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",277);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",300);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",297);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",211);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",216);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",211);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",229);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",325);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",209);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);
        setAmountListAdapter(doctorAcceptAmountList);
    }
    private void monthOuterAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",400);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",477);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",380);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",599);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",612);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",416);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",511);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",433);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",425);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",509);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);
        setAmountListAdapter(doctorAcceptAmountList);
    }
    private void monthInnerAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",602);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",677);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",600);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",699);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",712);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",716);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",511);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",824);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",525);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",809);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);
        setAmountListAdapter(doctorAcceptAmountList);
    }
    private void yearOuterAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",1001);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",902);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",800);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",899);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",912);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",1016);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",1211);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",1224);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",1125);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",1109);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);
        setAmountListAdapter(doctorAcceptAmountList);
    }
    private void yearInnerAccAmount(){
        doctorAcceptAmountList.clear();
        DoctorAcceptAmount doctorAcceptAmount1 = new DoctorAcceptAmount("裴正学",1501);
        DoctorAcceptAmount doctorAcceptAmount2 = new DoctorAcceptAmount("李玉田",1202);
        DoctorAcceptAmount doctorAcceptAmount3 = new DoctorAcceptAmount("李建成",1000);
        DoctorAcceptAmount doctorAcceptAmount4 = new DoctorAcceptAmount("陈学忠",1999);
        DoctorAcceptAmount doctorAcceptAmount5 = new DoctorAcceptAmount("李英平",1912);
        DoctorAcceptAmount doctorAcceptAmount6 = new DoctorAcceptAmount("牛荣",1016);
        DoctorAcceptAmount doctorAcceptAmount7 = new DoctorAcceptAmount("谢清军",1211);
        DoctorAcceptAmount doctorAcceptAmount8 = new DoctorAcceptAmount("李斌",1224);
        DoctorAcceptAmount doctorAcceptAmount9 = new DoctorAcceptAmount("杨碎胜",1125);
        DoctorAcceptAmount doctorAcceptAmount10 = new DoctorAcceptAmount("李英平",1109);


        doctorAcceptAmountList.add(doctorAcceptAmount1);
        doctorAcceptAmountList.add(doctorAcceptAmount2);
        doctorAcceptAmountList.add(doctorAcceptAmount3);
        doctorAcceptAmountList.add(doctorAcceptAmount4);
        doctorAcceptAmountList.add(doctorAcceptAmount5);
        doctorAcceptAmountList.add(doctorAcceptAmount6);
        doctorAcceptAmountList.add(doctorAcceptAmount7);
        doctorAcceptAmountList.add(doctorAcceptAmount8);
        doctorAcceptAmountList.add(doctorAcceptAmount9);
        doctorAcceptAmountList.add(doctorAcceptAmount10);
        setAmountListAdapter(doctorAcceptAmountList);
    }

    public void setAmountListAdapter(List<DoctorAcceptAmount> list){
        acceptAmountAdapter = new AcceptAmountAdapter(AcceptAmountActivity.this,R.layout.item_accept_amount_info,list);
        listView = (ListView)findViewById(R.id.accept_amount_list);
        listView.setAdapter(acceptAmountAdapter);
    }
}