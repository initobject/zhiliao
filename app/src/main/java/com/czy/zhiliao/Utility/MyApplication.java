package com.czy.zhiliao.Utility;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by ZY on 2016/7/26.
 * 自定义Application，用于配置ImageLoaderConfiguration
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageLoader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // 缓存路径
                .diskCache(new UnlimitedDiskCache(cacheDir)).writeDebugLogs()
                // connectTimeout (10 s), readTimeout (10 s)  超时时间
                .imageDownloader(new BaseImageDownloader(context, 30 * 1000, 30 * 1000))
                // 缓存的文件数量
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
    }

}
