package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.DoubleColumnConverter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.ExpertAdapter;
import com.lzjs.uappoint.bean.Doctor;
import com.lzjs.uappoint.util.Contants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 专家列表
 */
public class ExpertFragment extends Fragment implements PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {

	private static final String ARG_POSITION = "departId";
	private static final String TAG ="ExpertFragment";
	public Context context; // 存储上下文对象
	public Activity activity; // 存储上下文对象
	private String departId;
	private ListView expertListView;
	private List<Doctor> expertList;
	private static OkHttpClient client = new OkHttpClient();
	private PullToRefreshView mPullToRefreshView;
	private ExpertAdapter expertAdapter;
	public static ExpertFragment newInstance(String position) {
		ExpertFragment f = new ExpertFragment();
		Bundle b = new Bundle();
		b.putString(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		departId = getArguments().getString(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		context = getActivity();
		activity = getActivity();
		View view = inflater.inflate(R.layout.activity_expert_list, container, false);
		expertListView = (ListView) view.findViewById(R.id.expert_list);
		getData();
		mPullToRefreshView= (PullToRefreshView) view.findViewById(R.id.expert_list_pull_view);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		// mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setLastUpdated(getTime());
		mPullToRefreshView.setEnablePullTorefresh(false);
		mPullToRefreshView.setEnablePullLoadMoreDataStatus(true);
		expertListView.setOnItemClickListener(this);
	}
	/*public  DisplayImageOptions getListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// 设置图片在下载期间显示的图片
				.showImageOnLoading(R.drawable.icon_empty)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.icon_error)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.icon_error)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(false)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisc(true)
				// 保留Exif信息
				.considerExifParams(true)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				// 设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//设置图片的解码配置
				.considerExifParams(true)
				// 设置图片下载前的延迟
				.delayBeforeLoading(100)// int
				// delayInMillis为你设置的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				// .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 淡入
				.build();
		return options;
	}*/

	private void getData() {
		expertList = new ArrayList<>();
		RequestParams params = new RequestParams();
		//params.addQueryStringParameter("cityName", "");
		//params.addQueryStringParameter("pageNo", mIndex+"");
		params.addQueryStringParameter("paramStr", departId);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Contants.HOME_ZLZJ_LIST_MORE , params,
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
						JSONObject obj = JSONObject.parseObject(responseInfo.result);
						if (obj.getString("result").equals("200")) {
							JSONObject objdata = obj.getJSONObject("response");
							JSONArray objArray = objdata.getJSONArray("datas");
							int length = objArray.size();
							if (objArray != null && length > 0) {
								Doctor doctor = null;
								for (int i = 0; i < length; i++) {
									doctor = new Doctor();
									doctor.setUserid(objArray.getJSONObject(i).getString("userid"));
									doctor.setUsername(objArray.getJSONObject(i).getString("username"));
									doctor.setLoginid(objArray.getJSONObject(i).getString("loginid"));
									doctor.setHeadimage(objArray.getJSONObject(i).getString("headimage"));
									doctor.setIntroduce(objArray.getJSONObject(i).getString("introduce"));
									doctor.setSpecial(objArray.getJSONObject(i).getString("special"));
									doctor.setHiscode(objArray.getJSONObject(i).getString("hiscode"));
									doctor.setHisname(objArray.getJSONObject(i).getString("hisname"));
									doctor.setTitlecode(objArray.getJSONObject(i).getString("titlecode"));
									doctor.setTitlename(objArray.getJSONObject(i).getString("titlename"));
									doctor.setDeptcode(objArray.getJSONObject(i).getString("deptcode"));
									doctor.setDeptname(objArray.getJSONObject(i).getString("deptname"));
									doctor.setPostcode(objArray.getJSONObject(i).getString("postcode"));
									doctor.setPostname(objArray.getJSONObject(i).getString("postname"));
									doctor.setDegreecode(objArray.getJSONObject(i).getString("degreecode"));
									doctor.setDegreename(objArray.getJSONObject(i).getString("degreename"));
									expertList.add(doctor);
								}
							}
							expertAdapter=new ExpertAdapter(getActivity());
							expertAdapter.addList(expertList);
							expertListView.setAdapter(expertAdapter);
						}
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						//Toast.makeText(EqDetailActivity.this,"网络请求失败，请检查网络",Toast.LENGTH_SHORT).show();
					}
				});

	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Intent intent = new Intent(activity, ExpertsDetailActivity.class);
		Bundle mbundle = new Bundle();
		mbundle.putString("expertId", expertList.get(i).getUserid());
		mbundle.putString("FLAG", 1+"");
		intent.putExtras(mbundle);
		startActivity(intent);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		//h.sendEmptyMessage(1);
		//getData(Mode.UP);
	}

}