package com.example.mvp_pokemon.data.repositories.pokemon.interfaces;

import com.example.mvp_pokemon.data.models.PokemonModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface PokemonDataSource {
    Observable<PokemonModel> getPokemon(int number);

    void savePokemon(PokemonModel pokemonModel);

    Single<List<PokemonModel>> getAllLocalPokemon();
}
