package com.czy.zhiliao.Listener;

import com.czy.zhiliao.Bean.Others;

import java.util.List;

/**
 * Created by ZY on 2016/7/28.
 * 加载文章主题列表事件监听
 */
public interface OnLoadThemesListener {

    void onSuccess(List<Others> othersList);

    void onFailure();

}
