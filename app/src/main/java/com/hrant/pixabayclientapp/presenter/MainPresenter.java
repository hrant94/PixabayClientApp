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
import cz.msebera.android.httpclient.client.HttpResponseException;

public class MainPresenter extends BasePresenter<IMainView> {
    private PixabayApiClient mApiClient;

    @Inject
    public MainPresenter(PixabayApiClient mApiClient) {
        this.mApiClient = mApiClient;
    }

    public void getImageList(int pageNo, int pageItemsCount, String searchString) {
        mApiClient.getImagesList(pageNo, pageItemsCount, searchString, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String responseJsonString = new String(responseBody, "UTF-8");
                    Gson gson = new Gson();
                    PixabayResponseModel response = gson.fromJson(responseJsonString, PixabayResponseModel.class);
                    int pagesCount;

                    //Check pagesCount
                    pagesCount = response.getTotalHits() / pageItemsCount + 1;

                    //Add loading item in list
                    if (pageNo < pagesCount)
                        response.getHits().add(null);

                    if (pageNo == 1) {
                        mView.updateImagesList(response.getHits(), pagesCount);
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
