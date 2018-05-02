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
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.ArticleAdapter;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.bean.Product;
import com.lzjs.uappoint.myview.CommonAdapter;
import com.lzjs.uappoint.myview.ViewHolder;
import com.lzjs.uappoint.util.Contants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 学术列表
 */
public class LearningFragment extends Fragment implements PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {

	private static final String ARG_POSITION = "channelId";
	private static final String TAG ="LearningFragment" ;
	public Context context; // 存储上下文对象
	public Activity activity; // 存储上下文对象
	private String channelId;
	private ListView articleListView;
	private List<Article> articleList;
	private Request request;
	private static OkHttpClient client = new OkHttpClient();
	private PullToRefreshView mPullToRefreshView;
	private int mIndex = 0;
	private ArticleAdapter articleAdapter;
	public static LearningFragment newInstance(String position) {
		LearningFragment f = new LearningFragment();
		Bundle b = new Bundle();
		b.putString(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		channelId = getArguments().getString(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		context = getActivity();
		activity = getActivity();
		View view = inflater.inflate(R.layout.activity_article_list, container, false);
		articleListView = (ListView) view.findViewById(R.id.article_list);
		getData(Mode.DOWN);
		mPullToRefreshView= (PullToRefreshView) view.findViewById(R.id.article_list_pull_view);

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
		articleListView.setOnItemClickListener(this);
	}
	public  DisplayImageOptions getListOptions() {
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
	}

	private void getData(final Mode mode) {
		switch (mode) {
			case UP:
				mIndex++;
				break;
			case DOWN:
				mIndex = 0;
				break;
		}
		articleList = new ArrayList<>();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("paramStr", channelId);
		params.addQueryStringParameter("pageNo", mIndex+"");
		params.addQueryStringParameter("reqKeyword", "");
		//params.addQueryStringParameter("channelId", channelId);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				Contants.ART_LISTS,
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
						JSONObject obj = JSONObject.parseObject(responseInfo.result);
						if (obj.getString("result").equals("200")) {
							JSONObject objdata = obj.getJSONObject("response");
							JSONArray objArray = objdata.getJSONArray("datas");
							int length = objArray.size();
							if (objArray != null && length > 0) {
								Article article = null;
								for (int i = 0; i < length; i++) {
									article = new Article();
									article.setImgUrl(objArray.getJSONObject(i).getJSONArray("images").get(0).toString());
									article.setTital(objArray.getJSONObject(i).getString("prititle"));
									article.setId(objArray.getJSONObject(i).getString("topicid"));
									article.setUrl(objArray.getJSONObject(i).getString("topicurl"));
									article.setCreateData(objArray.getJSONObject(i).getString("createdate"));
									article.setReplyCount(objArray.getJSONObject(i).getString("replyCount"));
									article.setVoteCount(objArray.getJSONObject(i).getString("voteCount"));
									article.setSceneTitle(objArray.getJSONObject(i).getString("topicabstract"));
									articleList.add(article);
								}
							}
							articleAdapter=new ArticleAdapter(getActivity());
							articleAdapter.addList(articleList);
							articleListView.setAdapter(articleAdapter);
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
		Intent intent = new Intent(activity, ArticleDetailActivity.class);
		Bundle mbundle = new Bundle();
		mbundle.putString("articeId", articleList.get(i).getId());
		mbundle.putString("url", articleList.get(i).getUrl());
		mbundle.putString("FLAG", 1+"");
		intent.putExtras(mbundle);
		startActivity(intent);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		h.sendEmptyMessage(1);
		getData(Mode.UP);
	}
	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				h.postAtTime(new Runnable() {
					@Override
					public void run() {
						mPullToRefreshView.onFooterRefreshComplete();
					}
				}, SystemClock.uptimeMillis() + 1000);

			}else if (msg.what==3){
				//Toast.makeText(EquiListActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
			}
		}
	};
	enum Mode {
		DOWN, UP
	}
}