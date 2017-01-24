package com.example.mvp_pokemon.data.repositories.pokemon.interfaces;

import com.example.mvp_pokemon.data.models.PokemonModel;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface PokemonDataSource {
    Observable<PokemonModel> getPokemon(int number);

    void savePokemon(PokemonModel pokemonModel);

    Maybe<PokemonModel> getAllLocalPokemon();
}
