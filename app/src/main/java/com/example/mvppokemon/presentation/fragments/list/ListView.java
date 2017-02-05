package com.example.mvppokemon.presentation.fragments.list;

import android.app.Activity;
import android.support.annotation.UiThread;

import com.example.mvppokemon.data.models.PokemonModel;

import java.util.List;

@UiThread
public interface ListView {

    void setElementsInAdapter(List<PokemonModel> pokemonList);

    void addElementToAdapter(PokemonModel pokemon);

    void setRefreshing(boolean visibility);

    Activity getParentActivity();
}
