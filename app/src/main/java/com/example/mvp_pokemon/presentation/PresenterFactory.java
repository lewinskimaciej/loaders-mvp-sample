package com.example.mvp_pokemon.presentation;

import android.support.annotation.NonNull;

/**
 * Factory to implement to create a presenter
 */
public interface PresenterFactory<T extends BasePresenterInterface> {
    @NonNull
    T create();
}
