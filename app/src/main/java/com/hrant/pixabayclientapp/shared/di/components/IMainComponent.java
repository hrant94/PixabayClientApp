package com.hrant.pixabayclientapp.shared.di.components;


import com.hrant.pixabayclientapp.shared.di.components.root.IAppComponent;
import com.hrant.pixabayclientapp.shared.di.modules.MainModule;
import com.hrant.pixabayclientapp.shared.di.scopes.MainScope;
import com.hrant.pixabayclientapp.view.activities.mainActivity.MainActivity;

import dagger.Component;

@MainScope
@Component(dependencies = {IAppComponent.class}, modules = {MainModule.class})
public interface IMainComponent {
    void inject(MainActivity mainActivity);
}