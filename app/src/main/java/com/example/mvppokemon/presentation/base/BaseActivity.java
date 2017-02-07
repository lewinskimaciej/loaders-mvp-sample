package com.example.mvppokemon.presentation.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mvppokemon.PokemonApplication;
import com.example.mvppokemon.R;
import com.example.mvppokemon.dagger.component.ApplicationComponent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseActivity<P extends BasePresenterInterface<V>, V>
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<P>,
        HttpExceptionResolutionInterface {

    /**
     * Common counter for views (fragments and activities) that is used to generate loader ids
     */
    public static final AtomicInteger viewCounter = new AtomicInteger(0);
    private static final String RECREATION_SAVED_STATE = "recreation_state";
    private static final String LOADER_ID_SAVED_STATE = "loader_id_state";
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
     * Is this the first start of the activity (after onCreate)
     */
    private boolean firstStart;
    /**
     * Unique identifier for the loader, persisted across re-creation
     */
    private int uniqueLoaderIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstStart = savedInstanceState == null || savedInstanceState.getBoolean(RECREATION_SAVED_STATE);
        uniqueLoaderIdentifier = savedInstanceState == null ? BaseActivity.viewCounter.incrementAndGet() : savedInstanceState.getInt(LOADER_ID_SAVED_STATE);

        injectDependencies();

        getSupportLoaderManager().initLoader(uniqueLoaderIdentifier, null, this).startLoading();
    }

    private void injectDependencies() {
        setupComponent(((PokemonApplication) getApplication()).getApplicationComponent());
    }

    @Override
    protected void onStart() {
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
    protected void onStop() {
        if (presenter != null) {
            presenter.onStop();
            presenter.onViewDetached();
        }
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RECREATION_SAVED_STATE, firstStart);
        outState.putInt(LOADER_ID_SAVED_STATE, uniqueLoaderIdentifier);
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, getPresenterFactory());
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

    @Override
    public void onInternalServerError() {
        Toast.makeText(this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotFound() {
        Toast.makeText(this, getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGenericError() {
        Toast.makeText(this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
    }
}
