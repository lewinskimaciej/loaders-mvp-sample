package com.example.mvppokemon.data.repositories.pokemon.interfaces;

import com.example.mvppokemon.data.models.PokemonModel;

import io.reactivex.Observable;
import io.reactivex.Single;


public interface PokemonRepositoryInterface {

    Observable<PokemonModel> getPokemon(long number);

    Observable<PokemonModel> savePokemon(PokemonModel pokemonModel);

    Observable<PokemonModel> getAllLocalPokemonSortedById();
}
