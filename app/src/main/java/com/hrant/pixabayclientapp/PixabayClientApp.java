package com.hrant.pixabayclientapp;

import android.app.Application;

import com.hrant.pixabayclientapp.shared.di.components.DaggerIMainComponent;
import com.hrant.pixabayclientapp.shared.di.components.IMainComponent;
import com.hrant.pixabayclientapp.shared.di.components.root.DaggerIAppComponent;
import com.hrant.pixabayclientapp.shared.di.components.root.IAppComponent;
import com.hrant.pixabayclientapp.shared.di.modules.root.AppModule;
import com.hrant.pixabayclientapp.shared.di.modules.root.NetModule;
import com.hrant.pixabayclientapp.shared.di.modules.root.MainModule;

public class PixabayClientApp extends Application {
    private IAppComponent mAppComponent;
    private IMainComponent mMainComponent;
    private static PixabayClientApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerIAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();

        mInstance = this;
    }

    public static synchronized PixabayClientApp getInstance() {
        return mInstance;
    }

    public IMainComponent getMainComponent() {
        mMainComponent = DaggerIMainComponent.builder()
                .iAppComponent(mAppComponent)
                .mainModule(new MainModule())
                .build();
        return mMainComponent;
    }

    public void releaseMainComponent() {
        mMainComponent = null;
    }
}
