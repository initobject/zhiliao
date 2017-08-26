package com.czy.zhiliao.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.czy.zhiliao.R;

/**
 * Created by ZY on 2016/8/1.
 * 主题文章顶部图片
 */
public class ThemeArticleTopHolder extends RecyclerView.ViewHolder {

    public ImageView themeImage;

    public TextView themeDescription;

    public ThemeArticleTopHolder(View itemView) {
        super(itemView);
        themeImage = (ImageView) itemView.findViewById(R.id.themeImage);
        themeDescription = (TextView) itemView.findViewById(R.id.themeDescription);
    }

}
