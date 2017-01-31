package com.example.mvppokemon.presentation.fragments.list;

import com.example.mvppokemon.presentation.BasePresenterInterface;

public interface ListPresenterInterface extends BasePresenterInterface<ListView> {

    void refreshData();
}