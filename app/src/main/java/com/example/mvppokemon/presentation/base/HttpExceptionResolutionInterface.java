package com.example.mvppokemon.presentation.base;

/**
 * Created on 27.01.2017.
 *
 * @author Maciej Lewinski
 */

public interface HttpExceptionResolutionInterface {
    void onInternalServerError();

    void onNotFound();

    void onGenericError();
}
