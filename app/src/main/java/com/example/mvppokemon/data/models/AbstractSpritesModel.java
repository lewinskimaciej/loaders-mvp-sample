package com.example.mvppokemon.data.models;

import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.requery.CascadeAction;
import io.requery.Entity;
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

    @OneToOne(mappedBy = "sprites", cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    PokemonModel pokemonModel;

    @JsonProperty("front_default")
    String frontDefault;
}
