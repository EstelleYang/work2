package com.lzjs.uappoint.act;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;


/**
 * 场景详细页面
 * wangdq
 * by on 2016年3月10日16:27:52
 */
public class PublishInfoDetailActivity extends Activity {
    private static final String TAG ="PublishInfoDetailActivity" ;
    @ViewInject(R.id.detailInfo)
    private WebView webView;
    @ViewInject(R.id.center_title_text)
    private TextView center_title_text;

    private String sceneId;
    private String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        sceneId=getIntent().getStringExtra("sceneId");
        flag=getIntent().getStringExtra("FLAG");

        setContentView(R.layout.activity_publish_info_detail);
        ViewUtils.inject(this);

        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setBlockNetworkImage(false);
        //Log.i(TAG, "onCreate: " + Contants.GET_SCENE_DETAIL + sceneId);
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                center_title_text.setEllipsize(TextUtils.TruncateAt.END);
                center_title_text.setSingleLine(true);
                center_title_text.setText("攻略详情");
            }

        };
        webView.setWebChromeClient(webChromeClient);
        if ("1".equals(flag)){
            String url=Contants.GET_GUANG+sceneId;
            Log.e("123",Contants.GET_GUANG+sceneId);
            webView.loadUrl(url);
        }else {
            String url1=Contants.GET_SCENE_DETAIL+sceneId;
            Log.e("123",Contants.GET_SCENE_DETAIL+sceneId);
            webView.loadUrl(url1);
        }

    }

    /**
     * 返回首页
     * @param view
     */
    @OnClick(R.id.title1_back)
    public void infoClick(View view){
        onBackPressed();
    }

    /**
     * 监听返回键
     * */
    public void onBackPressed() {
        finish();
    }
}
