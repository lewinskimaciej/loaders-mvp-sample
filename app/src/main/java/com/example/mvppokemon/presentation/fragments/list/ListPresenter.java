package com.example.mvppokemon.presentation.fragments.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mvppokemon.common.dictionaries.BundleKey;
import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.base.BasePresenter;
import com.example.mvppokemon.presentation.activities.pokemon.PokemonActivity;
import com.example.mvppokemon.presentation.events.PokemonClickedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
        EventBus.getDefault().register(this);
        if (view != null) {
            if (pokemonModelList != null) {
                view.setElementsInAdapter(pokemonModelList);
            } else {
                getAllLocalPokemon();
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
            view.setRefreshing(true);

            pokemonModelList = new ArrayList<>();

            pokemonRepository.getAllLocalPokemonSortedById()
                    .observeOn(AndroidSchedulers.mainThread())
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
