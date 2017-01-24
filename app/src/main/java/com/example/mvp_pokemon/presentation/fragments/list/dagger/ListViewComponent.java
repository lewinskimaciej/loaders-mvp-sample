package com.example.mvp_pokemon.presentation.fragments.list.dagger;

import com.example.mvp_pokemon.dagger.component.ApplicationComponent;
import com.example.mvp_pokemon.dagger.scope.FragmentScope;
import com.example.mvp_pokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvp_pokemon.data.retrofit.PokemonRetrofitModule;
import com.example.mvp_pokemon.presentation.fragments.list.ListFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class,
        modules = {ListViewModule.class,
                PokemonRepositoryModule.class,
                PokemonRetrofitModule.class})
public interface ListViewComponent {
    void injectTo(ListFragment fragment);
}