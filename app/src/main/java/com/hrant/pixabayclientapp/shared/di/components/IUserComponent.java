package com.hrant.pixabayclientapp.shared.di.components;


import com.hrant.pixabayclientapp.shared.di.components.root.IAppComponent;
import com.hrant.pixabayclientapp.shared.di.modules.root.UserModule;
import com.hrant.pixabayclientapp.shared.di.scopes.UserScope;
import com.hrant.pixabayclientapp.view.activities.MainActivity;

import dagger.Component;

@UserScope
@Component(dependencies = {IAppComponent.class}, modules = {UserModule.class})
public interface IUserComponent {
    void inject(MainActivity mainActivity);
}