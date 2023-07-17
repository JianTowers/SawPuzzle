package com.tours.sawpuzzle.ui.select;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tours.sawpuzzle.R;
import com.tours.sawpuzzle.utils.CacheUtils;

import java.util.List;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/6/27
 **/
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ImageItem> {

    private static final String TAG = "ImageAdapter";
    private List<String> datas;


    public SelectAdapter() {
    }

    @NonNull
    @Override
    public ImageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
        return new SelectAdapter.ImageItem(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ImageItem holder, int position) {
        String item = datas.get(position);
        CacheUtils.getInstance().load(holder.image.getContext(), item, holder.image);
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
