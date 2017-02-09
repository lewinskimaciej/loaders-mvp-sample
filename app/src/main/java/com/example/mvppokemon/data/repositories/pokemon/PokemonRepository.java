package com.example.mvppokemon.data.repositories.pokemon;

import com.example.mvppokemon.dagger.qualifier.LocalDataSource;
import com.example.mvppokemon.dagger.qualifier.RemoteDataSource;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

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
    public Observable<PokemonModel> getPokemon(long number) {
        return pokemonRemoteDataSource.getPokemon(number)
                .map(pokemonModel -> {
                    savePokemon(pokemonModel)
                            .subscribe();
                    return pokemonModel;
                })
                .mergeWith(pokemonLocalDataSource.getPokemon(number));
    }

    @Override
    public Observable<PokemonModel> savePokemon(PokemonModel pokemonModel) {
        return pokemonLocalDataSource.savePokemon(pokemonModel);
    }

    @Override
    public Observable<PokemonModel> getAllLocalPokemonSortedById() {
        return pokemonLocalDataSource.getAllLocalPokemonSortedById();
    }

}
