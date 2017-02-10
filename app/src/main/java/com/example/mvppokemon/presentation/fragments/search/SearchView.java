package com.example.mvppokemon.presentation.fragments.search;

import android.support.annotation.UiThread;

import com.example.mvppokemon.data.models.PokemonModel;

@UiThread
public interface SearchView {

    void setPokemon(PokemonModel pokemon);

    void setButtonEnabled(boolean enabled);

    void startPokemonActivity(PokemonModel pokemonModel);
}
