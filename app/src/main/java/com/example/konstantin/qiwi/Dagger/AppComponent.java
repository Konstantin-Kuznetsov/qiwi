package com.example.konstantin.qiwi.Dagger;

import com.example.konstantin.qiwi.Model.DataResolver;
import com.example.konstantin.qiwi.Presenter.FormFragmentPresenter;
import com.example.konstantin.qiwi.UI.FormFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Konstantin on 17.12.2017.
 */

@Singleton
@Component(modules = {AppModule.class, DataProvidersModule.class, PresentersModule.class, UtilsModule.class})
public interface AppComponent  {

    void inject(DataResolver dataResolver);

    void inject(FormFragmentPresenter formFragmentPresenter);

    void inject(FormFragment formFragment);



}
