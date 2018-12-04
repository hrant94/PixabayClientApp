package com.hrant.pixabayclientapp.view.activities.mainActivity;

import com.hrant.pixabayclientapp.model.PixabayImageModel;
import com.hrant.pixabayclientapp.view.activities.IBaseView;

import java.util.List;

public interface IMainView extends IBaseView {
    void addToImagesList(List<PixabayImageModel> hits);

    void updateImagesList(List<PixabayImageModel> hits, int pagesCount);

    void showEmptyState();

    void hideLoading();
}
