package com.example.mvppokemon.presentation.fragments.list.dagger;

import android.support.annotation.NonNull;

import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.base.PresenterFactory;
import com.example.mvppokemon.presentation.fragments.list.ListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public final class ListViewModule {

    @Provides
    public PresenterFactory<ListPresenter> providePresenterFactory(@NonNull @Repository PokemonRepositoryInterface pokemonRepository) {
        return () -> new ListPresenter(pokemonRepository);
    }
}
