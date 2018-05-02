package com.lzjs.uappoint.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;

/**
 * 会议/通告详情
 */
public class MeetingDetailActivity extends BaseActivity {

    @ViewInject(R.id.wv_meeting_detail)
    private WebView webView;
    private String meetingId;
    private String url;
    private FrameLayout mWebContainer;
    //private String type;
    private Toolbar mToolbar;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        tv_title=(TextView)findViewById(R.id.toolbar_name);
        mToolbar.setTitle("");
        tv_title.setText("详情");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewUtils.inject(this);
        initView();
        meetingId=getIntent().getStringExtra("meetingId");
        //type=getIntent().getStringExtra("type");
        url =getIntent().getStringExtra("url");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //webSettings.setLoadWithOverviewMode(true); 
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setBlockNetworkImage(false);
        //webSettings.setAllowFileAccess(true);

        //Log.i(TAG, "onCreate: " + Contants.GET_SCENE_DETAIL + sceneId);
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //center_title_text.setEllipsize(TextUtils.TruncateAt.END);
                //center_title_text.setSingleLine(true);
                //center_title_text.setText("详情");
            }

        };
        webView.setWebChromeClient(webChromeClient);
        //String url= Contants.GET_MEETING_TYPE+meetingId;
        //Log.e("123",Contants.GET_MEETING_TYPE+meetingId);
        webView.loadUrl(url);

    }
    private  void initView(){
        mWebContainer = (FrameLayout) findViewById(R.id.wv_container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    public void destroyWebView() {
        mWebContainer.removeAllViews();
        if(webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webView.freeMemory();
            webView.pauseTimers();
            webView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }
}
