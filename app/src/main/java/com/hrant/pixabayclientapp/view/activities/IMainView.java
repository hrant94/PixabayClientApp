package com.hrant.pixabayclientapp.view.activities;

import com.hrant.pixabayclientapp.model.PixabayImageModel;

import java.util.List;

public interface IMainView extends IBaseView {
    void setImagesList(List<PixabayImageModel> hits);

    void hideLoading();
}
