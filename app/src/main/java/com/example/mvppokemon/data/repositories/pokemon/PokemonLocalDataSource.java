package com.example.mvppokemon.data.repositories.pokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.models.StatsModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import timber.log.Timber;

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PokemonModel> savePokemon(final PokemonModel pokemon) {
        List<StatsModel> tempList = new ArrayList<>(pokemon.getStats());

        pokemon.getStats().clear();


        return database.upsert(pokemon)
                .toObservable()
                .map(pokemonModel -> {
                    for (StatsModel statsModel : tempList) {
                        pokemonModel.getStats().add(statsModel);
                    }
                    database.update(pokemonModel)
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                    return pokemonModel;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PokemonModel> getAllLocalPokemonSortedById() {
        return database.select(PokemonModel.class)
                .orderBy(PokemonModel.ID)
                .get()
                .observable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
