package com.tours.sawpuzzle.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tours.sawpuzzle.R;
import com.tours.sawpuzzle.data.ImageBlock;

import java.util.Collections;
import java.util.List;

/**
 * @Description: TODO
 * @Author: Jian
 * @Date: 2023/6/21
 **/
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageItem> {

    private static final String TAG = "ImageAdapter";
    private List<ImageBlock> datas;
    private int imageSize;
    private int imageSide;

    @NonNull
    @Override
    public ImageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageItem(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ImageItem holder, int position) {
        ImageBlock item = datas.get(position);
        holder.image.setImageBitmap(item.getBitmap());
        holder.image.setOnClickListener(view -> {
            swapImage(item);
        });
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    /**
     * 交换图片
     */
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void swapImage(ImageBlock item) {
        if (item.getOriginal() == imageSize) {
            return;
        }

        //所在图片与空白图片绝对值为1或者为imageSide
        int distance = Math.abs(getEmptyPosition() - getNormalPosition(item));
        if (distance == 1 || distance == imageSide) {
            Collections.swap(datas, getEmptyPosition(), getNormalPosition(item));
            notifyDataSetChanged();
        }
    }

    /**
     * 获取空白图片所在位置
     */
    private int getEmptyPosition() {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getOriginal() == imageSize) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 获取空白图片所在位置
     */
    private int getNormalPosition(ImageBlock item) {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i) == item) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 获取空白块
     */
    private ImageBlock getEmpty() {
        for (ImageBlock image : datas) {
            if (image.getOriginal() == imageSize) {
                return image;
            }
        }
        return null;
    }


    public void updateData(List<ImageBlock> list) {
        this.datas = list;
        this.imageSize = list.size() - 1;
        this.imageSide = (int) Math.sqrt(list.size());
        notifyDataSetChanged();
    }


    public static class ImageItem extends RecyclerView.ViewHolder {
        private ImageView image;

        public ImageView getImage() {
            return image;
        }

        public ImageItem(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
