package com.tours.sawpuzzle.utils.cache;

import android.graphics.Bitmap;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/7/27
 **/
public class CacheData {
    private String key;
    private Bitmap bitmap;

    public CacheData(){

    }

    public CacheData(String key, Bitmap bitmap) {
        this.key = key;
        this.bitmap = bitmap;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
