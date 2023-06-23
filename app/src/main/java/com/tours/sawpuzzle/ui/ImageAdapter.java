package com.tours.sawpuzzle.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tours.sawpuzzle.R;
import com.tours.sawpuzzle.data.ImageBlock;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Jian
 * @Date: 2023/6/21
 **/
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageItem> {

    private List<ImageBlock> datas;

    @NonNull
    @Override
    public ImageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageItem holder, int position) {
        ImageBlock imageBlock = datas.get(position);
        holder.image.setImageBitmap(imageBlock.getBitmap());
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }


    public void updateData(List<ImageBlock> list){
        this.datas = list;
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
