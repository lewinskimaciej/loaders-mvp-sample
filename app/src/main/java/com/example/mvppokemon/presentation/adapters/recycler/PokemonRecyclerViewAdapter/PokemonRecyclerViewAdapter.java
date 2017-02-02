package com.example.mvppokemon.presentation.adapters.recycler.PokemonRecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.mvppokemon.R;
import com.example.mvppokemon.data.models.PokemonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 12.01.2017.
 *
 * @author Maciej Lewinski
 */

public class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<PokemonModel> pokemonModels;

    public PokemonRecyclerViewAdapter(Context context) {
        this.context = context;
        pokemonModels = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_pokemon, parent, false);
        return new PokemonHolder(context, view);
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
        notifyDataSetChanged();
    }
}
