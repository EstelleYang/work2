/**
 * 存放用户配置类
 * zhangxw
 * 2015-12-08
 *
 */
package com.lzjs.uappoint.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.lzjs.uappoint.bean.ChannelItem;

import java.util.ArrayList;
import java.util.List;

//实现标记的写入与读取
public class SharedUtils {
	private static final String FILE_NAME = "uappoint";
	private static final String MODE_NAME = "welcome";
	private static final String CHECU_UPDATE = "checkUpdate";//检查更新

	// 获取boolean类型的值
	public static boolean getWelcomeBoolean(Context context) {
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getBoolean(MODE_NAME, false);
	}

	// 写入Boolean类型的值
	public static void putWelcomeBoolean(Context context, boolean isFirst) {
		Editor editor = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putBoolean(MODE_NAME, isFirst);
		editor.commit();
	}

	// 是否检测更新
	public static boolean getCheckVersionUpdateBoolean(Context context){
		return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).getBoolean(CHECU_UPDATE,true);
	}

	// 更改检查更新的状态
	public static void putCheckVersionUpdateBoolean(Context context,boolean checkUpdate){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(CHECU_UPDATE,checkUpdate);
		editor.commit();
	}

	// 写入一个String类型的数据
	public static void putCityName(Context context, String cityName) {

		Editor editor = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putString("cityName", cityName);
		editor.commit();
	}

	// 获取String类型的值
	public static String getCityName(Context context) {

		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getString("cityName", "兰州市");
	}

	/**
	 * 获取定位城市
	 * @param context
	 * @return
	 */
	public static String getLocationCityName(Context context){
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("locationCityName", "");
	}

	/**
	 *插入定位城市
	 * @param context
	 * @param cityName
	 * @return
	 */
	public static void putLocationCityName(Context context, String cityName){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putString("locationCityName", cityName);
		editor.commit();
	}

	public static void putLoginUserId(Context context, String userId){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putString("userId", userId);
		editor.commit();
	}

	public static String getLoginUserId(Context context){
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("userId", "");
	}

    public static void putLoginUserName(Context context, String userName){
        Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("userName", userName);
        editor.commit();
    }

    public static String getLoginUserName(Context context){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("userName", "");
    }

    public static void putSignature(Context context, String singature){
        Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("singature", singature);
        editor.commit();
    }

    public static String getSignature(Context context){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("signature", "");
    }

    public static void putHeadimage(Context context, String headimage){
        Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("headimage", headimage);
        editor.commit();
    }

    public static String getHeadimage(Context context){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("headimage", "");
    }

    public static void putImei(Context context, String imei){
        Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("imei", imei);
        editor.commit();
    }

    public static String getImei(Context context){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("imei", "");
    }

	public static void putInitChannel(Context context, boolean userId){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean("initChannel", userId);
		editor.commit();
	}

	public static boolean getInitChannel(Context context){
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean("initChannel", true);
	}

	public static void putDepartChannelList(Context context, List<ChannelItem> list) {
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove("DepartChannelSize");
        editor.putInt("DepartChannelSize", list == null ? 0 : list.size());
        editor.commit();
		for(int i=0;i<list.size();i++) {
			editor.remove("DepartChannelId_" + i);
			editor.putString("DepartChannelId_" + i, list.get(i).getId());
            editor.remove("DepartChannelName_" + i);
            editor.putString("DepartChannelName_" + i, list.get(i).getName());
		}
		editor.commit();
	}

    public static ArrayList<ChannelItem> loadDepartChannelList(Context context){
        ArrayList<ChannelItem> list = new ArrayList<ChannelItem>();
        list.add(new ChannelItem("0", "全部", 0, 1));
        int size = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getInt("DepartChannelSize", 0);
        for(int i=0;i<size;i++) {
            ChannelItem item = new ChannelItem();
            item.setId(context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("DepartChannelId_"+i, ""));
            item.setName(context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("DepartChannelName_"+i, ""));
            item.setOrderId(i+1);
            item.setSelected(Integer.parseInt("1"));
            list.add(item);
        }
        return list;
    }
}