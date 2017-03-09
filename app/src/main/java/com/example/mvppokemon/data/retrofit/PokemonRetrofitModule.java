package com.example.mvppokemon.data.retrofit;

import com.example.mvppokemon.dagger.qualifier.NonCachedRetrofit;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class PokemonRetrofitModule {
    @Provides
    PokemonRetrofitInterface providesPokemonRetrofitInterface(@NonCachedRetrofit Retrofit retrofit) {
        return retrofit.create(PokemonRetrofitInterface.class);
    }
}
