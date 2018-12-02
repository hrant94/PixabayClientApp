package com.hrant.pixabayclientapp.presenter.root;

import android.content.Context;

import com.hrant.pixabayclientapp.view.activities.IBaseView;

import javax.inject.Inject;

public class BasePresenter<T extends IBaseView> {

    @Inject
    protected Context mContext;
    protected T mView;

    public void onViewCreated(T view) {
        mView = view;
    }
}