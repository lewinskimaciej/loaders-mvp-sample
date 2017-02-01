package com.example.mvppokemon.dagger.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mvppokemon.dagger.qualifier.AuthenticationInterceptor;
import com.example.mvppokemon.dagger.qualifier.CachedOkHttpClient;
import com.example.mvppokemon.dagger.qualifier.CachedRetrofit;
import com.example.mvppokemon.dagger.qualifier.NonCachedOkHttpClient;
import com.example.mvppokemon.dagger.qualifier.NonCachedRetrofit;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public final class CommonModule {

    public static final int CACHE_SIZE = 10_485_760;    // 10 * 1024 * 1024

    private final String baseUrl;
    private final String apiKey;

    public CommonModule(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = CACHE_SIZE;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    @CachedOkHttpClient
    OkHttpClient provideCachedOkHttpClient(Cache cache, @AuthenticationInterceptor Interceptor authenticationInterceptor, HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder().cache(cache)
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @NonCachedOkHttpClient
    OkHttpClient provideNonCachedOkHttpClient(@AuthenticationInterceptor Interceptor authenticationInterceptor, HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule())
                .writerWithDefaultPrettyPrinter();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    @Singleton
    @NonCachedRetrofit
    Retrofit provideNonCachedRetrofit(ObjectMapper objectMapper, @NonCachedOkHttpClient OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @CachedRetrofit
    Retrofit provideCachedRetrofit(ObjectMapper objectMapper, @CachedOkHttpClient OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @AuthenticationInterceptor
    Interceptor provideAuthenticationInterceptor(SharedPreferences sharedPreferences) {
        return chain -> {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body());

            String userToken = sharedPreferences.getString("UserToken", null);

            if (userToken != null && !userToken.isEmpty()) {
                requestBuilder.header("Authorization", "Token token=" + userToken);
            }

            if (apiKey != null && !apiKey.isEmpty()) {
                requestBuilder.header("X-Api-Key", apiKey);
            }

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

}
