package com.example.mvp_pokemon.presentation.activities.main.dagger;

import android.support.annotation.NonNull;

import com.example.mvp_pokemon.presentation.activities.main.MainPresenter;
import com.example.mvp_pokemon.presentation.activities.main.MainPresenterInterface;
import com.example.mvp_pokemon.presentation.PresenterFactory;

import dagger.Module;
import dagger.Provides;

@Module
public final class MainViewModule {

    @Provides
    public PresenterFactory<MainPresenter> providePresenterFactory() {
        return new PresenterFactory<MainPresenter>() {
            @NonNull
            @Override
            public MainPresenter create() {
                return new MainPresenter();
            }
        };
    }
}
