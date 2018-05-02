package com.lzjs.uappoint.act;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.UserInfo;
import com.lzjs.uappoint.idcardscan.IdCardMainActivity;
import com.lzjs.uappoint.magicimage.MagicMainActivity;
import com.lzjs.uappoint.magicimage.constant.ErrorEnum;
import com.lzjs.uappoint.magicimage.entity.IdentifyResult;
import com.lzjs.uappoint.magicimage.util.BitMapUtils;
import com.lzjs.uappoint.magicimage.util.TecentHttpUtil;
import com.lzjs.uappoint.myview.CountDownTimerUtils;
import com.lzjs.uappoint.util.AllCapTransformationMethod;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.IDCardUtil;
import com.lzjs.uappoint.util.PwdCheckUtil;
import com.lzjs.uappoint.util.StringUtils;
import com.lzjs.uappoint.util.ToolsUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class RegisterActivity extends AppCompatActivity implements Handler.Callback{

    private static final String TAG ="RegisterActivity" ;
    private Button registered_confirm;
    private TextView verification_number_bt;
    //private TextView center_title_text;
    private EditText et_phone_number;
    private EditText et_password;
    private EditText et_password_confirm;
    private EditText idcardno;
    private EditText number_yzm;
    private Handler mHandler;
    private ImageView title1_back;
    private TextView tv_camera;
    private TextView tv_tooltip;
    private final static int REQUEST_IMAGE = 100;
    private Bitmap bitmap = null, copy = null;
    private String p = null;
    private AlertDialog dialogInfo;// 识别身份证信息弹窗
    private boolean flag=false;
    private View progress;
    private UserInfo userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        userinfo = new UserInfo();
    }

    private void initView() {
        registered_confirm= (Button) findViewById(R.id.registered_confirm);
        //center_title_text=(TextView)findViewById(R.id.center_title_text);
        verification_number_bt= (TextView) findViewById(R.id.verification_number_bt);
        et_phone_number= (EditText) findViewById(R.id.et_phone_number);
        idcardno= (EditText) findViewById(R.id.idcardno);
        et_password= (EditText) findViewById(R.id.et_password);
        et_password_confirm= (EditText) findViewById(R.id.et_password_confirm);
        number_yzm= (EditText) findViewById(R.id.number_yzm);
        title1_back= (ImageView) findViewById(R.id.title1_back);
        tv_camera= (TextView) findViewById(R.id.tv_camera);
        tv_tooltip=(TextView) findViewById(R.id.tv_tooltip) ;
        mHandler=new Handler(this);
        progress = findViewById(R.id.progress);

        //center_title_text.setText(R.string.register);
        final CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(verification_number_bt, 60000, 1000);
        registered_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();//校验短信验证码
                Log.i(TAG, "onClick: flag"+flag);
            }
        });
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 应用没有读取手机外部存储的权限
                    // 申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            100);
                }else {
                    selectImage();
                }
            }
        });
        idcardno.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X' };
                return numberChars;
            }
        });
        idcardno.setTransformationMethod(new AllCapTransformationMethod(true));  //自动转换小写为大写

        verification_number_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verification_number_bt.setClickable(false);
                //verification_number_bt.setTextColor(0x88000000);
                if (validPhone()) {
                    mCountDownTimerUtils.start();
                    String mobile=et_phone_number.getText().toString();
                    IdentifyingCode(mobile);//下发短信验证码
                }
            }
        });
        title1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

    /**
     * 注册验证
     * @return
     */

    private void check(){
        userinfo.setMobile("");
        if (validIdCard() && validPasswd() && validPhone() && validSmsCode()) {
            final String mobile=et_phone_number.getText().toString();
            String smscode=number_yzm.getText().toString();

            RequestParams params = new RequestParams();
            params.addQueryStringParameter("mobile", mobile);
            params.addQueryStringParameter("smscode", smscode);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.JUDGMENT_VALIDATE_CODE,
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
                            Log.i(TAG, "onSuccess: " + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("response");
                                //JSONObject jsondata = response.getJSONObject("data");
                                if(response.get("data").equals("success")){
                                    userinfo.setMobile(mobile);
                                    regist();
                                }else{
                                    Toast.makeText(RegisterActivity.this, "手机号或验证码错误！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(RegisterActivity.this,"网络错误，请检查网络！",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * 注册
     */
    public void regist(){
        if (validIdCard() && validPasswd() && validPhone() && validSmsCode()) {
            //String mobile=et_phone_number.getText().toString();
            String pwd=et_password.getText().toString();
            //String vcode=number_yzm.getText().toString();
            //String userid=idcardno.getText().toString();

            userinfo.setUserpwd(pwd);

            RequestParams params = new RequestParams();

            params.addQueryStringParameter("userid", userinfo.getUserid());
            params.addQueryStringParameter("username", userinfo.getUsername());
            params.addQueryStringParameter("userpwd", ToolsUtil.md5Encode(userinfo.getUserid()+pwd));
            params.addQueryStringParameter("mobile", userinfo.getMobile());
            //String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            //String imei = "123dddd-ddfef-dd";
            params.addQueryStringParameter("imei",ExitApplication.getInstance().getImei());
            params.addQueryStringParameter("sexname", userinfo.getSexname());
            params.addQueryStringParameter("address", userinfo.getAddress());
            //传中文时
            //params.addBodyParameter("deviceIntoTime", URLEncoder.encode(userinfo.getSexname(), "utf-8"));
            Log.v(TAG+"===", ExitApplication.getInstance().getImei());

            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.REGISTER_CREATE_PASS,
                    params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            registered_confirm.setText("注册中...");
                        }
                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                        }
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            try {
                                Log.i(TAG, "onSuccess: " + responseInfo.result);
                                JSONObject object = JSON.parseObject(responseInfo.result);
                                if (object.getString("result").equals("200")) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                    RegisterActivity.this.finish();
                                }else{
                                    JSONObject respObj = object.getJSONObject("response");
                                    JSONObject errObj = respObj.getJSONObject("errors");
                                    String errtext = errObj.getString("errorText");
                                    tv_tooltip.setVisibility(View.VISIBLE);
                                    tv_tooltip.setText(errtext);
                                    registered_confirm.setText("注册");
                                }
                            }catch (Exception e){
                                tv_tooltip.setVisibility(View.VISIBLE);
                                tv_tooltip.setText("注册时发生系统错误！");
                                registered_confirm.setText("注册");
                                Log.e(TAG, Log.getStackTraceString(e));

                            }
                        }
                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(RegisterActivity.this, "系统异常，请稍后再试", Toast.LENGTH_SHORT).show();
                        }});
        }
    }

    /**
     * 获取短信验证码
     */
    public void IdentifyingCode(String mobile){
        if(StringUtils.isMobileNO(mobile)){
        //if (validPhone()) {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("mobile", mobile);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.GET_VALIDATE_CODE,
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
                            Log.i(TAG, "onSuccess: " + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                /*
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 60; i >= 0; i--) {
                                            Message message = Message.obtain();
                                            message.arg1 = i;
                                            message.what = 1;
                                            mHandler.sendMessage(message);
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }).start();
*/
                                Toast.makeText(RegisterActivity.this,"验证码已下发至手机请注意查收！",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(RegisterActivity.this,"网络错误，请检查网络！",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else
            Toast.makeText(RegisterActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                if (msg.arg1 == 0) {
                    verification_number_bt.setText("重新获取");
                    verification_number_bt.setClickable(true);
                    verification_number_bt.setTextColor(0xff000000);
                } else {
                    verification_number_bt.setText(msg.arg1 + "秒后重新获取");
                }

                break;
        }
        return true;
    }

    private void selectImage(){
        MultiImageSelector.create(RegisterActivity.this)
                .showCamera(true) // 是否显示相机. 默认为显示
//                .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(RegisterActivity.this, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                if(path != null && path.size() > 0){
                    p = path.get(0);
//                    onSelected();
                    bitmap = getImage(p);
                    //imageView.setImageBitmap(bitmap);
                    if (bitmap != null) {
                        //识别
                        idcardOCR();
                    }
                }
            }
        }
    }

    /**
     * 获取压缩后的图片
     * @param srcPath
     * @return
     */
    private Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void idcardOCR() {
        progress.setVisibility(View.VISIBLE);
        //清理用户身份证信息
        //userinfo = new UserInfo();
        userinfo.setUserid("");
        userinfo.setUsername("");
        userinfo.setAddress("");
        userinfo.setSexname("");
        idcardno.setText("");

        TecentHttpUtil.uploadIdCard(BitMapUtils.bitmapToBase64(bitmap), "0", new TecentHttpUtil.SimpleCallBack() {
            @Override
            public void Succ(String res) {
                IdentifyResult result = new Gson().fromJson(res, IdentifyResult.class);
                progress.setVisibility(View.GONE);
                if(result != null){
                    if(result.getErrorcode() == 0){
                        // 识别成功
                        idcardno.setText(result.getId());
                        userinfo.setUserid(result.getId());
                        userinfo.setUsername(result.getName());
                        userinfo.setAddress(result.getAddress());
                        userinfo.setSexname(result.getSex());
                        showDialogInfo(result);
                    }else {
                        tv_tooltip.setVisibility(View.VISIBLE);
                        tv_tooltip.setText(ErrorEnum.getErrormsg(result.getErrorcode()));
                    }
                }
            }

            @Override
            public void error() {
                progress.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 显示对话框
     * @param result
     */
    private void showDialogInfo(final IdentifyResult result){
        StringBuilder sb = new StringBuilder();
        sb.append("姓名：" + result.getName() + "\n");
        sb.append("性别：" + result.getSex() + "\t" + "民族：" + result.getNation() + "\n");
        sb.append("出生：" + result.getBirth() + "\n");
        sb.append("住址：" + result.getAddress() + "\n" + "\n");
        sb.append("公民身份号码：" + result.getId() + "\n");
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        dialogInfo = builder.setTitle("识别成功")
                .setMessage(sb.toString())
                .setPositiveButton("复制号码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", result.getId());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(RegisterActivity.this, "身份证号已复制到粘贴板", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialogInfo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                // 申请到权限
                selectImage();
            }else {
                Toast.makeText(getApplicationContext(), "没有读取外部存储权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validIdCard() {
        String userid=idcardno.getText().toString();
        if (userid == null || userid.trim().equals("")) {
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("请【选择】或【拍摄】身份证照片！");
            return false;
        }
        try {
            if (!IDCardUtil.IDCardValidate(userid)) {
                tv_tooltip.setVisibility(View.VISIBLE);
                tv_tooltip.setText("无效的身份证号码，请重新【选择】或【拍摄】身份证照片！");
                return false;
            }
        } catch (ParseException e) {
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("无效的身份证号码，请重新【选择】或【拍摄】身份证照片！");
            return false;
        }
        return true;
    }

    private boolean validPhone() {
        String mobile=et_phone_number.getText().toString();
        if (mobile == null || mobile.trim().equals("")) {
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("请输入电话号码！");
            return false;
        }
        if (!ToolsUtil.isMobileNO(mobile)) {
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("请输入正确的电话号码！");
            return false;
        }
        return true;
    }

    private boolean validPasswd() {
        String passwd=et_password.getText().toString();
        String passwdConfirm=et_password_confirm.getText().toString();
        if (passwd == null || passwdConfirm == null || passwd.trim().equals("") || passwdConfirm.trim().equals("")) {
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("请输入密码！");
            return false;
        }
        if (!passwd.equals(passwdConfirm)) {
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("密码不一致，请重新输入！");
            return false;
        }
        if (!PwdCheckUtil.isContainAll(passwd)) {
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("密码必须同时包含数字、大写字母、小写字母，不能包含特殊字符！");
            return false;
        }
        return true;
    }

    private boolean validSmsCode() {
        String smscode=number_yzm.getText().toString();
        if (TextUtils.isEmpty(smscode)){
            tv_tooltip.setVisibility(View.VISIBLE);
            tv_tooltip.setText("请填写短信验证码！");
            return false;
        }
        return true;
    }
}
