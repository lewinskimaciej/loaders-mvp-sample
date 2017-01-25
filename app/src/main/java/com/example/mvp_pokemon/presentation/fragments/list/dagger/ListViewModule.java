package com.example.mvp_pokemon.presentation.fragments.list.dagger;

import android.support.annotation.NonNull;

import com.example.mvp_pokemon.dagger.qualifier.Repository;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvp_pokemon.presentation.PresenterFactory;
import com.example.mvp_pokemon.presentation.fragments.list.ListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public final class ListViewModule {

    @Provides
    public PresenterFactory<ListPresenter> providePresenterFactory(@NonNull @Repository PokemonRepositoryInterface pokemonRepository) {
        return () -> new ListPresenter(pokemonRepository);
    }
}
