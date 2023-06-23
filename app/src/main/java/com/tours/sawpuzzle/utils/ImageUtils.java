package com.tours.sawpuzzle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tours.sawpuzzle.R;
import com.tours.sawpuzzle.data.ImageBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/6/21
 **/
public class ImageUtils {

    /**
     * 将图片切成 , piece *piece
     *
     * @param bitmap
     * @param piece
     * @return
     */
    public static List<ImageBlock> split(Context context, Bitmap bitmap, int piece) {

        List<ImageBlock> images = new ArrayList<ImageBlock>(piece * piece);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Log.e("TAG", "bitmap Width = " + width + " , height = " + height);
        int pieceWidth = Math.min(width, height) / piece;

        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                ImageBlock imageBlock = new ImageBlock();
                imageBlock.setOriginal(i * piece + j);
                int xValue = j * pieceWidth;
                int yValue = i * pieceWidth;

                if (i == piece - 1 && j == piece - 1) {
                    imageBlock.setBitmap(getBitmap(context, R.mipmap.empty));
                } else {
                    imageBlock.setBitmap(Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceWidth));
                }

                images.add(imageBlock);
            }
        }
        return images;
    }

    public static Bitmap getBitmap(Context context, int resourcesId) {
        return BitmapFactory.decodeResource(context.getResources(), resourcesId);
    }
}