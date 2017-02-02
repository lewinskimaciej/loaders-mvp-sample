package com.example.mvppokemon.presentation.activities.pokemon;

import android.support.annotation.UiThread;

import com.example.mvppokemon.data.models.StatsModel;

import java.util.List;

@UiThread
public interface PokemonView {

    void setPokemonSprite(String url);

    void setPokemonName(String name);

    void setPokemonId(int id);

    void setPokemonStats(List<StatsModel> statsModel);

    void setActionBarTitle(String title);
}
