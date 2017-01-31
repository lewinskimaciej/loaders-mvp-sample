package com.example.mvppokemon.presentation.fragments.list.dagger;

import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.dagger.scope.FragmentScope;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvppokemon.data.retrofit.PokemonRetrofitModule;
import com.example.mvppokemon.presentation.fragments.list.ListFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class,
        modules = {ListViewModule.class,
                PokemonRepositoryModule.class,
                PokemonRetrofitModule.class})
public interface ListViewComponent {
    void injectTo(ListFragment fragment);
}