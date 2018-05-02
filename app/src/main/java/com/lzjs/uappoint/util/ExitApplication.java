/**
 * 应用程序退出
 * 存放activity的队列
 * zhangxw
 * 2015-12-08
 */
package com.lzjs.uappoint.util;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.UserInfo;
import com.lzjs.uappoint.db.SQLHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzjs.uappoint.fresco.utils.ImagePipelineConfigFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xutils.x;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 */

public class ExitApplication extends Application {
	
    private List activitys = null;
    private static ExitApplication instance;
    //public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private SQLHelper sqlHelper;

    public TextView mLocationResult;
    //public Vibrator mVibrator;
    public String cityName;

    public AMapLocationClient locationClient = null;
    public AMapLocationClientOption locationOption = null;

    public ExitApplication() {
        activitys = new LinkedList();
    }

    private String signature;
    private String userid;
    private String username;
    private String headimage;
    private String imei;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        //mLocationClient = new LocationClient(this.getApplicationContext());
        //mMyLocationListener = new MyLocationListener();
        //mLocationClient.registerLocationListener(mMyLocationListener);
        //locationClient = new AMapLocationClient(this.getApplicationContext());
        //locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        //locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
        // 设置定位监听
        //locationClient.setLocationListener(mMyLocationListener);
        //mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        instance=this;
        initUserInfo();
        x.Ext.init(this);
    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

/*        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();*/
        //配置图片加载的插件
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .diskCacheSize(50 * 1024 * 1024)
                .memoryCacheSize(2 * 1024 * 1024)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initUserInfo() {
        //比较IMEI是否一致
        String imeiStore = SharedUtils.getImei(getApplicationContext());
        //String imeiThis = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        String imeiThis = ToolsUtil.getImeiCode(getApplicationContext());

        if (imeiStore == null || imeiStore.trim().equals("") || !imeiStore.equals(imeiThis)) {
            //清除登录状态
            setImei(imeiThis == null ? "" : imeiThis);
            setHeadimage("");
            setSignature("");
            setUserid("");
            setUsername("");

            SharedUtils.putImei(getApplicationContext(),imeiThis == null ? "" : imeiThis);
            SharedUtils.putHeadimage(getApplicationContext(),"");
            SharedUtils.putLoginUserId(getApplicationContext(),"");
            SharedUtils.putLoginUserName(getApplicationContext(),"");
            SharedUtils.putSignature(getApplicationContext(),"");
        } else {
            setImei(imeiThis == null ? "" : imeiThis);
            setHeadimage(SharedUtils.getHeadimage(getApplicationContext()));
            setSignature(SharedUtils.getSignature(getApplicationContext()));
            setUserid(SharedUtils.getLoginUserId(getApplicationContext()));
            setUsername(SharedUtils.getLoginUserName(getApplicationContext()));
        }
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     * 
     * @return
     */
    public static ExitApplication getInstance() {
        if (null == instance) {
            instance = new ExitApplication();
        }
        return instance;
    }
 
    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if(!activitys.contains(activity)){
                activitys.add(activity);
            }
        }else{
            activitys.add(activity);
        }
    }
 
    // 遍历所有Activity并finish
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
        	for (int i = 0; i < activitys.size(); i++) {
				Activity activity = (Activity) activitys.get(i);
				activity.finish();
			}
        }
        System.exit(0);
    }

    // 遍历所有Activity并finish
    public void finishAll() {
        if (activitys != null && activitys.size() > 0) {
            for (int i = 0; i < activitys.size(); i++) {
                Activity activity = (Activity) activitys.get(i);
                activity.finish();
            }
        }
    }
    /** 获取Application */
    public static ExitApplication getApp() {
        return instance;
    }

    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(instance);
        return sqlHelper;
    }

    /** 摧毁应用进程时候调用 */
    public void onTerminate() {
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }
    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(!cityName.isEmpty()){
                //mLocationClient.stop();
                SharedUtils.putWelcomeBoolean(getApplicationContext(), false);
                if(cityName.equals(aMapLocation.getCity())){
                    SharedUtils.putLocationCityName(getApplicationContext(), cityName);
                }else{
                    SharedUtils.putLocationCityName(getApplicationContext(), aMapLocation.getCity());
                }
            }else{
                cityName = aMapLocation.getCity();
                logMsg(cityName);
            }
        }
    }


    /**
     * 显示请求字符串
     * @param str
     */
    public void logMsg(final String str) {
        try {
            if (mLocationResult != null){
                if(!str.isEmpty()){
                    mLocationResult.setText(str);
                    SharedUtils.putWelcomeBoolean(getApplicationContext(), false);
                    SharedUtils.putLocationCityName(getApplicationContext(), str);
                    //mLocationClient.stop();
                    /*locationClient.stopLocation();
                    locationClient.onDestroy();*/
                }
                Timer timer = new Timer();
                timer.schedule(new DestoryLocation(), 60*1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class DestoryLocation extends TimerTask {
        @Override
        public void run() {
            if(cityName.isEmpty()){
                Log.i("msgInfo", "定位失败了");
                SharedUtils.putWelcomeBoolean(getApplicationContext(), false);
                SharedUtils.putLocationCityName(getApplicationContext(), "");
                //mLocationClient.stop();
                /*locationClient.stopLocation();
                locationClient.onDestroy();*/
            }
        }
    }
}
