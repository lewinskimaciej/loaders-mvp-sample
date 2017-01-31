package com.example.mvppokemon.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Abstract presenter implementation that contains base implementation for other presenters.
 * Subclasses must call super for all {@link BasePresenter} method overriding.
 */
public abstract class BasePresenter<V>
        implements BasePresenterInterface<V>,
        HttpExceptionResolutionInterface {

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

    @Override
    public void onInternalServerError() {
        if (view != null) {
            if (view instanceof BaseActivity) {
                ((BaseActivity) view).onInternalServerError();
            } else if (view instanceof BaseFragment) {
                ((BaseFragment) view).onInternalServerError();
            }
        }
    }

    @Override
    public void onNotFound() {
        if (view != null) {
            if (view instanceof BaseActivity) {
                ((BaseActivity) view).onNotFound();
            } else if (view instanceof BaseFragment) {
                ((BaseFragment) view).onNotFound();
            }
        }
    }

    @Override
    public void onGenericError() {
        if (view != null) {
            if (view instanceof BaseActivity) {
                ((BaseActivity) view).onGenericError();
            } else if (view instanceof BaseFragment) {
                ((BaseFragment) view).onGenericError();
            }
        }
    }
}
