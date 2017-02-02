package com.example.mvppokemon.presentation.adapters.recycler.PokemonRecyclerViewAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mvppokemon.R;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.presentation.events.PokemonClickedEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 01.02.2017.
 *
 * @author Maciej Lewinski
 */

public class PokemonHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.pokemon_number)
    TextView pokemonNumber;

    @BindView(R.id.pokemon_sprite)
    ImageView pokemonSprite;

    @BindView(R.id.pokemon_name)
    TextView pokemonName;

    PokemonModel pokemonModel;
    Context context;

    PokemonHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.show_pokemon)
    public void showPokemon() {
        EventBus.getDefault().post(new PokemonClickedEvent(pokemonModel));
    }

    public void setPokemon(PokemonModel pokemon) {
        if (pokemon != null) {
            pokemonModel = pokemon;
            pokemonNumber.setText(String.valueOf(pokemonModel.getId()));
            pokemonName.setText(pokemonModel.getName());
            Glide.with(context)
                    .load(pokemonModel.getSprites().getFrontDefault())
                    .into(pokemonSprite);
        }
    }
}
