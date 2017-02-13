package com.example.mvppokemon.presentation.fragments.list;

import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public final class ListPresenter extends BasePresenter<ListView> implements ListPresenterInterface {

    // The view is available using the view variable
    PokemonRepositoryInterface pokemonRepository;

    List<PokemonModel> pokemonModelList;

    @Inject
    public ListPresenter(@Repository PokemonRepositoryInterface pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);
        if (view != null) {
            if (pokemonModelList != null) {
                view.setElementsInAdapter(pokemonModelList);
            } else {
                refreshData();
            }
        }
    }

    @Override
    public void refreshData() {
        getAllLocalPokemon();
    }

    private void getAllLocalPokemon() {
        if (view != null) {
            view.setRefreshing(true);

            pokemonModelList = new ArrayList<>();

            pokemonRepository.getAllLocalPokemonSortedById()
                    .subscribe(new DisposableObserver<PokemonModel>() {
                        @Override
                        public void onNext(PokemonModel value) {
                            Timber.d("%s", value.getName());
                            pokemonModelList.add(value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d("onError");
                            if (view != null) {
                                onGenericError();
                                view.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onComplete() {
                            Timber.d("onComplete");
                            if (view != null) {
                                view.setElementsInAdapter(pokemonModelList);
                                view.setRefreshing(false);
                            }
                        }
                    });
        }
    }

}
