package com.example.mvp_pokemon.presentation.fragments.search.dagger;

import android.support.annotation.NonNull;

import com.example.mvp_pokemon.dagger.qualifier.Repository;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvp_pokemon.presentation.PresenterFactory;
import com.example.mvp_pokemon.presentation.fragments.search.SearchPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public final class SearchViewModule {

    @Provides
    public PresenterFactory<SearchPresenter> providePresenterFactory(@NonNull @Repository PokemonRepositoryInterface pokemonRepository) {
        return () -> new SearchPresenter(pokemonRepository);
    }
}
