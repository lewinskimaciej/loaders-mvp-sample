package com.example.mvppokemon.dagger.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mvppokemon.PokemonApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class ApplicationModule {

    @NonNull
    private final Application application;

    public ApplicationModule(@NonNull Application application) {
        this.application = application;
    }

    @Provides
    public Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    public PokemonApplication providePokemonApplication() {
        return (PokemonApplication) application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }
}
