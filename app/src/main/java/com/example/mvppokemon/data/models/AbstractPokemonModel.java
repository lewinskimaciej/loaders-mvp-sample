package com.example.mvppokemon.data.models;

import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.requery.CascadeAction;
import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.OneToOne;
import io.requery.Persistable;

@Entity
public abstract class AbstractPokemonModel implements Parcelable, Persistable {

    @Key
    @JsonProperty("id")
    long id;

    @JsonProperty("name")
    String name;

    @OneToMany(mappedBy = "pokemonModel", cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    @JsonProperty("stats")
    List<StatsModel> stats;

    @ForeignKey
    @OneToOne(cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    @JsonProperty("sprites")
    SpritesModel sprites;
}
