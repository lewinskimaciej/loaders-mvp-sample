package com.example.mvppokemon.data.repositories.pokemon;

import com.example.mvppokemon.dagger.qualifier.LocalDataSource;
import com.example.mvppokemon.dagger.qualifier.RemoteDataSource;
import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.data.retrofit.PokemonRetrofitInterface;

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
