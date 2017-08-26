package com.czy.zhiliao.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.czy.zhiliao.Activity.ArticleContentActivity;
import com.czy.zhiliao.Adapter.BannerPagerAdapter;
import com.czy.zhiliao.Bean.TopStories;
import com.czy.zhiliao.Listener.OnItemClickListener;
import com.czy.zhiliao.R;
import com.czy.zhiliao.Utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 2016/7/30.
 * 图片轮播控件
 */
public class Banner extends FrameLayout {

    private ViewPager viewPager;

    private BannerPagerAdapter adapter;

    private TextView topStoriesTitle;

    private List<ImageView> imageViews;

    private List<TopStories> topStoriesList;

    private List<View> dotList;

    private Handler handler;

    private Runnable runnable;

    private Context context;

    private OnItemClickListener onItemClickListener;

    //当前显示的图片索引
    private int currentItem = 0;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //初始化
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.banner, this, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        topStoriesList = getDefaultBannerList();
        dotList = new ArrayList<>();
        handler = new Handler();
        topStoriesTitle = (TextView) view.findViewById(R.id.articleTitle);
        View dot0 = view.findViewById(R.id.v_dot0);
        View dot1 = view.findViewById(R.id.v_dot1);
        View dot2 = view.findViewById(R.id.v_dot2);
        View dot3 = view.findViewById(R.id.v_dot3);
        View dot4 = view.findViewById(R.id.v_dot4);
        dotList.add(dot0);
        dotList.add(dot1);
        dotList.add(dot2);
        dotList.add(dot3);
        dotList.add(dot4);
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void OnItemClick(int id) {
                id = topStoriesList.get(id).getId();
                Intent intent = new Intent(context, ArticleContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", id);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handler.postDelayed(this, 2500);
            }
        };
        reset();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, layoutParams);
    }

    //重置
    private void reset() {
        imageViews = new ArrayList<>();
        for (int i = 0; i < topStoriesList.size(); i++) {
            if (i > 4) {
                break;
            }
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.default_image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Constant.getImageLoader().displayImage(topStoriesList.get(i).getImage(),
                    imageView, Constant.getDisplayImageOptions());
            imageViews.add(imageView);
            dotList.get(i).setVisibility(View.VISIBLE);
        }
        adapter = new BannerPagerAdapter(imageViews);
        adapter.setOnItemClickListener(onItemClickListener);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());
    }

    public void update(List<TopStories> list) {
        topStoriesList.clear();
        topStoriesList = list;
        reset();
        adapter.notifyDataSetChanged();
        topStoriesTitle.setText(topStoriesList.get(0).getTitle());
    }

    public void startPlay() {
        cancelPlay();
        handler.postDelayed(runnable, 2500);
    }

    public void cancelPlay() {
        handler.removeCallbacks(runnable);
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            TopStories topStories = topStoriesList.get(position);
            topStoriesTitle.setText(topStories.getTitle());
            dotList.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dotList.get(position).setBackgroundResource(R.drawable.dot_selected);
            oldPosition = position;
        }
    }

    //获取默认值
    private List<TopStories> getDefaultBannerList() {
        List<TopStories> topStoriesList = new ArrayList<>();
        TopStories topStories = new TopStories();
        topStories.setTitle("享受阅读的乐趣");
        topStories.setImage("0");
        topStories.setId(0);
        topStoriesList.add(topStories);
        return topStoriesList;
    }

}
