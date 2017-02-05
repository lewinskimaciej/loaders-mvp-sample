package com.example.mvppokemon.data.repositories.pokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvppokemon.data.retrofit.PokemonRetrofitInterface;

import javax.inject.Inject;

import io.reactivex.Observable;
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

    /**
     * @deprecated (Operation not available in remote data source)
     */
    @Deprecated
    @Override
    public void savePokemon(PokemonModel pokemonModel) {
        // cannot save in remote
    }

    /**
     * @deprecated (Operation not available in remote data source)
     */
    @Deprecated
    @Override
    public Observable<PokemonModel> getAllLocalPokemonSortedById() {
        // api has no method to get more than one at a time
        return null;
    }

}
