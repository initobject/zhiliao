package com.czy.zhiliao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.czy.zhiliao.R;

import java.util.List;

/**
 * 侧滑菜单文章主题列表Adapter
 * Created by ZY on 2016/7/28.
 */
public class ArticleThemeListAdapter extends BaseAdapter {

    private List<String> themeList;

    private Context context;

    public ArticleThemeListAdapter(Context context, List<String> themeList) {
        this.context = context;
        this.themeList = themeList;
    }

    @Override
    public int getCount() {
        return themeList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.themes_item, parent, false);
            TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
            tv_item.setText(themeList.get(position));
        }
        return convertView;
    }

}
