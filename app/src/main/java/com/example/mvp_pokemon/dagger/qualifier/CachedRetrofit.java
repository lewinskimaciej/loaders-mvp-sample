package com.example.mvp_pokemon.dagger.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * The qualifier for Retrofit instance with cache
 *
 * @author Sławomir Onyszko
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CachedRetrofit {
}
