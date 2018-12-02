package com.hrant.pixabayclientapp.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hrant.pixabayclientapp.R;
import com.hrant.pixabayclientapp.model.PixabayImageModel;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder>{
    private Context mContext;
    private List<PixabayImageModel> mList;

    public ImagesAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_grid_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(mList.get(i).getPreviewURL())
                .into((ImageView) viewHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addImagesList(List<PixabayImageModel> images) {
        mList.addAll(images);
        notifyItemRangeInserted(mList.size() - images.size(), images.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
