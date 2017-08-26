package com.czy.zhiliao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.czy.zhiliao.Bean.ArticleContent;
import com.czy.zhiliao.Listener.OnLoadDataListener;
import com.czy.zhiliao.Net.HttpUtil;
import com.czy.zhiliao.R;
import com.czy.zhiliao.Utility.Constant;

/**
 * Created by ZY on 2016/7/26.
 * 利用WebView加载文章内容，CSS文件已存在本地
 */
public class ArticleContentActivity extends AppCompatActivity {

    private OnLoadDataListener onLoadDataListener;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        init();
        HttpUtil.getInstance().getArticleContent(getIntent().getIntExtra("ID", 0), onLoadDataListener);
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        webView.getSettings().setAppCacheEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("享受阅读的乐趣");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onLoadDataListener = new OnLoadDataListener() {
            @Override
            public void onSuccess(Object object) {
                ArticleContent content = (ArticleContent) object;
                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
                collapsingToolbarLayout.setTitle(content.getTitle());
                webView.loadDataWithBaseURL("x-data://base", content.getBody(), "text/html", "UTF-8", null);
                ImageView imageView = (ImageView) findViewById(R.id.headImage);
                Constant.getImageLoader().displayImage(content.getImage(), imageView, Constant.getDisplayImageOptions());
            }

            @Override
            public void onFailure() {
                Snackbar snackbar;
                if (!HttpUtil.getInstance().isNetworkConnected(ArticleContentActivity.this)) {
                    snackbar = Snackbar.make(webView, "似乎没有连接网络?", Snackbar.LENGTH_SHORT);
                } else {
                    snackbar = Snackbar.make(webView, "好奇怪，文章加载不出来", Snackbar.LENGTH_SHORT);
                }
                snackbar.getView().setBackgroundColor(Color.parseColor("#0099CC"));
                snackbar.show();
            }
        };
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, ArticleContentActivity.class);
        intent.putExtra("ID", id);
        context.startActivity(intent);
    }

}
