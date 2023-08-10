package com.tours.sawpuzzle.ui.select;

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
import com.tours.sawpuzzle.utils.cache.CacheUtils;

import java.util.List;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/6/27
 **/
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ImageItem> {

    private static final String TAG = "SelectAdapter";
    private List<String> datas;

    //为-1时，没有选中任何一张图片
    private int selectPosition = -1;

    @NonNull
    @Override
    public ImageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
        return new SelectAdapter.ImageItem(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ImageItem holder, @SuppressLint("RecyclerView") int position) {
        String item = datas.get(position);

        //只刷新选中图标
        if (selectPosition == position) {
            holder.select.setImageResource(R.drawable.ic_select);
        } else {
            holder.select.setImageResource(R.drawable.ic_unselect);
        }
        holder.image.setOnClickListener(view -> {
            int lastPosition = -1;
            if (selectPosition != -1) {
                lastPosition = selectPosition;
            }

            if (selectPosition != position) {
                selectPosition = position;
            } else {
                selectPosition = -1;
            }

            if (lastPosition != -1) {
                notifyItemChanged(lastPosition, item);
            }
            if (selectPosition != -1) {
                notifyItemChanged(selectPosition, item);
            }
        });

        holder.image.setTag(item);
        CacheUtils.getInstance().load(item,it->{
            holder.image.post(()->{
                if (it == null || holder.image.getTag() != it.getKey()){
                    holder.image.setImageResource(R.mipmap.test);
                }else {
                    holder.image.setImageBitmap(it.getBitmap());
                }
            });

            return false;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ImageItem holder, @SuppressLint("RecyclerView") int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        //只刷新选中图标
        if (selectPosition == position) {
            holder.select.setImageResource(R.drawable.ic_select);
        } else {
            holder.select.setImageResource(R.drawable.ic_unselect);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    public void updateData(List<String> list) {
        this.datas = list;
        notifyDataSetChanged();
    }

    public String getImage(){
        if (selectPosition == -1){
            return null;
        }
        return datas.get(selectPosition);
    }


    public static class ImageItem extends RecyclerView.ViewHolder {
        private ImageView image;
        private ImageView select;

        public ImageItem(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            select = itemView.findViewById(R.id.select);
        }
    }
}
