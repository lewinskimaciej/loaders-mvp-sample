package com.example.mvppokemon.data.repositories.pokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.models.StatsModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public final class PokemonLocalDataSource implements PokemonDataSource {

    ReactiveEntityStore<Persistable> database;

    @Inject
    public PokemonLocalDataSource(ReactiveEntityStore<Persistable> database) {
        this.database = database;
    }

    @Override
    public Observable<PokemonModel> getPokemon(long number) {
        return database.findByKey(PokemonModel.class, number)
                .toObservable()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<PokemonModel> savePokemon(final PokemonModel pokemon) {
        List<StatsModel> tempList = new ArrayList<>(pokemon.getStats());

        return database.upsert(pokemon)
                .toObservable()
                .map(pokemonModel -> {
                    pokemonModel.getStats().clear();

                    for (StatsModel statsModel : tempList) {
                        pokemonModel.getStats().add(statsModel);
                    }
                    database.update(pokemonModel)
                            .subscribeOn(Schedulers.io())
                            .blockingGet();
                    return pokemonModel;
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<PokemonModel> getAllLocalPokemonSortedById() {
        return database.select(PokemonModel.class)
                .orderBy(PokemonModel.ID)
                .get()
                .observable()
                .subscribeOn(Schedulers.computation());
    }
}
