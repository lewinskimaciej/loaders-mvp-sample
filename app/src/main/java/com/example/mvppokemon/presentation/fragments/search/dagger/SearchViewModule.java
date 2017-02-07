package com.example.mvppokemon.presentation.fragments.search.dagger;

import android.support.annotation.NonNull;

import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.base.PresenterFactory;
import com.example.mvppokemon.presentation.fragments.search.SearchPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public final class SearchViewModule {

    @Provides
    public PresenterFactory<SearchPresenter> providePresenterFactory(@NonNull @Repository PokemonRepositoryInterface pokemonRepository) {
        return () -> new SearchPresenter(pokemonRepository);
    }
}
