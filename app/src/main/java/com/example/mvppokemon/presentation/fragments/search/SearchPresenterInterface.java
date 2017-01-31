package com.example.mvppokemon.presentation.fragments.search;

import com.example.mvppokemon.presentation.BasePresenterInterface;

public interface SearchPresenterInterface extends BasePresenterInterface<SearchView> {
    void getPokemon(int number);
}