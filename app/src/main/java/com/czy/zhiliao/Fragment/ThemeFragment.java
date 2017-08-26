package com.czy.zhiliao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.czy.zhiliao.Adapter.ArticleThemeContentAdapter;
import com.czy.zhiliao.Bean.ArticleTheme;
import com.czy.zhiliao.Listener.OnLoadDataListener;
import com.czy.zhiliao.Net.HttpUtil;
import com.czy.zhiliao.R;

/**
 * Created by ZY on 2016/7/31.
 * 主题文章
 */
public class ThemeFragment extends BaseFragment {

    //文章列表
    private RecyclerView recyclerView;

    private ArticleThemeContentAdapter adapter;

    private OnLoadDataListener onLoadDataListener;

    private int id;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.articleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        String title = "享受阅读的乐趣";
        if (bundle != null) {
            id = bundle.getInt("ID", 1);
            title = bundle.getString("Title");
        }
        if (getRootActivity().getSupportActionBar() != null) {
            getRootActivity().getSupportActionBar().setTitle(title);
        }
        adapter = new ArticleThemeContentAdapter(activity);
        recyclerView.setAdapter(adapter);
        onLoadDataListener = new OnLoadDataListener() {
            @Override
            public void onSuccess(Object content) {
                ArticleTheme articleTheme = (ArticleTheme) content;
                adapter.setData(articleTheme);
                adapter.notifyDataSetChanged();
                stopRefresh();
                hint(recyclerView, "已经是最新文章啦");
            }

            @Override
            public void onFailure() {
                if (activity != null) {
                    hint(recyclerView, "好奇怪，文章加载不来");
                }
                stopRefresh();
            }
        };
        refreshData();
    }

    public void stopRefresh() {
        if (getRootActivity() != null) {
            getRootActivity().setRefresh(false);
        }
    }

    public void refreshData() {
        if (!HttpUtil.getInstance().isNetworkConnected(activity)) {
            stopRefresh();
            hint(recyclerView, "似乎没有连接网络？");
            return;
        }
        HttpUtil.getInstance().getArticleListByTheme(id, onLoadDataListener);
    }

    public void smoothScrollToFirst() {
        recyclerView.smoothScrollToPosition(0);
    }

    public int getFirstVisibleItemPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return layoutManager.findFirstVisibleItemPosition();
    }

}
