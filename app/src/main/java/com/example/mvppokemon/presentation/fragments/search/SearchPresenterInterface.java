package com.example.mvppokemon.presentation.fragments.search;

import android.content.Context;

import com.example.mvppokemon.presentation.base.BasePresenterInterface;

public interface SearchPresenterInterface extends BasePresenterInterface<SearchView> {
    void getPokemon(int number);

    void pokemonClicked(Context context);
}
