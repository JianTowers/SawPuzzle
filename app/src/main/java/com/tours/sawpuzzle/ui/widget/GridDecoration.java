package com.tours.sawpuzzle.ui.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/7/17
 **/
public class GridDecoration extends RecyclerView.ItemDecoration {

    private int length = 0;

    public GridDecoration(int length) {
        this.length = length;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(length, length, length, length);
    }
}
