package com.example.mvp_pokemon.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Abstract presenter implementation that contains base implementation for other presenters.
 * Subclasses must call super for all {@link BasePresenter} method overriding.
 */
public abstract class BasePresenter<V> implements BasePresenterInterface<V> {

    /**
     * The view
     */
    @Nullable
    protected V view;

    @Override
    public void onViewAttached(@NonNull V view) {
        this.view = view;
    }


    @Override
    public void onStart(boolean firstStart) {

    }

    @Override
    public void onStop() {

    }


    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onPresenterDestroyed() {

    }
}
