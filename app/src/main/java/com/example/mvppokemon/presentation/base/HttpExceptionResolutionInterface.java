package com.example.mvppokemon.presentation.base;

public interface HttpExceptionResolutionInterface {
    void onInternalServerError();

    void onNotFound();

    void onGenericError();
}
