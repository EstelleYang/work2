package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.lzjs.uappoint.bean.AEvaluate;
import com.lzjs.uappoint.util.Contants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 商家评论列表
 * Created by wangdq on 2016/1/21.
 */
public class MerchantAppraiseFragment extends Fragment {
    public static String TAG="MerchantAppraiseFragment";
    public Context context; // 存储上下文对象
    public Activity activity; // 存储上下文对象
    private ListView listView;
    private AEvaluate aEvaluate;
    private List<AEvaluate> aEvaluateList;
    private AEvaluateAdapter aEvaluateAdapter;



    private static String merchantId;
    private static OkHttpClient client = new OkHttpClient();
    /**
     * 在这里直接设置连接超时，静态方法内，在构造方法被调用前就已经初始话了
     */
    /*static {
        client.newBuilder().connectTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(100, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(100, TimeUnit.SECONDS);
    }*/

    private Request request;

    public static MerchantAppraiseFragment newInstance(String merchantId){
        MerchantAppraiseFragment fragment = new MerchantAppraiseFragment();
        //fragment.title=title;
        MerchantAppraiseFragment.merchantId=merchantId;
        return  fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        activity = getActivity();
        View view=inflater.inflate(R.layout.activity_merchant_appraise_fragment,container,false);
        getData();
        Log.i(TAG, "onCreateView: aEvaluateList size"+aEvaluateList.size());
        listView = (ListView) view.findViewById(R.id.appraise_listView);

        return view;
    }
    /**
     * 获取数据
     */
    private void getData(){
        aEvaluateList = new ArrayList<>();

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("merchantId", merchantId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.GET_EVALUATE_INFOS,
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
                        JSONObject object= JSON.parseObject(responseInfo.result);
                        if(object.getString("result").equals("200")){
                            JSONObject response=object.getJSONObject("Response");
                            JSONArray jsonArray=response.getJSONArray("datas");
                            for(int i=0;i<jsonArray.size();i++){
                                aEvaluate =JSON.parseObject(jsonArray.get(i).toString(),AEvaluate.class);
                                aEvaluateList.add(aEvaluate);
                            }
                            aEvaluateAdapter = new AEvaluateAdapter(context,aEvaluateList);
                            listView.setAdapter(aEvaluateAdapter);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getActivity(), "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });

/*        String intr = "相当好的店家，服务到位，价格也合适，比别的店家都便宜，选的颜色也好，真不错！！！服务佳，神速飞毛腿，风雨无阻，包装也好，很方便，超级准时，品质棒！";
        for(int i=0;i<10;i++){
            aEvaluate = new AEvaluate();
            aEvaluate.setEvaluateId("000"+i);
            aEvaluate.setRegiUserId("0000" + i);
            aEvaluate.setRegiUserName("张三");
            aEvaluate.setMerchantId("00000" + i);
            aEvaluate.setMerchantName("商家");
            aEvaluate.setMenuId("0000000" + i);
            aEvaluate.setBsMenuName("装备");
            aEvaluate.setEvaluateContent(intr);
            aEvaluate.setEvaluatePicId("drawable://" + R.drawable.tp);
            aEvaluate.setCreateTime("2016-02-01");
            aEvaluateList.add(aEvaluate);
        }*/
    }

    private class AEvaluateAdapter extends BaseAdapter {
        private Context context;
        List<AEvaluate> aEvaluateList;

        public final class ListItemView {                //自定义控件集合
            public ImageView image;
            public TextView merchantName;//商家名称
            public TextView appraiseTime;//发布时间
            public TextView appraiseContent;//评价内容
            public TextView replyContent;
            public TextView replyTime;
            public TextView my_appraise_content;
            public TextView my_appraise_time;
        }
        public AEvaluateAdapter ( Context context,List<AEvaluate> list){
            this.context = context;
            this.aEvaluateList = list;
        }
        @Override
        public int getCount() {
            return aEvaluateList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //自定义视图
            ListItemView listItemView = null;
            if(convertView == null){
                listItemView = new ListItemView();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_my_appraise_list_item,null);
                listItemView.image = (ImageView) convertView.findViewById(R.id.appraise_view_pic);
                listItemView.merchantName = (TextView) convertView.findViewById(R.id.merchantName);
                listItemView.appraiseTime = (TextView) convertView.findViewById(R.id.appraiseTime);
                listItemView.appraiseContent = (TextView) convertView.findViewById(R.id.appraiseContent);
                listItemView.replyContent = (TextView) convertView.findViewById(R.id.replyContent);
                listItemView.replyTime = (TextView) convertView.findViewById(R.id.replyTime);
                listItemView.my_appraise_content = (TextView) convertView.findViewById(R.id.my_appraise_content);
                listItemView.my_appraise_time = (TextView) convertView.findViewById(R.id.my_appraise_time);

                convertView.setTag(listItemView);
            }else {
                listItemView = (ListItemView) convertView.getTag();
            }
            //ImageLoader.getInstance().displayImage(aEvaluateList.get(position).getEvaluatePicId(), listItemView.image);
            listItemView.merchantName.setText(aEvaluateList.get(position).getRegiUserName());
            listItemView.appraiseTime.setText(aEvaluateList.get(position).getCreateTime());
            listItemView.appraiseContent.setText(aEvaluateList.get(position).getEvaluateContent());
            listItemView.image.setVisibility(View.GONE);

            if (!aEvaluateList.get(position).getReplys().isEmpty()) {
                listItemView.replyContent.setText(aEvaluateList.get(position).getReplys().get(0).getReplyContent());
                listItemView.replyTime.setText(aEvaluateList.get(position).getReplys().get(0).getReplyTime());
            }else{
                listItemView.replyContent.setVisibility(View.GONE);
                listItemView.replyTime.setVisibility(View.GONE);
                listItemView.my_appraise_content.setVisibility(View.GONE);
                listItemView.my_appraise_time.setVisibility(View.GONE);

            }

            return convertView;
        }
    }

}
