package com.example.mvppokemon.data.repositories.pokemon.interfaces;

import com.example.mvppokemon.data.models.PokemonModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;


public interface PokemonRepositoryInterface {

    Observable<PokemonModel> getPokemon(int number);

    void savePokemon(PokemonModel pokemonModel);

    Single<List<PokemonModel>> getAllLocalPokemon();
}
