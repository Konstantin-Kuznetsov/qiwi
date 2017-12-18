package com.example.konstantin.qiwi.Dagger;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by Konstantin on 17.12.2017.
 */

@Module
public class UtilsModule {

    @Provides
    @NonNull
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @NonNull
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }
}
