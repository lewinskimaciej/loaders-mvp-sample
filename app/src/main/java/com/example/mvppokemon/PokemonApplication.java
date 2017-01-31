package com.example.mvppokemon;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.dagger.component.DaggerApplicationComponent;
import com.example.mvppokemon.dagger.module.ApplicationModule;
import com.example.mvppokemon.dagger.module.CommonModule;
import com.example.mvppokemon.dagger.module.DatabaseModule;

import timber.log.Timber;

public final class PokemonApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule(1, "mvp-pokemon-database"))
                .commonModule(new CommonModule("http://pokeapi.co/api/v2/", ""))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    @NonNull
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}