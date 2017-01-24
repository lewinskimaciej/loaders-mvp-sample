package com.example.mvp_pokemon.data.models;

import com.google.gson.annotations.SerializedName;

import io.requery.Entity;

/**
 * Created on 13.01.2017.
 *
 * @author Maciej Lewinski
 */

@Entity
public abstract class AbstractSpritesModel {

    @SerializedName("front_default")
    String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
