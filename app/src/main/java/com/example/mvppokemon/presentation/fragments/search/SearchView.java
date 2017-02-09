package com.example.mvppokemon.presentation.fragments.search;

import android.support.annotation.UiThread;

import com.example.mvppokemon.data.models.PokemonModel;

@UiThread
public interface SearchView {

    void setPokemonNumber(long number);

    void setPokemonName(String name);

    void setPokemonSprite(String url);

    void setPokemonBackgroundVisbility(boolean visible);

    void setButtonEnabled(boolean enabled);

    void startPokemonActivity(PokemonModel pokemonModel);
}
