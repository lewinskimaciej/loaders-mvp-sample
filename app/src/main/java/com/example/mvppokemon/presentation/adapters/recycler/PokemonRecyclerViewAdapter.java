package com.example.mvppokemon.presentation.adapters.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mvppokemon.R;
import com.example.mvppokemon.data.models.PokemonModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 12.01.2017.
 *
 * @author Maciej Lewinski
 */

public class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PokemonModel> pokemonModels;

    private Comparator<PokemonModel> comparator;

    public PokemonRecyclerViewAdapter(Context context) {
        this.context = context;
        pokemonModels = new ArrayList<>();

        comparator = (pokemonModel, t1) -> Long.compare(pokemonModel.getId(), t1.getId());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_pokemon, parent, false);
        return new PokemonHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PokemonModel pokemonModel = pokemonModels.get(position);

        PokemonHolder pokemonHolder = (PokemonHolder) holder;

        pokemonHolder.setPokemon(pokemonModel);
    }

    @Override
    public int getItemCount() {
        return pokemonModels.size();
    }

    public void setPokemonList(List<PokemonModel> pokemonList) {
        this.pokemonModels = new ArrayList<>(pokemonList);
        Collections.sort(pokemonModels, comparator);
        notifyDataSetChanged();
    }

    class PokemonHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pokemon_number)
        TextView pokemonNumber;

        @BindView(R.id.pokemon_sprite)
        ImageView pokemonSprite;

        @BindView(R.id.pokemon_name)
        TextView pokemonName;

        PokemonModel pokemonModel;

        PokemonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.show_pokemon)
        public void showPokemon() {
            // TODO: intent to start PokemonActivity
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
}
