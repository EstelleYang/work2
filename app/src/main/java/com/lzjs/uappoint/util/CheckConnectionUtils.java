package com.lzjs.uappoint.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CheckConnectionUtils {

    //判断网络是否链接

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
    //判断网络是否通畅
    public static boolean detect(Activity act) {

        ConnectivityManager manager = (ConnectivityManager) act
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        return !(networkinfo == null || !networkinfo.isAvailable());

    }
}
