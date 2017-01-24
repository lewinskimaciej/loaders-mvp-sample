package com.example.mvp_pokemon.presentation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.mvp_pokemon.PokemonApplication;
import com.example.mvp_pokemon.dagger.component.ApplicationComponent;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseFragment<P extends BasePresenterInterface<V>, V> extends Fragment implements LoaderManager.LoaderCallbacks<P> {

    private final static String RECREATION_SAVED_STATE = "recreation_state";
    private final static String LOADER_ID_SAVED_STATE = "loader_id_state";
    /**
     * Do we need to call {@link #doStart()} from the {@link #onLoadFinished(Loader, BasePresenterInterface)} method.
     * Will be true if presenter wasn't loaded when {@link #onStart()} is reached
     */
    private final AtomicBoolean needToCallStart = new AtomicBoolean(false);
    /**
     * The presenter for this view
     */
    @Nullable
    protected P presenter;
    /**
     * Is this the first start of the fragment (after onCreate)
     */
    private boolean firstStart;
    /**
     * Unique identifier for the loader, persisted across re-creation
     */
    private int uniqueLoaderIdentifier;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstStart = savedInstanceState == null || savedInstanceState.getBoolean(RECREATION_SAVED_STATE);
        uniqueLoaderIdentifier = savedInstanceState == null ? BaseActivity.viewCounter.incrementAndGet() : savedInstanceState.getInt(LOADER_ID_SAVED_STATE);

        injectDependencies();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // See http://stackoverflow.com/a/32289822/2508174 for the use of getActivity().getSupportLoaderManager()
        getActivity().getSupportLoaderManager().initLoader(uniqueLoaderIdentifier, null, this).startLoading();
    }

    private void injectDependencies() {
        setupComponent(((PokemonApplication) getActivity().getApplication()).getApplicationComponent());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter == null) {
            needToCallStart.set(true);
        } else {
            doStart();
        }
    }

    /**
     * Call the presenter callbacks for onStart
     */
    @SuppressWarnings("unchecked")
    private void doStart() {
        assert presenter != null;
        presenter.onViewAttached((V) this);
        presenter.onStart(firstStart);
        firstStart = false;
    }

    @Override
    public void onStop() {
        if (presenter != null) {
            presenter.onStop();
            presenter.onViewDetached();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RECREATION_SAVED_STATE, firstStart);
        outState.putInt(LOADER_ID_SAVED_STATE, uniqueLoaderIdentifier);
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(getActivity(), getPresenterFactory());
    }

    @Override
    public final void onLoadFinished(Loader<P> loader, P presenter) {
        this.presenter = presenter;
        if (needToCallStart.compareAndSet(true, false)) {
            doStart();
        }
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        this.presenter = null;
    }

    /**
     * Get the presenter factory implementation for this view
     *
     * @return the presenter factory
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Setup the injection component for this view
     *
     * @param applicationComponent the app component
     */
    protected abstract void setupComponent(@NonNull ApplicationComponent applicationComponent);
}