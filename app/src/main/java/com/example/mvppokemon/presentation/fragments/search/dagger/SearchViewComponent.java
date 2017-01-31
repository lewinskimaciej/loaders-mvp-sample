package com.example.mvppokemon.presentation.fragments.search.dagger;

import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.dagger.scope.FragmentScope;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvppokemon.data.retrofit.PokemonRetrofitModule;
import com.example.mvppokemon.presentation.fragments.search.SearchFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class,
        modules = {SearchViewModule.class,
                PokemonRepositoryModule.class,
                PokemonRetrofitModule.class})
public interface SearchViewComponent {
    void injectTo(SearchFragment fragment);
}