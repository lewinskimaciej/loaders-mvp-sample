package com.example.mvppokemon.presentation.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

/**
 * Loader that implements the loading of a Presenter, made to persist on activity recreation
 */
public final class PresenterLoader<T extends BasePresenterInterface> extends Loader<T> {

    /**
     * Factory to create the presenter
     */
    @NonNull
    private final PresenterFactory<T> presenterFactory;
    /**
     * The presenter, will be null if not created yet
     */
    @Nullable
    private T presenter;

    public PresenterLoader(Context context, @NonNull PresenterFactory<T> presenterFactory) {
        super(context);
        this.presenterFactory = presenterFactory;
    }

    @Override
    protected void onStartLoading() {
        // if we already own a presenter instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        // Otherwise, force a load
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        // Create the Presenter using the Factory
        presenter = presenterFactory.create();

        // Deliver the result
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        if (presenter != null) {
            presenter.onPresenterDestroyed();
            presenter = null;
        }
    }
}
