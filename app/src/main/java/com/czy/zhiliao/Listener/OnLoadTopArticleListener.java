package com.czy.zhiliao.Listener;

import com.czy.zhiliao.Bean.TopStories;

import java.util.List;

/**
 * Created by ZY on 2016/7/30.
 * 加载顶部Banner文章事件监听
 */
public interface OnLoadTopArticleListener {

    void onSuccess(List<TopStories> topStoriesList);

    void onFailure();

}
