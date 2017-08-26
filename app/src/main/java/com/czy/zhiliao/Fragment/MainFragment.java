package com.czy.zhiliao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.czy.zhiliao.Adapter.ArticleListAdapter;
import com.czy.zhiliao.Bean.ArticleBefore;
import com.czy.zhiliao.Bean.ArticleLatest;
import com.czy.zhiliao.Bean.TopStories;
import com.czy.zhiliao.Listener.OnLoadDataListener;
import com.czy.zhiliao.Listener.OnSlideToTheBottomListener;
import com.czy.zhiliao.Net.HttpUtil;
import com.czy.zhiliao.R;

import java.util.List;

/**
 * Created by ZY on 2016/7/28.
 * 主页文章
 */
public class MainFragment extends BaseFragment {

    //文章列表
    private RecyclerView recyclerView;

    private ArticleListAdapter adapter;

    private OnLoadDataListener latestListener;

    private OnLoadDataListener beforeListener;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.articleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    protected void initData() {
        adapter = new ArticleListAdapter(activity);
        recyclerView.setAdapter(adapter);
        //加载最新文章事件监听
        latestListener = new OnLoadDataListener() {
            @Override
            public void onSuccess(Object content) {
                ArticleLatest articleLatest = (ArticleLatest) content;
                adapter.setData(articleLatest);
                getRootActivity().setDate(articleLatest.getDate());
                List<TopStories> topStoriesList = articleLatest.getTop_stories();
                if (adapter.onLoadTopArticleListener != null) {
                    adapter.onLoadTopArticleListener.onSuccess(topStoriesList);
                }
                stopRefresh();
                hint(recyclerView, "已经是最新文章啦");
                //加载最新文章成功后在后台再加载下一页
                getBeforeArticleList();
            }

            @Override
            public void onFailure() {
                if (activity != null) {
                    hint(recyclerView, "好奇怪，文章加载不来");
                }
                stopRefresh();
            }
        };
        //加载过去文章事件监听
        beforeListener = new OnLoadDataListener() {
            @Override
            public void onSuccess(Object content) {
                ArticleBefore articleBefore = (ArticleBefore) content;
                adapter.addData(articleBefore.getStories());
                adapter.notifyDataSetChanged();
                getRootActivity().setDate(articleBefore.getDate());
            }

            @Override
            public void onFailure() {
                if (activity != null) {
                    hint(recyclerView, "好奇怪，文章加载不来");
                }
            }
        };
        //滑动到底部事件监听
        OnSlideToTheBottomListener slideListener = new OnSlideToTheBottomListener() {
            @Override
            public void onSlideToTheBottom() {
                getBeforeArticleList();
            }
        };
        adapter.setSlideToTheBottomListener(slideListener);
        getLatestArticleList();
    }

    public void getLatestArticleList() {
        if (!HttpUtil.getInstance().isNetworkConnected(activity)) {
            hint(recyclerView, "似乎没有连接网络？");
            stopRefresh();
            return;
        }
        HttpUtil.getInstance().getLatestArticleList(latestListener);
    }

    public void getBeforeArticleList() {
        if (!HttpUtil.getInstance().isNetworkConnected(activity)) {
            hint(recyclerView, "似乎没有连接网络？");
            return;
        }
        HttpUtil.getInstance().getBeforeArticleList(getRootActivity().getDate(), beforeListener);
    }

    public void stopRefresh() {
        if (getRootActivity() != null) {
            getRootActivity().setRefresh(false);
        }
    }

    public void smoothScrollToFirst() {
        recyclerView.smoothScrollToPosition(0);
    }

    public int getFirstVisibleItemPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return layoutManager.findFirstVisibleItemPosition();
    }

}
