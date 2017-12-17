package com.example.konstantin.qiwi.Dagger;

import android.support.annotation.NonNull;

import com.example.konstantin.qiwi.Presenter.FormFragmentPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Konstantin on 17.12.2017.
 */

@Module
public class PresentersModule {
    @Provides
    @NonNull
    @Singleton
    public FormFragmentPresenter provideFormFragmentPresenter() {
        return new FormFragmentPresenter();
    }
}
