package com.hrant.pixabayclientapp.shared.di.components.root;

import android.content.Context;

import com.hrant.pixabayclientapp.shared.di.modules.root.AppModule;
import com.hrant.pixabayclientapp.shared.di.modules.root.NetModule;
import com.loopj.android.http.AsyncHttpClient;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface IAppComponent {

    AsyncHttpClient httpClient();

    Context context();

}