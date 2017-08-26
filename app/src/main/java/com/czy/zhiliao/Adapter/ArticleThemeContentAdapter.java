package com.czy.zhiliao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.czy.zhiliao.Activity.ArticleContentActivity;
import com.czy.zhiliao.Bean.ArticleTheme;
import com.czy.zhiliao.Bean.Stories;
import com.czy.zhiliao.Holder.ArticleListHolder;
import com.czy.zhiliao.Holder.ThemeArticleTopHolder;
import com.czy.zhiliao.Listener.OnItemClickListener;
import com.czy.zhiliao.R;
import com.czy.zhiliao.Utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 特定文章主题下的文章列表Adapter
 * Created by ZY on 2016/7/31.
 */
public class ArticleThemeContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArticleTheme articleTheme;

    private List<Stories> storiesList;

    private LayoutInflater inflater;

    private Context context;

    private OnItemClickListener onItemClickListener;

    private final int TYPE_TOP = 0;

    private final int TYPE_ARTICLE = 1;

    public ArticleThemeContentAdapter(Context context) {
        this.context = context;
        init();
    }

    public void init() {
        inflater = LayoutInflater.from(context);
        storiesList = new ArrayList<>();
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                int id = storiesList.get(position - 1).getId();
                Intent intent = new Intent(context, ArticleContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", id);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        return TYPE_ARTICLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ARTICLE) {
            view = inflater.inflate(R.layout.article_list_item, parent, false);
            return new ArticleListHolder(view);
        } else {
            view = inflater.inflate(R.layout.theme_top, parent, false);
            return new ThemeArticleTopHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ThemeArticleTopHolder themeArticleTopHolder = (ThemeArticleTopHolder) holder;
            String url = "";
            String description = "";
            if (articleTheme != null && articleTheme.getBackground() != null && articleTheme.getDescription() != null) {
                url = articleTheme.getBackground();
                description = articleTheme.getDescription();
            }
            Constant.getImageLoader().displayImage(url,
                    themeArticleTopHolder.themeImage, Constant.getDisplayImageOptions());
            themeArticleTopHolder.themeDescription.setText(description);
            return;
        }
        ArticleListHolder articleListHolder = (ArticleListHolder) holder;
        List<String> images = storiesList.get(position - 1).getImages();
        if (images != null && images.size() > 0) {
            Constant.getImageLoader().displayImage(images.get(0),
                    articleListHolder.articleImage, Constant.getDisplayImageOptions());
            articleListHolder.articleImage.setVisibility(View.VISIBLE);
        } else {
            articleListHolder.articleImage.setVisibility(View.GONE);
        }
        articleListHolder.articleTitle.setText(storiesList.get(position - 1).getTitle());
        articleListHolder.setItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return storiesList.size() + 1;
    }

    public void setData(ArticleTheme articleTheme) {
        storiesList.clear();
        this.articleTheme = articleTheme;
        storiesList.addAll(articleTheme.getStories());
    }

}
