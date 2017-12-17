package com.example.konstantin.qiwi.Dagger;

import android.support.annotation.NonNull;

import com.example.konstantin.qiwi.Model.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Konstantin on 17.12.2017.
 */

@Module
public class DataProvidersModule {
    @Provides
    @NonNull
    @Singleton
    public DataManager provideDataResolver() {
        return new DataManager();
    }
}
