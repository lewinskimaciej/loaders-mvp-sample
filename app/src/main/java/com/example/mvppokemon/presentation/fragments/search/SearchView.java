package com.example.mvppokemon.presentation.fragments.search;

import android.support.annotation.UiThread;

@UiThread
public interface SearchView {

    void setPokemonNumber(long number);

    void setPokemonName(String name);

    void setPokemonSprite(String url);

    void setButtonEnabled(boolean enabled);
}