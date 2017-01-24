package com.example.mvp_pokemon.dagger.module;

import android.app.Application;

import com.example.mvp_pokemon.BuildConfig;
import com.example.mvp_pokemon.data.models.Models;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

@Module
public final class DatabaseModule {

    private final int schemaVersion;
    private final String schemaName;

    public DatabaseModule(int schemaVersion, String schemaName) {
        this.schemaVersion = schemaVersion;
        this.schemaName = schemaName;
    }

    @Provides
    @Singleton
    ReactiveEntityStore<Persistable> providesDatabase(Application application) {
        DatabaseSource source = new DatabaseSource(application, Models.DEFAULT, 1);
        ReactiveEntityStore<Persistable> dataStore;
        if (BuildConfig.DEBUG) {
            // use this in development mode to drop and recreate the tables on every upgrade
            source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS);
            source.setLoggingEnabled(true);
        }
        Configuration configuration = source.getConfiguration();
        dataStore = ReactiveSupport.toReactiveStore(
                new EntityDataStore<Persistable>(configuration));

        return dataStore;
    }

}
