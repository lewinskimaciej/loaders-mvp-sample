package com.example.mvppokemon.presentation.activities.pokemon;

import com.example.mvppokemon.presentation.base.BasePresenterInterface;

public interface PokemonPresenterInterface extends BasePresenterInterface<PokemonView> {
    void getPokemonId(int pokemonId);
}
