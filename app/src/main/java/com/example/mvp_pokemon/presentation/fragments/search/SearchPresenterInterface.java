package com.example.mvp_pokemon.presentation.fragments.search;

import com.example.mvp_pokemon.presentation.BasePresenterInterface;

public interface SearchPresenterInterface extends BasePresenterInterface<SearchView> {
    void getPokemon(int number);
}