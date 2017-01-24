package com.example.mvp_pokemon;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.mvp_pokemon.dagger.component.ApplicationComponent;
import com.example.mvp_pokemon.dagger.module.ApplicationModule;
import com.example.mvp_pokemon.dagger.module.CommonModule;
import com.example.mvp_pokemon.dagger.module.DatabaseModule;
import com.example.mvp_pokemon.dagger.component.DaggerApplicationComponent;

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