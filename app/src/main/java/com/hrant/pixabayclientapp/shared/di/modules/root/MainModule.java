package com.hrant.pixabayclientapp.shared.di.modules.root;

import com.hrant.pixabayclientapp.shared.data.apiClients.PixabayApiClient;
import com.hrant.pixabayclientapp.shared.di.scopes.MainScope;
import com.loopj.android.http.AsyncHttpClient;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    @MainScope
    PixabayApiClient providesPixabayApiClient(AsyncHttpClient client) {
        return new PixabayApiClient(client);
    }
}