package com.lzjs.uappoint.act;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.myview.CircleImageView;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.DisplayUtil;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 菜单我的
 * shalei
 * 2016/1/19.
 */
public class MineActivity extends Activity implements View.OnClickListener {
    public static final String TAG = MineActivity.class.getSimpleName();
    private TextView myName;
    private TextView mySex;
    private Button body_submit;
    private String name;
    //private String sex;
    private CircleImageView myPic;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine);
        initView();
    }

    private void initView() {
        myName = (TextView) findViewById(R.id.myName);
        //mySex = (TextView) findViewById(R.id.mySex);
        body_submit = (Button) findViewById(R.id.body_submit);
        myPic = (CircleImageView) findViewById(R.id.myPic);
        myPic.setOnClickListener(this);

        String userid = ExitApplication.getInstance().getUserid();
        Log.e("123", "=====" + userid);
        if (userid.isEmpty()) {
            myName.setText("");
            body_submit.setText("登录");
            body_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                }
            });
        } else {
            myName.setText(ExitApplication.getInstance().getUsername());
            body_submit.setText("注销登录");
            body_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清除登录状态
                    SharedUtils.putHeadimage(getApplicationContext(),"");
                    SharedUtils.putLoginUserId(getApplicationContext(),"");
                    SharedUtils.putLoginUserName(getApplicationContext(),"");
                    SharedUtils.putSignature(getApplicationContext(),"");

                    ExitApplication.getInstance().setHeadimage("");
                    ExitApplication.getInstance().setSignature("");
                    ExitApplication.getInstance().setUserid("");
                    ExitApplication.getInstance().setUsername("");

                    Toast.makeText(getBaseContext(), "注销成功", Toast.LENGTH_SHORT).show();
                    MineActivity.this.finish();
                }
            });
            isLogin = true;
        }

        if (isLogin) {
            ImageLoader.getInstance().displayImage(ExitApplication.getInstance().getHeadimage(),myPic, DisplayUtil.getOptions());
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.user_head_male_circle, myPic, DisplayUtil.getOptions());
        }
    }


    /**
     * 个人信息
     */
    public void setMyInfBtnClick(View view) {
        if (isLogin)
            //  startActivity(new Intent(getBaseContext(), UserInfoActivity.class));
            startActivityForResult(new Intent(getBaseContext(), UserInfoActivity.class), 100);

        else
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }

    /**
     * 修改密码
     */
    public void setMyUdpPwdBtnClick(View view) {
        if (isLogin)
            startActivity(new Intent(getBaseContext(), UserPassWordActivity.class));
        else
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }

    /**
     * 收货地址
     */
    public void setMyIdentifyBtnClick(View view) {
        if (isLogin)
            startActivity(new Intent(getBaseContext(), UserAddresListActivity.class));
        else
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }

    /**
     * 我的订单
     */
    public void setMyAssessBtnClick(View view) {
        if (isLogin)
            startActivity(new Intent(getBaseContext(), OrderListActivity.class));
        else
            startActivity(new Intent(getBaseContext(), LoginActivity.class));

    }

    /**
     * 我的评价
     */
    public void setMyEvaluateBtnClick(View view) {
        if (isLogin)
            startActivity(new Intent(getBaseContext(), UserAppraiseActivity.class));
        else
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }

    /**
     * 我的收藏 yangjing
     */
    public void setMyFavoriteBtnClick(View view){
        //   if (isLogin()){
        startActivity(new Intent(getBaseContext(),CollectionArticleListActivity.class));
        //   }
        // else{
        // startActivity(new Intent(getBaseContext(), LoginActivity.class));
        //   }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == 200) {
                name = data.getStringExtra("name");
                //sex = data.getStringExtra("sex");
                myName.setText(name);
                //mySex.setText(sex);
            }
        }

        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 */
                if (data != null) {
                    setPicToView(data);
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myPic:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                /**
                 * 下面这句话，与其它方式写是一样的效果，如果：
                 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 * intent.setType(""image/*");设置数据类型
                 * 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                 */
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, 1);
                break;
        }

    }


    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            myPic.setImageDrawable(drawable);

            //File file=new File(getSDPath(),Bitmap2Bytes(),"head.jpg");
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("picType", "00B");
//            String filename="head.jpeg";
            params.addBodyParameter("file",getFileFromBytes(Bitmap2Bytes(photo),getSDPath()+"/he.jpeg"));
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.FILE_UPLOAD,
                    params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            Log.e(TAG,"******************"+responseInfo.result);

                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("Response");
                                JSONObject jsondata = response.getJSONObject("data");
                                String fileId = jsondata.getString("fileId");
                                Log.e(TAG,"******************"+fileId);
                                sendPicToServer(fileId);

                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });
        }
    }


    /**
     * 上传头像到服务器
     */

    private void sendPicToServer(String headPic) {

        RequestParams params = new RequestParams();
        String regiId = SharedUtils.getLoginUserId(this);
        params.addBodyParameter("regiId", regiId);
        params.addBodyParameter("headPic",headPic);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Contants.SEND_PIC, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e(TAG, "onSuccess");
                        Log.e(TAG, responseInfo.result);
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
                            JSONObject response = object.getJSONObject("Response");
                            JSONObject jsondata = response.getJSONObject("data");
                            String desc = jsondata.getString("desc");
                            Log.e(TAG, desc);

                            if (jsondata.getBoolean("success")) {
                                Toast.makeText(MineActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MineActivity.this, "上传失败!", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(MineActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();

                    }
                }

        );


    }


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }
}
