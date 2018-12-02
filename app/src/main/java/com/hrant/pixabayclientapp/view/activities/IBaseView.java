package com.hrant.pixabayclientapp.view.activities;

import android.support.annotation.StringRes;

public interface IBaseView {
    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

}
