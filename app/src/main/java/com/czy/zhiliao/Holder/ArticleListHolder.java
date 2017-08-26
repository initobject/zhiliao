package com.czy.zhiliao.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.czy.zhiliao.Listener.OnItemClickListener;
import com.czy.zhiliao.R;

/**
 * Created by ZY on 2016/7/26.
 * 文章列表
 */
public class ArticleListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView articleTitle;

    public ImageView articleImage;

    public OnItemClickListener itemClickListener;

    public ArticleListHolder(View itemView) {
        super(itemView);
        articleTitle = (TextView) itemView.findViewById(R.id.article_title);
        articleImage = (ImageView) itemView.findViewById(R.id.article_image);
        articleTitle.setOnClickListener(this);
        articleImage.setOnClickListener(this);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.OnItemClick(getAdapterPosition());
        }
    }

}
