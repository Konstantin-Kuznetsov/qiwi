package com.example.konstantin.qiwi.Dagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Konstantin on 17.12.2017.
 */

@Singleton
@Component(modules = {AppModule.class, DataProvidersModule.class, PresentersModule.class, UtilsModule.class})
public interface AppComponent  {
}
