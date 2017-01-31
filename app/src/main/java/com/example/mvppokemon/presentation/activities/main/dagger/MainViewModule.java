package com.example.mvppokemon.presentation.activities.main.dagger;

import android.support.annotation.NonNull;

import com.example.mvppokemon.presentation.PresenterFactory;
import com.example.mvppokemon.presentation.activities.main.MainPresenter;

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
