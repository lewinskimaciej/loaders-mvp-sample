package com.example.mvp_pokemon.dagger.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mvp_pokemon.PokemonApplication;
import com.example.mvp_pokemon.dagger.module.ApplicationModule;
import com.example.mvp_pokemon.dagger.module.CommonModule;
import com.example.mvp_pokemon.dagger.module.DatabaseModule;
import com.example.mvp_pokemon.dagger.qualifier.AuthenticationInterceptor;
import com.example.mvp_pokemon.dagger.qualifier.CachedOkHttpClient;
import com.example.mvp_pokemon.dagger.qualifier.CachedRetrofit;
import com.example.mvp_pokemon.dagger.qualifier.NonCachedOkHttpClient;
import com.example.mvp_pokemon.dagger.qualifier.NonCachedRetrofit;
import com.google.gson.Gson;

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

    PokemonApplication PokemonApplication();

    Application application();

    SharedPreferences sharedPreferences();

    Cache cache();

    Gson gson();

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