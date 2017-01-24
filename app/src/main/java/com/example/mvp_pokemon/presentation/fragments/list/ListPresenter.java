package com.example.mvp_pokemon.presentation.fragments.list;

import com.example.mvp_pokemon.dagger.qualifier.Repository;
import com.example.mvp_pokemon.data.models.PokemonModel;
import com.example.mvp_pokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvp_pokemon.presentation.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public final class ListPresenter extends BasePresenter<ListView> implements ListPresenterInterface {

    ArrayList<PokemonModel> pokemonList = new ArrayList<>();

    // The view is available using the view variable
    PokemonRepositoryInterface pokemonRepository;

    @Inject
    public ListPresenter(@Repository PokemonRepositoryInterface pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);
        // Your code here. Your view is available using view and will not be null until next onStop()
        pokemonRepository.getAllLocalPokemon().subscribe(new DisposableMaybeObserver<PokemonModel>() {
            @Override
            public void onSuccess(PokemonModel value) {
                Timber.d("onSuccess");
                addPokemon(value);
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("onError");
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete");
            }
        });
    }

    private void addPokemon(PokemonModel pokemon) {
        if (pokemon != null) {
            pokemonList.add(pokemon);
            if (view != null) {
                view.addPokemonToAdapter(pokemon);
            }
        }
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
    public void refreshData() {
        if (view != null) {
            view.setAdapterData(pokemonList);
        }
    }
}