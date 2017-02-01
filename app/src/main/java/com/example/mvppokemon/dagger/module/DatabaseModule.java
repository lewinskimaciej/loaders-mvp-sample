package com.example.mvppokemon.dagger.module;

import android.app.Application;

import com.example.mvppokemon.BuildConfig;
import com.example.mvppokemon.data.models.Models;

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

    public DatabaseModule(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    @Provides
    @Singleton
    ReactiveEntityStore<Persistable> providesDatabase(Application application) {
        DatabaseSource source = new DatabaseSource(application, Models.DEFAULT, schemaVersion);
        ReactiveEntityStore<Persistable> dataStore;
        if (BuildConfig.DEBUG) {
            // use this in development mode to drop and recreate the tables on every upgrade
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            source.setLoggingEnabled(true);
        }
        Configuration configuration = source.getConfiguration();
        dataStore = ReactiveSupport.toReactiveStore(
                new EntityDataStore<Persistable>(configuration));

        return dataStore;
    }

}
