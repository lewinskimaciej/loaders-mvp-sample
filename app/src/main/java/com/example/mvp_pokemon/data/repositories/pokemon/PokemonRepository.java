package com.example.mvp_pokemon.data.repositories.pokemon;

import com.example.mvp_pokemon.dagger.qualifier.LocalDataSource;
import com.example.mvp_pokemon.dagger.qualifier.RemoteDataSource;
import com.example.mvp_pokemon.data.models.PokemonModel;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public final class PokemonRepository implements PokemonRepositoryInterface {

    private final PokemonDataSource pokemonRemoteDataSource;

    private final PokemonDataSource pokemonLocalDataSource;

    @Inject
    public PokemonRepository(@RemoteDataSource PokemonDataSource pokemonRemoteDataSource,
                             @LocalDataSource PokemonDataSource pokemonLocalDataSource) {
        this.pokemonRemoteDataSource = pokemonRemoteDataSource;
        this.pokemonLocalDataSource = pokemonLocalDataSource;
    }

    @Override
    public Observable<PokemonModel> getPokemon(int number) {
        return pokemonRemoteDataSource.getPokemon(number)
                .map(pokemonModel -> {
                    savePokemon(pokemonModel);
                    return pokemonModel;
                })
                .mergeWith(pokemonLocalDataSource.getPokemon(number));
    }

    @Override
    public void savePokemon(PokemonModel pokemonModel) {
        pokemonLocalDataSource.savePokemon(pokemonModel);
    }

    @Override
    public Single<List<PokemonModel>> getAllLocalPokemon() {
        return pokemonLocalDataSource.getAllLocalPokemon();
    }

}
