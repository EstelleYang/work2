/**
 * 网络工具类
 * zhangxw
 * 2015-12-08
 */
package com.lzjs.uappoint.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class InternetCommon {
	/**
	 * 判断当前手机是否有网络
	 * */
	public static boolean networkStatusOk(Context context) {
		boolean netStatus = false;
		try {
			ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
			if (null != networkInfo) {
				if (networkInfo.isAvailable() && networkInfo.isConnected()) {
					netStatus = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return netStatus;
		}
		return netStatus;
	}
}
