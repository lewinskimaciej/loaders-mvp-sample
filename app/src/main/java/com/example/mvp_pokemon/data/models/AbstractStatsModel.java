package com.example.mvp_pokemon.data.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.requery.CascadeAction;
import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;

/**
 * Created on 25.01.2017.
 *
 * @author Maciej Lewinski
 */
@Entity
public abstract class AbstractStatsModel implements Parcelable, Persistable {

    @Key
    @Generated
    long id;

    @SerializedName("effort")
    int effort;

    @SerializedName("base_stat")
    int baseStat;

    @ForeignKey
    @OneToOne(cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    @SerializedName("stat")
    StatModel statModel;

    @ManyToOne(cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    PokemonModel pokemonModel;
}
