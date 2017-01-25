package com.example.mvp_pokemon.presentation.activities.main.dagger;

import com.example.mvp_pokemon.dagger.component.ApplicationComponent;
import com.example.mvp_pokemon.dagger.scope.ActivityScope;
import com.example.mvp_pokemon.presentation.activities.main.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainViewModule.class)
public interface MainViewComponent {
    void injectTo(MainActivity activity);
}