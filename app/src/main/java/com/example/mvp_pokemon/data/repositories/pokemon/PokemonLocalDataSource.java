package com.example.mvp_pokemon.data.repositories.pokemon;

import com.example.mvp_pokemon.data.models.PokemonModel;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonDataSource;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;
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
        return null;
    }

    @Override
    public void savePokemon(final PokemonModel pokemonModel) {
        database.insert(pokemonModel)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new DisposableSingleObserver<PokemonModel>() {
                    @Override
                    public void onSuccess(PokemonModel value) {
                        Timber.d("inserted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d(e, "not inserted");
                        database.update(pokemonModel)
                                .subscribeOn(Schedulers.computation())
                                .subscribe(new DisposableSingleObserver<PokemonModel>() {
                            @Override
                            public void onSuccess(PokemonModel value) {
                                Timber.d("updated");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.d("not updated");
                            }
                        });
                    }
                });

    }

    @Override
    public Maybe<PokemonModel> getAllLocalPokemon() {
        return database.select(PokemonModel.class)
                .get()
                .maybe()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}