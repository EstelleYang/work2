package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.StringUtils;

/**
 * 文章详情
 */
public class ArticleDetailActivity extends BaseActivity {

    @ViewInject(R.id.wv_news_detail)
    private WebView webView;
    /*@ViewInject(R.id.title)
    private TextView center_title_text;*/
    @ViewInject(R.id.tv_support)
    private  TextView tv_support;
    @ViewInject(R.id.tv_add_fav)
    private  TextView tv_add_fav;
    @ViewInject(R.id.tv_comment)
    private  TextView tv_comment;
    private String articleId;
    private String articleUrl;
    private String flag;
    private boolean clickflag=false;
    private Toolbar mToolbar;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

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
        articleId=getIntent().getStringExtra("articeId");
        articleUrl=getIntent().getStringExtra("url");
        flag=getIntent().getStringExtra("FLAG");
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setBlockNetworkImage(false);

        //webSettings.setDisplayZoomControls(false);
        //webSettings.setAllowFileAccess(true); // 允许访问文件
        //webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        //webSettings.setSupportZoom(true); // 支持缩放

        /*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity > 240 ) {
            webSettings.setDefaultFontSize(40); //可以取1-72之间的任意值，默认16
        }

        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }*/


        tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickflag){
                    view.setSelected(false);
                    clickflag=false;
                }else{
                    view.setSelected(true);
                    clickflag=true;
                }

            }
        });
        tv_add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickflag){
                    view.setSelected(false);
                    clickflag=false;
                }else{
                    view.setSelected(true);
                    clickflag=true;
                }
            }
        });
        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BodyAuthenticateActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putBoolean("isImgShow", false);
                mbundle.putString("titleShow", "评论");
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });
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
        if ("1".equals(flag)){
            if (StringUtils.isEmpty(articleUrl)) {
                webView.loadUrl(Contants.GET_GUANG+articleId);
            } else {
                webView.loadUrl(articleUrl);
            }

        }else {
            if (StringUtils.isEmpty(articleUrl)) {
                webView.loadUrl(Contants.GET_SCENE_DETAIL+articleId);
            } else {
                webView.loadUrl(articleUrl);
            }
        }

    }
    private  void initView(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    public void destroyWebView() {
        //mWebContainer.removeAllViews();
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
