package com.czy.zhiliao.Net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.czy.zhiliao.Bean.ArticleBefore;
import com.czy.zhiliao.Bean.ArticleContent;
import com.czy.zhiliao.Bean.ArticleLatest;
import com.czy.zhiliao.Bean.ArticleTheme;
import com.czy.zhiliao.Bean.Others;
import com.czy.zhiliao.Bean.Theme;
import com.czy.zhiliao.Listener.OnLoadDataListener;
import com.czy.zhiliao.Listener.OnLoadThemesListener;
import com.czy.zhiliao.Utility.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by ZY on 2016/7/26.
 * 所有的网络操作
 */
public class HttpUtil {

    private AsyncHttpClient client;

    private static HttpUtil httpUtil;

    private HttpUtil() {
        client = new AsyncHttpClient();
        client.setConnectTimeout(1000 * 30);
        client.setTimeout(1000 * 30);
    }

    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    //判断当前网络状态
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //获取最新文章
    public void getLatestArticleList(final OnLoadDataListener listener) {
        client.get(Constant.LatestArticleUrl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArticleLatest articleLatest = JSON.parseObject(responseString, ArticleLatest.class);
                if (articleLatest != null && articleLatest.getStories() != null && articleLatest.getStories().size() > 0
                        && articleLatest.getTop_stories() != null && articleLatest.getTop_stories().size() > 0) {
                    if (listener != null) {
                        listener.onSuccess(articleLatest);
                        return;
                    }
                }
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    //获取过去文章
    public void getBeforeArticleList(final String date, final OnLoadDataListener listener) {
        client.get(Constant.BeforeArticleUrl + date, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArticleBefore articleBefore = JSON.parseObject(responseString, ArticleBefore.class);
                if (articleBefore != null && articleBefore.getStories() != null && articleBefore.getStories().size() > 0) {
                    if (listener != null) {
                        listener.onSuccess(articleBefore);
                        return;
                    }
                }
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    //获取文章内容
    public void getArticleContent(int id, final OnLoadDataListener listener) {
        client.get(Constant.ArticleContentUrl + id, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArticleContent content = JSON.parseObject(responseString, ArticleContent.class);
                if (content != null && !TextUtils.isEmpty(content.getBody())) {
                    String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/src.css\" type=\"text/css\">";
                    String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
                    html = html.replace("<div class=\"img-place-holder\">", "");
                    content.setBody(html);
                    if (listener != null) {
                        listener.onSuccess(content);
                        return;
                    }
                }
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    //获取文章主题
    public void getThemes(final OnLoadThemesListener listener) {
        client.get(Constant.ArticleThemesUrl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Theme theme = JSON.parseObject(responseString, Theme.class);
                List<Others> othersList = theme.getOthers();
                if (othersList != null && othersList.size() > 0) {
                    if (othersList.size() > 0) {
                        if (listener != null) {
                            listener.onSuccess(othersList);
                            return;
                        }
                    }
                }
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    //获取指定主题下的文章
    public void getArticleListByTheme(int id, final OnLoadDataListener listener) {
        client.get(Constant.ThemeContentUrl + id, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArticleTheme articleTheme = JSON.parseObject(responseString, ArticleTheme.class);
                if (articleTheme != null && articleTheme.getStories() != null && articleTheme.getStories().size() > 0) {
                    if (listener != null) {
                        listener.onSuccess(articleTheme);
                        return;
                    }
                }
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

}
