package com.hrant.pixabayclientapp.presenter;

import com.google.gson.Gson;
import com.hrant.pixabayclientapp.model.PixabayResponseModel;
import com.hrant.pixabayclientapp.presenter.root.BasePresenter;
import com.hrant.pixabayclientapp.shared.configurations.Constants;
import com.hrant.pixabayclientapp.shared.data.apiClients.PixabayApiClient;
import com.hrant.pixabayclientapp.view.activities.IMainView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void getImageList() {
        mApiClient.getImagesList(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String responseJsonString = new String(responseBody, "UTF-8");
                    Gson gson = new Gson();
                    PixabayResponseModel response = gson.fromJson(responseJsonString, PixabayResponseModel.class);
                    mView.setImagesList(response.getHits());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mView.onError(Constants.DEFAULT_ERROR_MESSAGE);
                }
                mView.hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (((HttpResponseException) error).getStatusCode() != 429)
                    mView.onError(error.getMessage());
                mView.hideLoading();
            }
        });
    }
}
