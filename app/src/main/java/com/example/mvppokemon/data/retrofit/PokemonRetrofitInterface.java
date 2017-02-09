package com.example.mvppokemon.data.retrofit;

import com.example.mvppokemon.data.models.PokemonModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PokemonRetrofitInterface {
    @GET("pokemon/{number}")
    Observable<PokemonModel> getPokemon(@Path("number") long number);
}
