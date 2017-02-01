package com.example.mvppokemon.dagger.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mvppokemon.PokemonApplication;
import com.example.mvppokemon.dagger.module.ApplicationModule;
import com.example.mvppokemon.dagger.module.CommonModule;
import com.example.mvppokemon.dagger.module.DatabaseModule;
import com.example.mvppokemon.dagger.qualifier.AuthenticationInterceptor;
import com.example.mvppokemon.dagger.qualifier.CachedOkHttpClient;
import com.example.mvppokemon.dagger.qualifier.CachedRetrofit;
import com.example.mvppokemon.dagger.qualifier.NonCachedOkHttpClient;
import com.example.mvppokemon.dagger.qualifier.NonCachedRetrofit;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import dagger.Component;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class, CommonModule.class, DatabaseModule.class})
public interface ApplicationComponent {

    Context context();

    PokemonApplication pokemonApplication();

    Application application();

    SharedPreferences sharedPreferences();

    Cache cache();

    ObjectMapper objectMapper();

    @AuthenticationInterceptor
    Interceptor authenticationInterceptor();

    @CachedOkHttpClient
    OkHttpClient cachedOkHttpClient();

    @NonCachedOkHttpClient
    OkHttpClient nonCachedOkHttpClient();

    @CachedRetrofit
    Retrofit cachedRetrofit();

    @NonCachedRetrofit
    Retrofit nonCachedRetrofit();

    ReactiveEntityStore<Persistable> database();
}
