package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.lzjs.uappoint.bean.Address;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;

import java.util.ArrayList;
import java.util.List;


/***
 * 用户地址列表
 * wangdq
 * 2016年3月5日17:13:24
 */
public class UserAddresListActivity extends Activity {

    private static final String TAG = UserAddresListActivity.class.getSimpleName();


    private ListView addres_list;
    @ViewInject(R.id.btn_add)
    private Button btn_add;
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;
    private List<Address> addressList;
    private ImageView title1_back;

    private BaseAdapter mAdapter;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_addres_list);
        ViewUtils.inject(this);
        center_title_text.setText("收货地址");
        initView();
        initData();
    }


    public void initView() {
        addres_list = (ListView) findViewById(R.id.addres_list);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        Log.e(TAG, "flag:" + flag);
//        if ("1".equals(flag)){
//            addres_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Log.e(TAG,"flag11:"+flag);
//                    Intent intent=new Intent();
//                    intent.putExtra("address",addressList.get(position).getDetailedAddress());
//                    UserAddresListActivity.this.setResult(200, intent);
//                    UserAddresListActivity.this.finish();
//                }
//            });}
        title1_back = (ImageView) findViewById(R.id.title1_back);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAddresListActivity.this, UserAddresActivity.class);
                startActivity(intent);
            }
        });
        title1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAddresListActivity.this.finish();
            }
        });
//        addres_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(UserAddresListActivity.this, "条目点击了", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void initData() {
        RequestParams params = new RequestParams();
        String regiUserId = SharedUtils.getLoginUserId(this);
        params.addQueryStringParameter("regiUserId", regiUserId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_ADDRESS_INFO_LIST,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e(TAG, responseInfo.result);

                        addressList = new ArrayList<>();
                        Address address = null;
                        JSONObject object = JSON.parseObject(responseInfo.result);

                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONArray jsonArray = response.getJSONArray("datas");
                            if (jsonArray.size() > 0) {
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    address = JSON.parseObject(jsonArray.get(i).toString(), Address.class);
                                    addressList.add(address);
                                }

                                Log.e("11111", addressList.toString());
                                mAdapter = new AddressAdapter(UserAddresListActivity.this, addressList);
                                addres_list.setAdapter(mAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(UserAddresListActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private class AddressAdapter extends BaseAdapter {
        private Context context;
        List<Address> list;
        private int pos;


        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */

        public AddressAdapter(Context context, List<Address> list) {
            this.context = context;
            this.list = list;
        }

        public final class ListItemView {                //自定义控件集合
            public TextView addre_name;//收货人
            public TextView addre_tel;//联系电话
            public TextView addre_detail;//收货地址
            public RadioButton addre_isdefault;//是否为默认
            public TextView addre_del;//删除
            public TextView addre_editor;//编辑
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Address getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ListItemView listItemView = null;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //自定义视图
            pos = position;
            if (convertView == null) {
                listItemView = new ListItemView();
                convertView = LayoutInflater.from(context).inflate(R.layout.addre_item, null);
                listItemView.addre_name = (TextView) convertView.findViewById(R.id.addre_name);
                listItemView.addre_tel = (TextView) convertView.findViewById(R.id.addre_tel);
                listItemView.addre_detail = (TextView) convertView.findViewById(R.id.addre_detail);
                listItemView.addre_isdefault = (RadioButton) convertView.findViewById(R.id.addre_isdefault);
                listItemView.addre_del = (TextView) convertView.findViewById(R.id.addre_del);
                //listItemView.addre_editor = (TextView) convertView.findViewById(R.id.addre_editor);
                convertView.setTag(listItemView);
            } else
                listItemView = (ListItemView) convertView.getTag();
            listItemView.addre_name.setText(list.get(position).getRealname());
            listItemView.addre_tel.setText(list.get(position).getPhoneNumber());
            listItemView.addre_detail.setText(list.get(position).getDetailedAddress());
            listItemView.addre_isdefault.setClickable(false);
            if (list.get(position).getIsDefault().equals("001")) {
                listItemView.addre_isdefault.setChecked(true);
            } else {
                listItemView.addre_isdefault.setChecked(false);

            }
            //编辑按钮
            listItemView.addre_editor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserAddresListActivity.this, EditorAddressActivity.class);
                    intent.putExtra("flag", "editor");
                    intent.putExtra("realname", list.get(position).getRealname());
                    intent.putExtra("phoneNumber", list.get(position).getPhoneNumber());
                    intent.putExtra("postcode", list.get(position).getPostcode());
                    intent.putExtra("addressProName", list.get(position).getAddressProName());
                    intent.putExtra("addressCityName", list.get(position).getAddressCityName());
                    intent.putExtra("addressCountyName", list.get(position).getAddressCountyName());
                    intent.putExtra("detailedAddress", list.get(position).getDetailedAddress());
                    intent.putExtra("isDefault", list.get(position).getIsDefault());
                    intent.putExtra("addressId", list.get(position).getAddressId());
                    startActivity(intent);
                }
            });


            final String addre = list.get(position).getAddressId();

            //删除按钮
            listItemView.addre_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String regiUserId = SharedUtils.getLoginUserId(getBaseContext());
                    if (regiUserId.length() > 0) {
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("regiUserId", regiUserId);
                        params.addBodyParameter("addressId", addre);
                        HttpUtils http = new HttpUtils();
                        http.send(HttpRequest.HttpMethod.POST,
                                Contants.DEL_USER_ADDRESS_INFO,
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
                                            if ("true".equals(jsonData.getString("success"))) {
                                                list.remove(position);
                                                notifyDataSetChanged();
                                                Toast.makeText(UserAddresListActivity.this, jsonData.getString("desc"), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(UserAddresListActivity.this, jsonData.getString("desc"), Toast.LENGTH_SHORT).show();
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
            });
            if ("1".equals(flag)) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("result", addressList.get(position).getDetailedAddress());
                        intent.putExtra("addressId", addressList.get(position).getAddressId());
                        Log.e(TAG, "result" + addressList.get(position).getDetailedAddress());
                        UserAddresListActivity.this.setResult(200, intent);
                        UserAddresListActivity.this.finish();
                    }
                });
            }
            return convertView;
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    /**
     * 头部返回
     *
     * @param view
     */
    @OnClick(R.id.title1_back)
    public void backBtnClick(View view) {
        finish();
    }
}
