package com.example.mvppokemon.presentation.fragments.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mvppokemon.common.dictionaries.BundleKey;
import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.BasePresenter;
import com.example.mvppokemon.presentation.activities.pokemon.PokemonActivity;
import com.example.mvppokemon.presentation.events.PokemonClickedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;

public final class ListPresenter extends BasePresenter<ListView> implements ListPresenterInterface {

    // The view is available using the view variable
    PokemonRepositoryInterface pokemonRepository;

    @Inject
    public ListPresenter(@Repository PokemonRepositoryInterface pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);
        EventBus.getDefault().register(this);

        getAllLocalPokemon();
    }

    private void setPokemonList(List<PokemonModel> pokemon) {
        if (pokemon != null) {
            if (view != null) {
                view.setAdapterData(pokemon);
            }
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void refreshData() {
        getAllLocalPokemon();
    }

    private void getAllLocalPokemon() {
        if (view != null) {
            view.setLoaderVisibility(true);
        }
        pokemonRepository.getAllLocalPokemon().subscribe(new DisposableSingleObserver<List<PokemonModel>>() {
            @Override
            public void onSuccess(List<PokemonModel> value) {
                if (view != null) {
                    view.setLoaderVisibility(false);
                    view.hideSwipeRefreshLoader();
                }

                setPokemonList(value);
            }

            @Override
            public void onError(Throwable e) {
                if (view != null) {
                    view.setLoaderVisibility(false);
                    view.hideSwipeRefreshLoader();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPokemonClicked(PokemonClickedEvent event) {
        if (view != null) {
            Activity activity = view.getParentActivity();
            Intent intent = new Intent(activity, PokemonActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(BundleKey.KEY_POKEMON_ID, event.getPokemonModel().getId());
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
    }
}
