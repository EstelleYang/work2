package com.lzjs.uappoint.act;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.ChannelManage;
import com.lzjs.uappoint.util.CheckConnectionUtils;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DisplayUtil;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SplashActivity extends Activity implements Handler.Callback{
    public static final String TAG=SplashActivity.class.getSimpleName();
    private Handler mHandler=null;

    //服务器版本名称
    private String AppVersionName;
    //服务器版本编码
    private String versionCode;
    //下载新版本apk的地址
    private String versionUrl;

    private SimpleDraweeView draweeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedUtils.putWelcomeBoolean(getApplicationContext(), true);
        draweeView = (SimpleDraweeView) findViewById(R.id.ivWelcome1);
        draweeView.setImageURI("res://com.lzjs.uappoint/" + R.drawable.splash_default);
        //SharedUtils.putInitChannel(getApplicationContext(),true);
        if(SharedUtils.getInitChannel(getApplicationContext())){//准备频道数据
            ChannelManage.getManage(ExitApplication.getInstance().getSQLHelper()).initDefaultChannel();
            SharedUtils.putInitChannel(getApplicationContext(),false);
        }
        Log.e(TAG,"dpi:"+DisplayUtil.getDPI(this));
        //判断网络是否连接
        if (CheckConnectionUtils.isNetworkConnected(getApplicationContext())&&CheckConnectionUtils.detect(this)){
            init();
        }else{
            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
  //      init();
//        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//        startActivity(intent);


    }

    private void init() {
        mHandler=new Handler(this);
        RequestParams params=new RequestParams();
        params.addQueryStringParameter("clientType", "00B");
        params.addQueryStringParameter("clientVersionType", "00A");
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.QUERY_VERSION_CODE,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e(TAG, "-------->" + responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            Log.e(TAG, "1111111" + AppVersionName);

                            JSONObject jsonData = response.getJSONObject("data");
                            if (!jsonData.isEmpty()) {
                                AppVersionName = jsonData.getString("appVersionName");
                                Log.e(TAG, "00000000000" + AppVersionName);
                                versionCode = jsonData.getString("appVersionCode");
                                versionUrl = jsonData.getString("appUpdateUrl");
                                Log.e(TAG, "************服务器的版本号" + AppVersionName);
                                if (isNeedUpdate()){
                                    mHandler.sendEmptyMessageDelayed(0, 1000);
                                    Log.e(TAG, "+++++++");
                                }else {
                                    mHandler.sendEmptyMessageDelayed(1,1000);

                                    Log.e(TAG,"+++++++++++++++++++");
                                }
                            }else{
                                mHandler.sendEmptyMessageDelayed(1,4000);
                            }
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        mHandler.sendEmptyMessageDelayed(1,2000);
                    }
                });



    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
                //需要更新
            case 0:
                showUpdataDialog();
                break;
            //不需要更新
            case 1:
                LoginMain();
                break;
        }

        return true;
    }
    /**
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤：
     *  1.创建alertDialog的builder.
     *  2.要给builder设置属性, 对话框的内容,样式,按钮
     *  3.通过builder 创建一个对话框
     *  4.对话框show()出来
     */
    protected void showUpdataDialog() {
        final AlertDialog.Builder builer = new android.support.v7.app.AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage("检测到最新版本，请及时更新");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LoginMain();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
    }
    /*
 * 从服务器中下载APK
 */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(versionUrl, pd);

                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        SplashActivity.this.finish();
    }

    /***
     * 进入主界面
     */
    private void LoginMain(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
        Log.e(TAG, "88888888888888" + path);
           //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                   URL url = new URL(path);
                  HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                  conn.setConnectTimeout(5000);
                  //获取到文件的大小
                  pd.setMax(conn.getContentLength()/1024);
                  InputStream is = conn.getInputStream();
                  File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
                  FileOutputStream fos = new FileOutputStream(file);
                  BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                   int len ;
                   int total=0;
                  while((len=bis.read(buffer))!=-1){
                           fos.write(buffer, 0, len);
                           total+= len;
                           //获取当前下载量
                           pd.setProgress(total/1024);
                       }
                    fos.close();
                  bis.close();
                   is.close();
                  return file;
               }
          else{
                  return null;
               }
        }

    /**
     * 判断有无新版本
     * @return
     */
    private boolean isNeedUpdate() {
        if (AppVersionName.equals(getVersion()) ) {
            Log.e(TAG,"......................本地的版本号"+getVersion());
            return false;
        } else {
            return true;
        }
    }

    // 获取当前版本的版本名
    private String getVersion() {
        String version = "";
        PackageManager packagemanager = getPackageManager();
        try {
            PackageInfo packageinfo;
            packageinfo = packagemanager.getPackageInfo(getPackageName(), 0);
            version = packageinfo.versionName;
            Log.e("当前版本的版本名", "当前版本的版本名" + version);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }


}
