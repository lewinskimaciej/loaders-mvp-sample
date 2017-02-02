package com.example.mvppokemon.presentation.activities.pokemon;

import com.example.mvppokemon.presentation.BasePresenterInterface;

public interface PokemonPresenterInterface extends BasePresenterInterface<PokemonView> {
    void getPokemonId(int pokemonId);
}
