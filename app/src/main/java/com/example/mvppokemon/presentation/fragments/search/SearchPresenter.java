package com.example.mvppokemon.presentation.fragments.search;

import com.example.mvppokemon.common.dictionaries.HttpCode;
import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.base.BasePresenter;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public final class SearchPresenter extends BasePresenter<SearchView> implements SearchPresenterInterface {

    // The view is available using the view variable
    private PokemonRepositoryInterface pokemonRepository;

    private PokemonModel currentPokemon;

    @Inject
    public SearchPresenter(@Repository PokemonRepositoryInterface pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }


    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);
        // Your code here. Your view is available using view and will not be null until next onStop()
        setPokemonData(currentPokemon);
    }

    @Override
    public void getPokemon(int number) {
        if (view != null) {
            view.setButtonEnabled(false);
            pokemonRepository.getPokemon(number)
                    .subscribe(new DisposableObserver<PokemonModel>() {
                        @Override
                        public void onNext(PokemonModel value) {
                            Timber.d("Pokemon name: %s", String.valueOf(value.getName()));
                            setPokemonData(value);
                            view.setButtonEnabled(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "error");
                            view.setButtonEnabled(true);
                            if (e instanceof HttpException) {
                                switch (((HttpException) e).code()) {
                                    case HttpCode.NOT_FOUND:
                                        onNotFound();
                                        break;
                                    case HttpCode.INTERNAL_SERVER_ERROR:
                                        onInternalServerError();
                                        break;
                                    default:
                                        onGenericError();
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onComplete() {
                            Timber.d("onComplete");
                            view.setButtonEnabled(true);
                        }
                    });
        }
    }

    @Override
    public void pokemonClicked() {
        if (view != null) {
            view.startPokemonActivity(currentPokemon);
        }
    }

    @Override
    public void setPokemonData(PokemonModel pokemonData) {
        if (view != null && pokemonData != null) {
            currentPokemon = pokemonData;
            view.setPokemon(pokemonData);
        }
    }

}
