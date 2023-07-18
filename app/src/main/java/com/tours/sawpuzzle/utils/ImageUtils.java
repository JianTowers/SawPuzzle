package com.tours.sawpuzzle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tours.sawpuzzle.data.ImageBlock;

import java.util.ArrayList;
import java.util.Collections;
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
    public static List<ImageBlock> split(Bitmap bitmap, int piece) {
        List<ImageBlock> images = new ArrayList<ImageBlock>(piece * piece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = Math.min(width, height) / piece;
        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                ImageBlock imageBlock = new ImageBlock();
                imageBlock.setOriginal(i * piece + j);
                int xValue = j * pieceWidth;
                int yValue = i * pieceWidth;
                if (i == piece - 1 && j == piece - 1) {
                    imageBlock.setBitmap(null);
                } else {
                    imageBlock.setBitmap(Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceWidth));
                }
                images.add(imageBlock);
            }
        }

        //随机打乱图片
        Collections.shuffle(images);
        return images;
    }

    /**
     * 获取图片正方形
     */
    public static Bitmap getSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = Math.min(width, height);
        return Bitmap.createBitmap(bitmap, 0, 0, pieceWidth, pieceWidth);
    }

    public static Bitmap getBitmap(Context context, int resourcesId) {
        return BitmapFactory.decodeResource(context.getResources(), resourcesId);
    }

    public static Bitmap getBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }
}