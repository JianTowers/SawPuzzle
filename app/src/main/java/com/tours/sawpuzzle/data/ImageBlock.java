package com.tours.sawpuzzle.data;

import android.graphics.Bitmap;

import java.util.Comparator;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/6/21
 **/
public class ImageBlock {

    private Bitmap bitmap;

    private int original;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }
}
