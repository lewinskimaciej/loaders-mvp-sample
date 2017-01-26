package com.example.mvp_pokemon.data.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.requery.CascadeAction;
import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.OneToOne;
import io.requery.Persistable;

/**
 * Created on 13.01.2017.
 *
 * @author Maciej Lewinski
 */

@Entity
public abstract class AbstractPokemonModel implements Parcelable, Persistable {

    @Key
    @SerializedName("id")
    long id;

    @SerializedName("name")
    String name;

    @OneToMany(mappedBy = "pokemonModel", cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    @SerializedName("stats")
    List<StatsModel> stats;

    @ForeignKey
    @OneToOne(cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    @SerializedName("sprites")
    SpritesModel sprites;
}
