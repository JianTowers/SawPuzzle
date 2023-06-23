package com.tours.sawpuzzle.data;

import android.graphics.Bitmap;

import java.util.Comparator;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/6/21
 **/
public class ImageBlock implements Comparator<ImageBlock> {

    private Bitmap bitmap;

    private int original;
    private int now;

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

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    @Override
    public int compare(ImageBlock t1, ImageBlock t2) {
        if (t1.now > t2.now) {
            return 1;
        } else {
            return 0;
        }
    }
}
