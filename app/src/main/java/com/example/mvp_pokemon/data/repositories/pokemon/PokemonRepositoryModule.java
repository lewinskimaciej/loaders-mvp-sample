package com.example.mvp_pokemon.data.repositories.pokemon;

import com.example.mvp_pokemon.dagger.qualifier.LocalDataSource;
import com.example.mvp_pokemon.dagger.qualifier.RemoteDataSource;
import com.example.mvp_pokemon.dagger.qualifier.Repository;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvp_pokemon.data.retrofit.PokemonRetrofitInterface;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

@Module
public final class PokemonRepositoryModule {

    @Provides
    @LocalDataSource
    PokemonDataSource providePokemonLocalDataSource(ReactiveEntityStore<Persistable> database) {
        return new PokemonLocalDataSource(database);
    }

    @Provides
    @RemoteDataSource
    PokemonDataSource providePokemonRemoteDataSource(PokemonRetrofitInterface pokemonRetrofit) {
        return new PokemonRemoteDataSource(pokemonRetrofit);
    }

    @Provides
    @Repository
    PokemonRepositoryInterface providePokemonRepository(@RemoteDataSource PokemonDataSource pokemonRemoteDataSource,
                                                        @LocalDataSource PokemonDataSource pokemonLocalDataSource) {
        return new PokemonRepository(pokemonRemoteDataSource, pokemonLocalDataSource);
    }

}
