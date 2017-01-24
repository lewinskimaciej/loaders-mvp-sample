package com.example.mvp_pokemon.presentation.fragments.list;

import android.support.annotation.UiThread;

import com.example.mvp_pokemon.data.models.PokemonModel;

import java.util.List;

@UiThread
public interface ListView {
    void setAdapterData(List<PokemonModel> pokemonList);

    void setLoaderVisibility(boolean visible);

    void hideSwipeRefreshLoader();
}