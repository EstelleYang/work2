package com.lzjs.uappoint.act;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.CollecteArticle;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.StringUtils;

public class CollectedArticleDetailActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TextView textView;
    private CollecteArticle collecteArticle;
    private WebView webView;
    private static final String TAG = "CollectedArticleDetailA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_article_detail);
        toolbar = (Toolbar)findViewById(R.id.id_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView = (WebView)findViewById(R.id.article_content);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        textView = (TextView)findViewById(R.id.toolbar_name);
        //textView.setText("详情");
        Intent intent = getIntent();
        collecteArticle = (CollecteArticle) intent.getSerializableExtra("article");
        textView.setText(collecteArticle.getTital());
        textView.setTextSize(15);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        Log.d(TAG, "onCreate: "+"webView will run");

       // if ("1".equals(flag)){
        String url = collecteArticle.getUrl();
            if (StringUtils.isEmpty(collecteArticle.getUrl())) {
                webView.loadUrl("https://baidu.com");
                Log.d(TAG, "onCreate: "+"webView is running");
                //webView.loadUrl(Contants.GET_GUANG+collecteArticle.getId());
            } else {
                webView.loadUrl(url);
            }

        //}else {
            /*if (StringUtils.isEmpty(articleUrl)) {
                webView.loadUrl(Contants.GET_SCENE_DETAIL+articleId);
            } else {
                webView.loadUrl(articleUrl);
            }*/
      //  }
    }
}