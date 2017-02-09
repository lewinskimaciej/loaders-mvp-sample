package com.example.mvppokemon.data.repositories.pokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvppokemon.data.retrofit.PokemonRetrofitInterface;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class PokemonRemoteDataSource implements PokemonDataSource {

    public static final String METHOD_NOT_AVAILABLE = "Method not available for remote";

    PokemonRetrofitInterface pokemonRetrofitInterface;

    @Inject
    public PokemonRemoteDataSource(PokemonRetrofitInterface pokemonRetrofitInterface) {
        this.pokemonRetrofitInterface = pokemonRetrofitInterface;
    }

    @Override
    public Observable<PokemonModel> getPokemon(long number) {
        return pokemonRetrofitInterface.getPokemon(number)
                .subscribeOn(Schedulers.io());
    }

    /**
     * @deprecated (Operation not available in remote data source)
     */
    @Deprecated
    @Override
    public Observable<PokemonModel> savePokemon(PokemonModel pokemonModel) {
        // cannot save in remote
        return Observable.error(new Throwable(METHOD_NOT_AVAILABLE));
    }

    /**
     * @deprecated (Operation not available in remote data source)
     */
    @Deprecated
    @Override
    public Observable<PokemonModel> getAllLocalPokemonSortedById() {
        // api has no method to get more than one at a time
        return Observable.error(new Throwable(METHOD_NOT_AVAILABLE));
    }

}
