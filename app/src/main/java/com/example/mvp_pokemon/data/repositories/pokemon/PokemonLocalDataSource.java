package com.example.mvp_pokemon.data.repositories.pokemon;

import com.example.mvp_pokemon.data.models.PokemonModel;
import com.example.mvp_pokemon.data.models.StatsModel;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonDataSource;

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
        pokemonModel.setSprites(pokemonModel.getSprites());

        /** not working for some reason for OneToMany **/
        List<StatsModel> tempList = new ArrayList<>(pokemonModel.getStats());
        pokemonModel.getStats().clear();

        for (StatsModel statsModel : tempList) {
            statsModel.setBaseStat(statsModel.getBaseStat());
            pokemonModel.getStats().add(statsModel);
        }

        database.upsert(pokemonModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<PokemonModel>() {
                    @Override
                    public void onSuccess(PokemonModel value) {
                        Timber.d("onSuccess");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("onError");
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