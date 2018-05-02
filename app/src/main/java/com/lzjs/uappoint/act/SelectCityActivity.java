package com.lzjs.uappoint.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocationClient;
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
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.City;
import com.lzjs.uappoint.myview.SliderBar;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.TimeHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends BaseActivity implements SliderBar.OnTouchingLetterChangedListener{

    @ViewInject(R.id.city_list)
    private ListView listDatas;
    private List<City> cityList = new ArrayList<>();
    @ViewInject(R.id.city_side_bar)
    private SliderBar sliderBar;
    @ViewInject(R.id.display)
    private  TextView tvDialog;
    private boolean isLocation;
    private String cityName;//当前城市名称
    private String choiceCityName;
    private String locationCityName;
    private AMapLocationClient mLocationClient;
    private TextView locationCityTV;
    private String exitTime;
    private City city;
    private Toolbar mToolbar;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        tv_title=(TextView)findViewById(R.id.toolbar_name);
        mToolbar.setTitle("");
        tv_title.setText("选择城市");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewUtils.inject(this);
        //初始化一个布局文件
        View view = LayoutInflater.from(this).inflate(R.layout.home_city_search, null);
        locationCityTV = (TextView) view.findViewById(R.id.index_city_item_searchcity);
        isLocation = SharedUtils.getWelcomeBoolean(this);
        loadCityDatas();
        //定位开始
        //选择城市
        choiceCityName = SharedUtils.getCityName(this);
        //定位城市
        locationCityName = SharedUtils.getLocationCityName(this);
//        if (isLocation) {
//            if (!choiceCityName.isEmpty()) {
//                locationCityTV.setText(locationCityName);
//            }
//
//        } else {
//            Log.e("choiceCityName","choiceCityName:"+city);
//            if (null != city){
//                locationCityTV.setText(cityName);
//            }else {
//                locationCityTV.setText(choiceCityName);
//            }
//
//
//        }
        locationCityTV.setText("正在定位中...");
        mLocationClient = ((ExitApplication) getApplication()).locationClient;
        ((ExitApplication) getApplication()).mLocationResult = locationCityTV;
        ((ExitApplication) getApplication()).cityName = choiceCityName;
        //initLocation();
        //mLocationClient.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                locationCityTV.setText(SharedUtils.getLocationCityName(SelectCityActivity.this)+"");
                judgeLocationCity();
            }
        }, 5000);
        exitTime = TimeHelper.addMinute(TimeHelper.getCurrentTime(), -10);
        //定位结束
        sliderBar.setmTextDialog(tvDialog);

        //textView.setCompoundDrawables(null, null, drawable, null);
        listDatas.addHeaderView(view);

        sliderBar.setOnTouchingLetterChangedListener(this);


    }
/*    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }*/
    public void judgeLocationCity() {
        try {
            choiceCityName = SharedUtils.getCityName(this);
            locationCityName = SharedUtils.getLocationCityName(this);
            if (!choiceCityName.isEmpty() && !choiceCityName.equals(locationCityName) && !locationCityName.isEmpty()) {
                new AlertDialog.Builder(this).setTitle("系统定位到您在" + locationCityName + "，需要切换到" + locationCityName + "吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                locationCityTV.setText(locationCityName);
                                SharedUtils.putCityName(SelectCityActivity.this, locationCityName);
                                Intent intent = new Intent(SelectCityActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!choiceCityName.isEmpty()) {
                                    locationCityTV.setText(choiceCityName);
                                }
                            }
                        }).show();
            } else if ("".equals(choiceCityName)) {
                SharedUtils.putCityName(this, locationCityName);
                locationCityTV.setText(locationCityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        //找到listView中显示索引位置
        listDatas.setSelection(findIndex(cityList, s));

    }
    private int findIndex(List<City> cityList, String s) {
        if(cityList != null){
            for(int i = 0 ;i<cityList.size();i++){
                City city = cityList.get(i);
                //根根据city中的AreaAlpha 进行比较
                if(s.equals(city.getAreaAlpha())){
                    return i;
                }
            }
        }else{
            Toast.makeText(this, "暂无信息", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }

    private StringBuffer buffer = new StringBuffer();//用来第一次保存首字母的索引
    private List<String> firstList = new ArrayList<String>();//用来保存索引值对象的城市名称

    //适配器
    public class MyAdapter extends BaseAdapter {
        private List<City> listCityDatas;

        public MyAdapter(List<City> listCityDatas){
            this.listCityDatas = listCityDatas ;
        }

        public  void  updateListView(List<City> listCityDatas){
            this.listCityDatas = listCityDatas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return listCityDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return listCityDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if( convertView == null ){
                holder = new Holder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_city_list_item,null);
                ViewUtils.inject(holder, convertView);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            //数据显示处理
            City city = listCityDatas.get(position);
            String sort = city.getAreaAlpha();
            Log.e("SelectCityActivity","selectcityactivity:-----"+sort);
            String name = city.getAreaName();
            //如果索引不存在
            if(buffer.indexOf(sort) == -1){
                buffer.append(sort);
                firstList.add(name);
            }
            if(firstList.contains(name)){
                holder.keySort.setText(sort);
                holder.keySort.setVisibility(View.VISIBLE);//包含对应的城市就让索引显示
            }else{
                holder.keySort.setVisibility(View.GONE);
            }
            holder.cityName.setText(name);
            holder.cityName.setPadding(holder.keySort.getPaddingLeft(),40,0,40);

            return convertView;
        }
    }
    public class Holder{
        @ViewInject(R.id.city_list_item_sort)
        public TextView keySort;
        @ViewInject(R.id.city_list_item_name)
        public TextView cityName;
    }
    /***
     * 调用web端接口,查询城市信息
     */
    private void loadCityDatas() {


        /*city = new City();
        city.setAreaId("1");
        city.setAreaAlpha("L");
        city.setAreaName("兰州");

        City c1=new City();
        c1.setAreaId("2");
        c1.setAreaAlpha("B");
        c1.setAreaName("白银");
        cityList.add(city);
        cityList.add(c1);
        MyAdapter myAdapter = new MyAdapter(cityList);
        listDatas.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();*/

        RequestParams params = new RequestParams();
        params.addBodyParameter("areaType", "00B");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_AREA_LIST,
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
                        Log.e("CITY", "CITY:" + responseInfo.result.toString());
                        JSONObject obj = JSON.parseObject(responseInfo.result);
                        Log.e("CITY", "CITYggggggggggggg:" + obj);
                        if(obj.getString("result").equals("200")){
                            JSONObject objdata = obj.getJSONObject("response");
                            JSONArray objArray = objdata.getJSONArray("datas");
                            int length = objArray.size();
                            if(objArray != null && length > 0){
                                for (int i = 0;i<length;i++){
                                    city = new City();
                                    city = JSON.parseObject(objArray.getJSONObject(i).toString(),City.class);
                                    city.setAreaId(objArray.getJSONObject(i).getString("datacode"));
                                    city.setAreaAlpha(objArray.getJSONObject(i).getString("datacode2"));
                                    city.setAreaName(objArray.getJSONObject(i).getString("dataname"));
                                    cityList.add(city);
                                }
                            }
                        }
                        MyAdapter myAdapter = new MyAdapter(cityList);
                        listDatas.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                        Log.e("城市数据","城市数据"+cityList);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getApplicationContext(),"网络错误，请检查网络！",Toast.LENGTH_SHORT).show();
                    }
                });



        //条目点击事件
        listDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //实现条目点击的功能
                cityName = SharedUtils.getCityName(getBaseContext());
                Intent intent = new Intent();
                if (position==0){
                    MainActivity.MainActivity_instence.finish();
                    intent.setClass(SelectCityActivity.this, MainActivity.class);
                    intent.putExtra("city", cityName);
                    SharedUtils.putCityName(SelectCityActivity.this,cityName);
                }else {
                    MainActivity.MainActivity_instence.finish();
                    intent.setClass(SelectCityActivity.this, MainActivity.class);
                    intent.putExtra("city", cityList.get(position - 1).getAreaName());
                    SharedUtils.putCityName(SelectCityActivity.this,cityList.get(position - 1).getAreaName());
                }
                startActivity(intent);
                finish();

            }
        });
    }

    /*@OnClick(R.id.index_city_back)
    public void backBtnClick(View view){
        finish();
    }*/

}
