package com.example.konstantin.qiwi.Dagger;

import android.support.annotation.NonNull;

import com.example.konstantin.qiwi.Model.QiwiBackend;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Konstantin on 17.12.2017.
 */

@Module
public class UtilsModule {
    private static final String BASE_URL = "https://w.qiwi.com/mobile/";

    @Provides
    @NonNull
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @NonNull
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @NonNull
    @Singleton
    public QiwiBackend.QiwiInterface provideQiwiApi(Retrofit retrofitInstance) {
        return retrofitInstance.create(QiwiBackend.QiwiInterface.class);
    }
}
