package com.example.mvp_pokemon.data.models;

import com.google.gson.annotations.SerializedName;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Key;
import io.requery.OneToOne;

/**
 * Created on 13.01.2017.
 *
 * @author Maciej Lewinski
 */

@Entity
public abstract class AbstractPokemonModel {

    @Key
    @SerializedName("id")
    long id;

    @SerializedName("name")
    String name;

    @ForeignKey
    @OneToOne
    @SerializedName("sprites")
    SpritesModel spritesModel;
}
