package com.czy.zhiliao.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.czy.zhiliao.Adapter.ArticleThemeListAdapter;
import com.czy.zhiliao.Bean.Others;
import com.czy.zhiliao.Bean.Theme;
import com.czy.zhiliao.Fragment.BaseFragment;
import com.czy.zhiliao.Fragment.MainFragment;
import com.czy.zhiliao.Fragment.ThemeFragment;
import com.czy.zhiliao.Listener.OnLoadThemesListener;
import com.czy.zhiliao.Net.HttpUtil;
import com.czy.zhiliao.R;
import com.czy.zhiliao.Utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： chenZY
 * 时间： 2017/8/26 16:01
 * 描述： https://github.com/leavesC
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private SwipeRefreshLayout refreshLayout;

    //文章的发布日期
    private String date;

    //用来实现再按一次退出程序的效果
    private boolean isExit;

    public boolean isHomepage;

    private String fragmentTag;

    private ListView themesListView;

    private ArticleThemeListAdapter adapter;

    private List<Others> themeList;

    private List<String> themeStringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, StartActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        initView();
        initTheme();
        fragmentTag = MainActivity.class.getSimpleName();
        getTransition().add(R.id.fl_content, new MainFragment(), fragmentTag).commit();
        isHomepage = true;
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("享受阅读的乐趣");
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        themesListView = (ListView) findViewById(R.id.themeList);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        //隐藏滑动条
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
        drawerToggle.syncState();
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isHomepage) {
                    MainFragment mainFragment = (MainFragment) getFragmentByTag(fragmentTag);
                    mainFragment.getLatestArticleList();
                } else {
                    ThemeFragment themeFragment = (ThemeFragment) getFragmentByTag(fragmentTag);
                    themeFragment.refreshData();
                }
            }
        });
    }

    private void initTheme() {
        Theme theme = JSON.parseObject(Constant.DefaultThemesJson, Theme.class);
        themeList = theme.getOthers();
        themeStringList = new ArrayList<>();
        for (int i = 0; i < themeList.size(); i++) {
            themeStringList.add(themeList.get(i).getName());
        }
        adapter = new ArticleThemeListAdapter(this, themeStringList);
        themesListView.setAdapter(adapter);
        //获取文章主题事件监听
        OnLoadThemesListener listener = new OnLoadThemesListener() {
            @Override
            public void onSuccess(List<Others> othersList) {
                themeList.clear();
                themeList.addAll(othersList);
                themeStringList.clear();
                for (int i = 0; i < othersList.size(); i++) {
                    themeStringList.add(othersList.get(i).getName());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        };
        themesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = themeList.get(position).getName();
                if (!fragmentTag.equals(title)) {
                    getThemeFragment(themeList.get(position).getId(), title);
                }
                closeDrawerLayout();
            }
        });
        HttpUtil.getInstance().getThemes(listener);
    }

    //获取主页
    private void getHomepage() {
        ThemeFragment themeFragment = (ThemeFragment) getFragmentByTag(fragmentTag);
        fragmentTag = MainActivity.class.getSimpleName();
        MainFragment mainFragment = (MainFragment) getFragmentByTag(fragmentTag);
        FragmentTransaction transition = getTransition();
        transition.hide(themeFragment);
        if (mainFragment == null) {
            transition.add(R.id.fl_content, new MainFragment(), MainActivity.class.getSimpleName()).commit();
        } else {
            transition.show(mainFragment).commit();
        }
        isHomepage = true;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("享受阅读的乐趣");
        }
    }

    //获取主题Fragment
    private void getThemeFragment(int id, String title) {
        ThemeFragment toFragment = (ThemeFragment) getFragmentByTag(title);
        BaseFragment nowFragment;
        if (isHomepage) {
            nowFragment = (MainFragment) getFragmentByTag(fragmentTag);
        } else {
            nowFragment = (ThemeFragment) getFragmentByTag(fragmentTag);
        }
        FragmentTransaction transition = getTransition();
        transition.hide(nowFragment);
        if (toFragment == null) {
            ThemeFragment themeFragment = new ThemeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("ID", id);
            bundle.putString("Title", title);
            themeFragment.setArguments(bundle);
            transition.add(R.id.fl_content, themeFragment, title).commit();
        } else {
            transition.show(toFragment).commit();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
        }
        fragmentTag = title;
        isHomepage = false;
        setRefresh(false);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawerLayout();
            return;
        }
        if (!isHomepage) {
            ThemeFragment themeFragment = (ThemeFragment) getFragmentByTag(fragmentTag);
            if (themeFragment.getFirstVisibleItemPosition() != 0) {
                themeFragment.smoothScrollToFirst();
                return;
            }
            getHomepage();
            return;
        } else {
            MainFragment mainFragment = (MainFragment) getFragmentByTag(MainActivity.class.getSimpleName());
            if (mainFragment.getFirstVisibleItemPosition() != 0) {
                mainFragment.smoothScrollToFirst();
                return;
            }
        }
        if (isExit) {
            refreshLayout.setRefreshing(false);
            super.onBackPressed();
        } else {
            hint();
        }
    }

    private void hint() {
        Snackbar snackbar = Snackbar.make(refreshLayout, "再按一次退出", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor("#0099CC"));
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                isExit = false;
            }

            @Override
            public void onShown(Snackbar snackbar) {
                isExit = true;
            }
        }).show();
    }

    private FragmentTransaction getTransition() {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        return transition;
    }

    private Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRefresh(boolean flag) {
        refreshLayout.setRefreshing(flag);
    }

    public void closeDrawerLayout() {
        this.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

}
