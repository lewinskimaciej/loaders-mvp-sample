package com.example.mvp_pokemon.data.repositories.pokemon.interfaces;

import com.example.mvp_pokemon.data.models.PokemonModel;

import io.reactivex.Observable;


public interface PokemonRepositoryInterface extends PokemonDataSource {
    Observable<PokemonModel> getPokemon(int number);

    void savePokemon(PokemonModel pokemonModel);
}
