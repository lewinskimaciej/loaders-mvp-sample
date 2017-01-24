package com.example.mvp_pokemon.presentation.fragments.list;

import android.support.annotation.UiThread;

import com.example.mvp_pokemon.data.models.PokemonModel;

import java.util.ArrayList;

@UiThread
public interface ListView {
    void setAdapterData(ArrayList<PokemonModel> pokemonList);

    void addPokemonToAdapter(PokemonModel pokemonModel);

    void setLoaderVisibility(boolean visible);
}