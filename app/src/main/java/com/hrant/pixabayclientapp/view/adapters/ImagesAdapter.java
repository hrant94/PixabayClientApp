package com.hrant.pixabayclientapp.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hrant.pixabayclientapp.view.activities.imagePreviewActivity.ImagePreviewActivity;
import com.hrant.pixabayclientapp.R;
import com.hrant.pixabayclientapp.model.PixabayImageModel;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private Context mContext;
    private List<PixabayImageModel> mList;
    public static final int IMAGE_ITEM = 1;
    public static final int LOADING = 2;

    public ImagesAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == IMAGE_ITEM) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_grid_item, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new ImageViewHolder(view);
        } else {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_loading_item, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == IMAGE_ITEM)
            onBindImageViewHolder((ImageViewHolder) viewHolder, i);
    }

    private void onBindImageViewHolder(@NonNull ImageViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(mList.get(i).getWebformatURL().replace("_640", "_340"))
                .apply(new RequestOptions()
                        .placeholder(R.color.grey))
                .into((ImageView) viewHolder.itemView);

        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ImagePreviewActivity.class);
            intent.putExtra("imageUrl", mList.get(i).getLargeImageURL());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) == null)
            return LOADING;
        else
            return IMAGE_ITEM;
    }

    public void addImagesList(List<PixabayImageModel> images) {
        if (mList.get(mList.size() - 1) == null) {
            notifyItemRemoved(mList.size()-1);
            mList.remove(mList.size() - 1);
        }
        mList.addAll(images);
        notifyItemRangeInserted(mList.size() - images.size(), images.size()-1);
    }

    public void updateImagesList(List<PixabayImageModel> images) {
        mList = images;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ImageViewHolder extends ViewHolder {

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class LoadingViewHolder extends ViewHolder {

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
