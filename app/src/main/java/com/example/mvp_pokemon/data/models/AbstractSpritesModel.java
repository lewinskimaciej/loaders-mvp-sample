package com.example.mvp_pokemon.data.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToOne;
import io.requery.Persistable;

/**
 * Created on 13.01.2017.
 *
 * @author Maciej Lewinski
 */

@Entity
public abstract class AbstractSpritesModel implements Parcelable, Persistable {

    @Key
    @Generated
    long id;

    @ForeignKey
    @OneToOne
    PokemonModel pokemonModel;

    @SerializedName("front_default")
    String frontDefault;
}
