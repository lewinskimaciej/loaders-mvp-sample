package com.example.mvppokemon.presentation.activities.main.dagger;

import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.dagger.scope.ActivityScope;
import com.example.mvppokemon.presentation.activities.main.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainViewModule.class)
public interface MainViewComponent {
    void injectTo(MainActivity activity);
}