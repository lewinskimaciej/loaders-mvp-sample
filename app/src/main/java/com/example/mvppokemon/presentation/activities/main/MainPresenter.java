package com.example.mvppokemon.presentation.activities.main;

import com.example.mvppokemon.presentation.BasePresenter;

import javax.inject.Inject;

public final class MainPresenter extends BasePresenter<MainView> implements MainPresenterInterface {

    // The view is available using the view variable

    @Inject
    public MainPresenter() {
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);
        // Your code here. Your view is available using view and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, view will be null after this method until next onStart()
        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */
        super.onPresenterDestroyed();
    }
}