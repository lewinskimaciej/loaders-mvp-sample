package com.example.mvp_pokemon.data.repositories.pokemon;

import com.example.mvp_pokemon.data.models.PokemonModel;
import com.example.mvp_pokemon.data.models.SpritesModel;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonDataSource;

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
        return null;
    }

    @Override
    public void savePokemon(final PokemonModel pokemonModel) {
        database.insert(pokemonModel.getSprites())
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new DisposableSingleObserver<SpritesModel>() {
                    @Override
                    public void onSuccess(SpritesModel value) {
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
                                    }
                                });
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