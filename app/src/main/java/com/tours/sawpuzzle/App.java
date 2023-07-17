package com.tours.sawpuzzle;

import android.app.Application;

import com.tours.sawpuzzle.utils.CacheUtils;

/**
 * @Description: TODO
 * @Author: Jian
 * @Date: 2023/6/27
 **/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CacheUtils.getInstance().init(this);
    }
}
