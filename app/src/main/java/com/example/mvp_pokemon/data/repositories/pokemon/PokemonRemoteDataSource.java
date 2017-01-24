package com.example.mvp_pokemon.data.repositories.pokemon;

import com.example.mvp_pokemon.data.models.PokemonModel;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvp_pokemon.data.retrofit.PokemonRetrofitInterface;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class PokemonRemoteDataSource implements PokemonDataSource {

    PokemonRetrofitInterface pokemonRetrofitInterface;

    @Inject
    public PokemonRemoteDataSource(PokemonRetrofitInterface pokemonRetrofitInterface) {
        this.pokemonRetrofitInterface = pokemonRetrofitInterface;
    }

    @Override
    public Observable<PokemonModel> getPokemon(int number) {
        return pokemonRetrofitInterface.getPokemon(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Deprecated
    @Override
    public void savePokemon(PokemonModel pokemonModel) {
        // cannot save in remote
    }

    @Deprecated
    @Override
    public Single<List<PokemonModel>> getAllLocalPokemon() {
        // do nothing
        return null;
    }
}
