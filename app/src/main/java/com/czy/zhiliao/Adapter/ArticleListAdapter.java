package com.czy.zhiliao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.czy.zhiliao.Activity.ArticleContentActivity;
import com.czy.zhiliao.Bean.ArticleLatest;
import com.czy.zhiliao.Bean.Stories;
import com.czy.zhiliao.Bean.TopStories;
import com.czy.zhiliao.Holder.ArticleListFooterHolder;
import com.czy.zhiliao.Holder.ArticleListHolder;
import com.czy.zhiliao.Holder.ArticleListTopHolder;
import com.czy.zhiliao.Listener.OnItemClickListener;
import com.czy.zhiliao.Listener.OnLoadTopArticleListener;
import com.czy.zhiliao.Listener.OnSlideToTheBottomListener;
import com.czy.zhiliao.R;
import com.czy.zhiliao.Utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 2016/7/26.
 * 文章列表Adapter
 */
public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Stories> storiesList;

    private LayoutInflater inflater;

    private Context context;

    private final int TYPE_TOP = 0;

    private final int TYPE_ARTICLE = 1;

    private final int TYPE_FOOTER = 2;

    public OnLoadTopArticleListener onLoadTopArticleListener;

    private OnSlideToTheBottomListener onSlideToTheBottomListener;

    private OnItemClickListener onItemClickListener;

    private ArticleListTopHolder articleListTopHolder;

    public ArticleListAdapter(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        inflater = LayoutInflater.from(context);
        storiesList = new ArrayList<>();
        //文章列表点击事件监听
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                int id = storiesList.get(position - 1).getId();
                ArticleContentActivity.startActivity(context, id);
            }
        };
        //加载banner文章事件监听
        onLoadTopArticleListener = new OnLoadTopArticleListener() {
            @Override
            public void onSuccess(List<TopStories> topStoriesList) {
                if (articleListTopHolder != null) {
                    articleListTopHolder.banner.update(topStoriesList);
                    articleListTopHolder.banner.startPlay();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure() {

            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }
        return TYPE_ARTICLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ARTICLE) {
            view = inflater.inflate(R.layout.article_list_item, parent, false);
            return new ArticleListHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = inflater.inflate(R.layout.fooder, parent, false);
            return new ArticleListFooterHolder(view);
        }
        view = inflater.inflate(R.layout.banner_layout, parent, false);
        return new ArticleListTopHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ARTICLE:
                ArticleListHolder articleListHolder = (ArticleListHolder) holder;
                Constant.getImageLoader().displayImage(storiesList.get(position - 1).getImages().get(0),
                        articleListHolder.articleImage, Constant.getDisplayImageOptions());
                articleListHolder.articleTitle.setText(storiesList.get(position - 1).getTitle());
                articleListHolder.setItemClickListener(onItemClickListener);
                break;
            case TYPE_FOOTER:
                //只有当文章数量不为零时才启用事件监听
                if (onSlideToTheBottomListener != null && storiesList != null && storiesList.size() > 0) {
                    onSlideToTheBottomListener.onSlideToTheBottom();
                }
                break;
            case TYPE_TOP:
                articleListTopHolder = (ArticleListTopHolder) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return storiesList.size() + 1;
    }

    //当刷新文章列表时使用
    public void setData(ArticleLatest articleLatest) {
        storiesList.clear();
        storiesList.addAll(articleLatest.getStories());
    }

    //当加载下一页文章内容时使用
    public void addData(List<Stories> storiesList) {
        this.storiesList.addAll(storiesList);
    }

    public void setSlideToTheBottomListener(OnSlideToTheBottomListener slideListener) {
        this.onSlideToTheBottomListener = slideListener;
    }

}