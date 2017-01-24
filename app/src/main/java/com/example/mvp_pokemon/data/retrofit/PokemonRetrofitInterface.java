package com.example.mvp_pokemon.data.retrofit;

import com.example.mvp_pokemon.data.models.PokemonModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created on 10.01.2017.
 *
 * @author Maciej Lewinski
 */

public interface PokemonRetrofitInterface {
    @GET("pokemon/{number}")
    Observable<PokemonModel> getPokemon(@Path("number") int number);
}
