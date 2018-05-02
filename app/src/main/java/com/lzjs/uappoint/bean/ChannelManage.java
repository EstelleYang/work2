package com.lzjs.uappoint.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.SQLException;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzjs.uappoint.dao.ChannelDao;
import com.lzjs.uappoint.db.SQLHelper;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.SharedUtils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ChannelManage {
	public static ChannelManage channelManage;
	/**
	 * 默认的用户选择频道列表
	 * */
	public static List<ChannelItem> defaultUserChannels;
	/**
	 * 默认的其他频道列表
	 * */
	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;
	private Request request;
	private static OkHttpClient client = new OkHttpClient();
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	/**
	 * 初始化频道管理类
	 * @param
	 * @throws SQLException
	 */
	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}

	/**
	 * 清除所有的频道
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();

			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(maplist.get(i).get(SQLHelper.ID)+"");
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
		} else {
            //以下为sangjp新增，避免初次安装时报错
			ChannelItem navigate = new ChannelItem();
			navigate.setId("0");
			navigate.setName("默认");
			navigate.setOrderId(0);
			navigate.setSelected(0);
			list.add(navigate);
		}
		return list;
	}

	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate= new ChannelItem();
				navigate.setId(maplist.get(i).get(SQLHelper.ID));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<ChannelItem>) cacheList;
	}

	/**
	 * 保存用户频道到数据库
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(1));
			channelDao.addCache(channelItem);
		}
	}

	/**
	 * 保存其他频道到数据库
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(0));
			channelDao.addCache(channelItem);
		}
	}

	/**
	 * 初始化数据库内的频道数据
	 */
	public void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		new Thread(new Runnable() {
			@Override
			public void run() {

				try{
					deleteAllChannel();
			FormBody formBody = new FormBody.Builder()
					.add("cityName", "")
					.add("pageNo","")
					.build();
			request = new Request.Builder().url(Contants.CHANNEL_LISTS).get().build();
			Response response = client.newCall(request).execute();
			String res=response.body().string();
			if(response.isSuccessful()&&!res.isEmpty() && res.indexOf("}")!=-1){
				JSONObject obj= JSONObject.parseObject(res);
				if(obj.getString("result").equals("200")){
					JSONObject objdata=obj.getJSONObject("response");
					JSONArray objArray=objdata.getJSONArray("datas");
					int length = objArray.size();
					if(objArray != null && length > 0){
						//new ChannelItem(1, "推荐", 1, 1)
						ChannelItem item=null;
						for(int i = 0; i < length; i++){
							item = new ChannelItem();
							item.setId(objArray.getJSONObject(i).getString("channelId"));
							item.setName(objArray.getJSONObject(i).getString("channelName"));
							item.setOrderId(i+1);
							item.setSelected(0);
							defaultOtherChannels.add(item);
						}
					}
				}
			}
			defaultUserChannels.add(new ChannelItem(0+"", "推荐", 0, 1));
			saveUserChannel(defaultUserChannels);
			saveOtherChannel(defaultOtherChannels);

		} catch (IOException e) {
			e.printStackTrace();
		}
			}
		}).start();

	}
}
