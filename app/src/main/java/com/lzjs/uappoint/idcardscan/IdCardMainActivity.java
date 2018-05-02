package com.lzjs.uappoint.idcardscan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.act.RegisterActivity;
import com.lzjs.uappoint.idcardscan.youtu.Youtu;
import com.lzjs.uappoint.idcardscan.youtu.sign.YoutuSign;
import com.google.gson.Gson;
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

//import okhttp3.Call;

/**
 * 注意6.0以上判断权限
 */
public class IdCardMainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    public final static String API_URL = "http://api.youtu.qq.com/youtu/";
    private static final String DEFAULT_PATH = "/sdcard/";
    private static final String DEFAULT_NAME = "default.jpg";
    //private static final String DEFAULT_PATH = "/images/";
    //private static final String DEFAULT_NAME = "*";
    private static final String DEFAULT_TYPE = "default";
    private static final String TAG = "idcard";
    private static int EXPIRED_SECONDS = 2592000;
    SurfaceHolder surfaceHolder;
    //private PreviewBorderView mPreviewBorderView;
    private CameraManager cameraManager;
    private boolean hasSurface;
    private Intent mIntent;
    private String filePath;
    private String fileName;
    private String type;
    private Button take, light;
    private boolean toggleLight;
    private View progress;
    /**
     * 拍照回调
     */
    @SuppressWarnings("deprecation")
    Camera.PictureCallback myjpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            // 根据拍照所得的数据创建位图
            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                    data.length);
            Bitmap bitmap2 = adjustPhotoRotation(bitmap, 90);
            int height = bitmap2.getHeight();
            int width = bitmap2.getWidth();
            int scanWidth = width * 15 / 16;
            int scanHeight = (int) (scanWidth * 0.63f);
            final Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, (width - scanWidth) / 2, (height - scanHeight) / 2, scanWidth, scanHeight);
//            保存图片到本地
//            File path = new File(filePath);
//            if (!path.exists()) {
//                path.mkdirs();
//            }
//            File file = new File(path, type + "_" + fileName);
//
//            FileOutputStream outStream = null;
//            try {
//                // 打开指定文件对应的输出流
//                outStream = new FileOutputStream(file);
//                // 把位图输出到指定文件中
//                bitmap1.compress(Bitmap.CompressFormat.JPEG,
//                        100, outStream);
//                outStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            JSONObject json = new JSONObject();

            String imageData = bitmapToBase64(bitmap1);
            try {
                json.put("image", imageData);
                json.put("app_id", AppID.YOUTU_APPID);
                json.put("card_type", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringBuffer mySign = new StringBuffer("");
            YoutuSign.appSign(AppID.YOUTU_APPID, AppID.YOUTU_SECRETID, AppID.YOUTU_SECRETKEY,
                    System.currentTimeMillis() / 1000 + EXPIRED_SECONDS,
                    AppID.QQNumber, mySign);
            progress.setVisibility(View.VISIBLE);
//            使用优图API请求
            try {
                Log.d("aaa", "1------------------------------------------------");
                JSONObject jsonObject = new Youtu(AppID.YOUTU_APPID, AppID.YOUTU_SECRETID, AppID.YOUTU_SECRETKEY, AppID.END_POINT, "").IdcardOcr(bitmap1, 0);
                Log.d("aaa", "2------------------------------------------------");
                progress.setVisibility(View.GONE);
                IDCardResponse idcard = new Gson().fromJson(jsonObject.toString(), IDCardResponse.class);
                Log.d("idcard", idcard.getId());
                Log.d("name", idcard.getName());
            } catch (IOException | JSONException | KeyManagementException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            //使用OKGO请求
//            OkGo.post(API_URL + "ocrapi/idcardocr").upJson(json)
//                    .headers("accept", "*/*")
//                    .headers("user-agent", "youtu-android-sdk")
//                    .headers("Authorization", mySign.toString())
//                    .headers("Content-Type", "text/json")
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(String s, Call call, okhttp3.Response response) {
//                            Log.d("ssss", s);
//                            IDCardResponse idcard = new Gson().fromJson(s, IDCardResponse.class);
//                            if (idcard.getErrorcode() == 0) {
//                                //TODO 拿到身份证扫描数据
//                                Intent it = new Intent();
//                                it.putExtra("idcard", idcard.getId());
//                                it.putExtra("name", idcard.getName());
//                            } else {
//                                decode(idcard.getErrorcode());
//                            }
//                            progress.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError(Call call, okhttp3.Response response, Exception e) {
//                            progress.setVisibility(View.GONE);
//                            e.printStackTrace();
//                            try {
//                                Log.d(TAG, response.body().string());
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                            }
//                        }
//                    });

        }
    };

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void decode(int code) {
        String s = "识别失败";
        switch (code) {
            case -7001:
                s = "未检测到身份证，请对准边框(请避免拍摄时倾角和旋转角过大、摄像头)";
                break;
            case -7002:
                s = "请使用第二代身份证件进行扫描";
                break;
            case -7003:
                s = "不是身份证正面照片(请使用带证件照的一面进行扫描)";
                break;
            case -7005:
                s = "确保扫描证件图像清晰";
                break;
            case -7006:
                s = "请避开灯光直射在证件表面";
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(s)
                .setPositiveButton("重新扫描", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cameraManager.takeGo();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("sangjp11111============","1");
        setContentView(R.layout.idcard_activity_main);
        Log.d("sangjp11111============","2");

        progress = findViewById(R.id.progress);
        Log.d("sangjp11111============","3");
        initIntent();
        Log.d("sangjp11111============","4");
        initLayoutParams();
        Log.d("sangjp11111============","5");
    }

    private void initIntent() {
        mIntent = getIntent();
        filePath = mIntent.getStringExtra("path");
        fileName = mIntent.getStringExtra("name");
        type = mIntent.getStringExtra("type");
        if (filePath == null) {
            filePath = DEFAULT_PATH;
        }
        if (fileName == null) {
            fileName = DEFAULT_NAME;
        }
        if (type == null) {
            type = DEFAULT_TYPE;
        }
        Log.e("TAG", filePath + "/" + fileName + "_" + type);
    }

    /**
     * 重置surface宽高比例为3:4，不重置的话图形会拉伸变形
     */
    private void initLayoutParams() {
        take = (Button) findViewById(R.id.take);
        light = (Button) findViewById(R.id.light);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraManager.takePicture(null, null, myjpegCallback);
            }
        });
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleLight) {
                    toggleLight = true;
                    cameraManager.openLight();
                } else {
                    toggleLight = false;
                    cameraManager.offLight();
                }
            }
        });

        //mPreviewBorderView = (PreviewBorderView) findViewById(R.id.borderview);

    }

    @Override
    protected void onResume() {
        super.onResume();

        int checkPermission = ContextCompat.checkSelfPermission(IdCardMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IdCardMainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Log.d("TTTT", "弹出提示");
            return;
        } else {
            //mLocationClient.start();
        }

        /*if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }*/

        /**
         * 初始化camera
         */
        cameraManager = new CameraManager();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();

        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 初始camera
     *
     * @param surfaceHolder SurfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            cameraManager.startPreview();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    public Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPause() {
        /**
         * 停止camera，是否资源操作
         */
        cameraManager.stopPreview();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

}
