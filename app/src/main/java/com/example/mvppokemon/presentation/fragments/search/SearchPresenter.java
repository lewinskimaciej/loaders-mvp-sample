package com.example.mvppokemon.presentation.fragments.search;

import com.example.mvppokemon.common.dictionaries.HttpCode;
import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.BasePresenter;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public final class SearchPresenter extends BasePresenter<SearchView> implements SearchPresenterInterface {

    // The view is available using the view variable
    PokemonRepositoryInterface pokemonRepository;

    PokemonModel currentPokemon;

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
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()
        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */
        super.onPresenterDestroyed();
    }

    @Override
    public void getPokemon(int number) {
        if (view != null) {
            view.setButtonEnabled(false);
            pokemonRepository.getPokemon(number)
                    .subscribe(new DisposableObserver<PokemonModel>() {
                        @Override
                        public void onNext(PokemonModel value) {
                            if (value != null) {
                                Timber.d("Pokemon name: %s", String.valueOf(value.getName()));
                                currentPokemon = value;
                                setPokemonData(currentPokemon);
                            }
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

    private void setPokemonData(PokemonModel pokemonData) {
        if (view != null && pokemonData != null) {
            view.setPokemonName(pokemonData.getName());
            view.setPokemonNumber(pokemonData.getId());
            view.setPokemonSprite(pokemonData.getSprites().getFrontDefault());
            view.setPokemonBackgroundVisbility(true);
        }
    }

}
