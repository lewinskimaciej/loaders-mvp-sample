package com.example.mvppokemon.presentation.fragments.search;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.presentation.base.BasePresenterInterface;

public interface SearchPresenterInterface extends BasePresenterInterface<SearchView> {
    void getPokemon(int number);

    void pokemonClicked();

    void setPokemonData(PokemonModel pokemonData);
}
