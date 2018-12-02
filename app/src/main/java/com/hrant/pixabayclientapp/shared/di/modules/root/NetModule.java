package com.hrant.pixabayclientapp.shared.di.modules.root;

import com.loopj.android.http.AsyncHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {

    @Provides
    @Singleton
    AsyncHttpClient provideHttpClient() {
        return new AsyncHttpClient();
    }
}