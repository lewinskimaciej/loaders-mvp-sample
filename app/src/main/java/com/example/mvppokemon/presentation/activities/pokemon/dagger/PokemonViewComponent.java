package com.example.mvppokemon.presentation.activities.pokemon.dagger;

import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.dagger.scope.ActivityScope;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvppokemon.data.retrofit.PokemonRetrofitModule;
import com.example.mvppokemon.presentation.activities.pokemon.PokemonActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class,
        modules = {PokemonViewModule.class,
                PokemonRepositoryModule.class,
                PokemonRetrofitModule.class})
public interface PokemonViewComponent {
    void injectTo(PokemonActivity activity);
}
