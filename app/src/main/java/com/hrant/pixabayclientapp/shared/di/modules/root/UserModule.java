package com.hrant.pixabayclientapp.shared.di.modules.root;

import com.hrant.pixabayclientapp.shared.data.apiClients.PixabayApiClient;
import com.hrant.pixabayclientapp.shared.di.scopes.UserScope;
import com.loopj.android.http.AsyncHttpClient;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    @Provides
    @UserScope
    PixabayApiClient providesPixabayApiClient(AsyncHttpClient client) {
        return new PixabayApiClient(client);
    }
}