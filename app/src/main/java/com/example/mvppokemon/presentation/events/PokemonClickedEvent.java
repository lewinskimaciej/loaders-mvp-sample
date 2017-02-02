package com.example.mvppokemon.presentation.events;

import com.example.mvppokemon.data.models.PokemonModel;

/**
 * Created on 02.02.2017.
 *
 * @author Maciej Lewinski
 */
public class PokemonClickedEvent {
    private PokemonModel pokemonModel;

    public PokemonClickedEvent(PokemonModel pokemonModel) {
        this.pokemonModel = pokemonModel;
    }

    public PokemonModel getPokemonModel() {
        return pokemonModel;
    }

    public void setPokemonModel(PokemonModel pokemonModel) {
        this.pokemonModel = pokemonModel;
    }
}
