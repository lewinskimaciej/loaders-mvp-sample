package com.example.mvppokemon.data.repositories.pokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.models.StatsModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    public Observable<PokemonModel> getPokemon(int number) {
        return database.findByKey(PokemonModel.class, number)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void savePokemon(final PokemonModel pokemonModel) {
        List<StatsModel> tempList = new ArrayList<>(pokemonModel.getStats());

        database.upsert(pokemonModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<PokemonModel>() {
                    @Override
                    public void onSuccess(PokemonModel value) {
                        Timber.d("onSuccess inserting initial");

                        value.getStats().clear();

                        for (StatsModel statsModel : tempList) {
                            value.getStats().add(statsModel);
                        }

                        updateAfterInserting(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("onError inserting initial");
                    }
                });
    }

    private void updateAfterInserting(PokemonModel value) {
        database.update(value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<PokemonModel>() {
                    @Override
                    public void onSuccess(PokemonModel value) {
                        Timber.d("onSuccess fully inserted and updated");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("onError updating after insert");
                    }
                });
    }

    @Override
    public Single<List<PokemonModel>> getAllLocalPokemon() {
        return database.select(PokemonModel.class)
                .orderBy(PokemonModel.ID)
                .get()
                .observable()
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
