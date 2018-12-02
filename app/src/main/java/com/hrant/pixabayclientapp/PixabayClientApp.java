package com.hrant.pixabayclientapp;

import android.app.Application;

import com.hrant.pixabayclientapp.shared.di.components.DaggerIUserComponent;
import com.hrant.pixabayclientapp.shared.di.components.IUserComponent;
import com.hrant.pixabayclientapp.shared.di.components.root.DaggerIAppComponent;
import com.hrant.pixabayclientapp.shared.di.components.root.IAppComponent;
import com.hrant.pixabayclientapp.shared.di.modules.root.AppModule;
import com.hrant.pixabayclientapp.shared.di.modules.root.NetModule;
import com.hrant.pixabayclientapp.shared.di.modules.root.UserModule;

public class PixabayClientApp extends Application {
    private IAppComponent mAppComponent;
    private IUserComponent mUserComponent;
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

    public IUserComponent getUserComponent() {
        mUserComponent = DaggerIUserComponent.builder()
                .iAppComponent(mAppComponent)
                .userModule(new UserModule())
                .build();
        return mUserComponent;
    }

    public void releaseUserComponent() {
        mUserComponent = null;
    }
}
