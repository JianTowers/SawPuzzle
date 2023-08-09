package com.tours.sawpuzzle.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * @Description: TODO
 * @Author: Jian
 * @Date: 2023/7/18
 **/
@SuppressLint("AppCompatCustomView")
public class CropImageView extends ImageView {

    public CropImageView(Context context) {
        super(context);
        init();
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private float widthSize;//布局宽度
    private float heightSize;//布局高度
    private Paint bitmapPaint;//图片画笔
    private Paint shadowPaint;//阴影画笔
    private Paint linePaint;//线条画笔

    private void init() {
        bitmapPaint = new Paint();
        bitmapPaint.setStrokeWidth(0);

        shadowPaint = new Paint();
        shadowPaint.setColor(Color.parseColor("#57FF9800"));
        shadowPaint.setStrokeWidth(4);
        shadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#FF9800"));
        linePaint.setStrokeWidth(4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
    }

    private Bitmap originalBitmap;//要裁剪的位图
    private float bitmapWidth;//位图原始宽度
    private float bimapHeight;//位图原始高度
    private float proportionWidth;//比例：宽  如裁图比例3:4，此处传3
    private float proportionHeight;//比例：高  如裁图比例3:4，此处传4

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (originalBitmap == null) {
            return;
        }

    }



    // =========================================
    // ================  外部方法 ===============
    // =========================================
    public void setBitmap(String path){

    }
}
