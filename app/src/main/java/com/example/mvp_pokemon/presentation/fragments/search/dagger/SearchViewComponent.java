package com.example.mvp_pokemon.presentation.fragments.search.dagger;

import com.example.mvp_pokemon.dagger.component.ApplicationComponent;
import com.example.mvp_pokemon.dagger.scope.FragmentScope;
import com.example.mvp_pokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvp_pokemon.data.retrofit.PokemonRetrofitModule;
import com.example.mvp_pokemon.presentation.fragments.search.SearchFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class,
        modules = {SearchViewModule.class,
                PokemonRepositoryModule.class,
                PokemonRetrofitModule.class})
public interface SearchViewComponent {
    void injectTo(SearchFragment fragment);
}