package com.example.mvp_pokemon.data.retrofit;

import com.example.mvp_pokemon.dagger.qualifier.NonCachedRetrofit;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created on 10.01.2017.
 *
 * @author Maciej Lewinski
 */

@Module
public class PokemonRetrofitModule {
    @Provides
    PokemonRetrofitInterface providesPokemonRetrofitInterface(@NonCachedRetrofit Retrofit retrofit) {
        return retrofit.create(PokemonRetrofitInterface.class);
    }
}
