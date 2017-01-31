package com.example.mvppokemon.dagger.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * The qualifier for OkHttp instance without cache
 *
 * @author Slawomir Onyszko
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NonCachedOkHttpClient {
}
