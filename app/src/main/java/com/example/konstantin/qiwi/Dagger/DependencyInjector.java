package com.example.konstantin.qiwi.Dagger;

import android.app.Application;

/**
 * Created by Konstantin on 17.12.2017.
 */

public class DependencyInjector extends Application {
    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        component = buildComponent();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this.getApplicationContext()))
                .utilsModule(new UtilsModule())
                .dataProvidersModule(new DataProvidersModule())
                .presentersModule(new PresentersModule())
                .build();
    }
}
