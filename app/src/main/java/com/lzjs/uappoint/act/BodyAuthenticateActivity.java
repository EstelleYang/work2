package com.lzjs.uappoint.act;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.util.Contants;
import com.redare.imagepicker.widget.ImagePickerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BodyAuthenticateActivity extends BaseActivity {
    public static final String TAG ="BodyAuthenticateActivity" ;
    boolean isImgShow;
    private  String titelShow="";
    private List<String> path = new ArrayList<>();
    private Toolbar mToolbar;
    private TextView tv_title;
    public static final int REQUEST_CODE = 123;
    private final int REQUEST_CODE_GALLERY = 1001;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private ImagePickerView imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_body_authenticate);

        isImgShow=getIntent().getBooleanExtra("isImgShow",false);
        titelShow=getIntent().getStringExtra("titleShow");

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        tv_title=(TextView)findViewById(R.id.toolbar_name);
        mToolbar.setTitle("");
        tv_title.setText(titelShow);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(isImgShow){
            findViewById(R.id.rl_imgs).setVisibility(View.GONE);
        }
        Button body_submit= (Button) findViewById(R.id.body_submit);
        body_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }else{
            //
        }
        imagePicker = (ImagePickerView) findViewById(R.id.imagePicker);
        imagePicker.setNoImgResource(R.layout.add_img);
        imagePicker.setColumnNumber(5);
    }
    //点击选择图片
    public void uploadImgClick(View view){
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_GALLERY: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imagePicker.onActivityResult(requestCode,resultCode,data);
        path=imagePicker.getImageList();
        String ID = java.util.UUID.randomUUID().toString();
        String paramId = "UT" + ID;
        for(int i=0;i<path.size();i++){
            String imgId = "I" + ID + "_" + i;
            sendPicToServer(path.get(i),paramId,imgId);
        }
    }

    private void sendPicToServer(String headPic,String paramId,String imgId) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("paramId", paramId);
        params.addBodyParameter("imgId", imgId);
        //params.addBodyParameter("file",headPic);
        params.addBodyParameter("file", new File(headPic));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Contants.FILE_UPLOAD, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject object = JSON.parseObject(responseInfo.result);
                        if (object.getString("result").equals("200")) {
/*                            JSONObject response = object.getJSONObject("response");
                            JSONObject jsondata = response.getJSONObject("data");
                            String desc = jsondata.getString("desc");
                            if (jsondata.getBoolean("success")) {
                                Toast.makeText(BodyAuthenticateActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BodyAuthenticateActivity.this, "上传失败!", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(BodyAuthenticateActivity.this, "网络错误,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
