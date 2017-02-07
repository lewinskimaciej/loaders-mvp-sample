package com.example.mvppokemon.presentation.activities.main.dagger;

import com.example.mvppokemon.presentation.base.PresenterFactory;
import com.example.mvppokemon.presentation.activities.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public final class MainViewModule {

    @Provides
    public PresenterFactory<MainPresenter> providePresenterFactory() {
        return MainPresenter::new;
    }
}
