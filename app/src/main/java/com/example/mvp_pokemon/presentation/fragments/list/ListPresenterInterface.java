package com.example.mvp_pokemon.presentation.fragments.list;

import com.example.mvp_pokemon.presentation.BasePresenterInterface;

public interface ListPresenterInterface extends BasePresenterInterface<ListView> {

    void refreshData();
}