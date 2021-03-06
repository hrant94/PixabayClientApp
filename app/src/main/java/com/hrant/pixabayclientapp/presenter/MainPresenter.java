package com.hrant.pixabayclientapp.presenter;

import com.google.gson.Gson;
import com.hrant.pixabayclientapp.model.PixabayResponseModel;
import com.hrant.pixabayclientapp.presenter.root.BasePresenter;
import com.hrant.pixabayclientapp.shared.configurations.Constants;
import com.hrant.pixabayclientapp.shared.data.apiClients.PixabayApiClient;
import com.hrant.pixabayclientapp.view.activities.mainActivity.IMainView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import cz.msebera.android.httpclient.Header;

public class MainPresenter extends BasePresenter<IMainView> {
    private PixabayApiClient mApiClient;

    @Inject
    MainPresenter(PixabayApiClient mApiClient) {
        this.mApiClient = mApiClient;
    }

    public void getImageList(int pageNo, int currentItemsCount, String searchString) {
        mApiClient.getImagesList(pageNo, searchString, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String responseJsonString = new String(responseBody, "UTF-8");
                    Gson gson = new Gson();
                    PixabayResponseModel response = gson.fromJson(responseJsonString, PixabayResponseModel.class);

                    //Add loading item in list
                    if (currentItemsCount + response.getHits().size() < response.getTotalHits())
                        response.getHits().add(null);

                    if (pageNo == 1) {
                        if (response.getHits().size() > 0)
                            mView.updateImagesList(response.getHits(), response.getTotalHits());
                        else
                            mView.showEmptyState();
                        mView.hideLoading();
                    } else
                        mView.addToImagesList(response.getHits());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mView.onError(Constants.DEFAULT_ERROR_MESSAGE);
                    mView.hideLoading();
                }
                mView.hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode != 429)
                    mView.onError(error.getMessage());
                mView.hideLoading();
            }
        });
    }

    public void cancelRequests() {
        mApiClient.cancelRequests();
    }
}
