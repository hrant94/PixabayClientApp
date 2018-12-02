package com.hrant.pixabayclientapp.shared.data.apiClients;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PixabayApiClient {

    private AsyncHttpClient mClient;
    private static final String BASE_URL = "https://pixabay.com/api/";
    private static final String KEY = "2314514-cdab59e3f4593733e95b2e786";

    public PixabayApiClient(AsyncHttpClient client) {
        mClient = client;
    }

    public void getImagesList(AsyncHttpResponseHandler responseHandler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("key", KEY);
        mClient.get(BASE_URL, requestParams, responseHandler);
    }
}