package com.example.mvppokemon.presentation.activities.pokemon;

import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public final class PokemonPresenter extends BasePresenter<PokemonView> implements PokemonPresenterInterface {

    PokemonRepositoryInterface pokemonRepository;

    PokemonModel pokemonModel;

    @Inject
    public PokemonPresenter(@Repository PokemonRepositoryInterface pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);

        if (pokemonModel != null) {
            setPokemon(pokemonModel);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void getPokemonId(int pokemonId) {
        pokemonRepository.getPokemon(pokemonId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<PokemonModel>() {
                    @Override
                    public void onNext(PokemonModel value) {
                        pokemonModel = value;
                        setPokemon(pokemonModel);
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

    @Override
    public void setPokemon(PokemonModel pokemon) {
        if (view != null) {
            view.setPokemonId((int) pokemon.getId());
            view.setPokemonSprite(pokemon.getSprites().getFrontDefault());
            view.setPokemonName(pokemon.getName());
            view.setActionBarTitle(pokemon.getName());
            view.setPokemonStats(pokemon.getStats());
        }
    }
}
