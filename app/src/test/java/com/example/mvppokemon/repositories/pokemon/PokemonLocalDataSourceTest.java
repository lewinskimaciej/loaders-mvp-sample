package com.example.mvppokemon.repositories.pokemon;

import android.content.Context;

import com.example.mvppokemon.BuildConfig;
import com.example.mvppokemon.data.models.Models;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.models.StatsModel;
import com.example.mvppokemon.data.repositories.pokemon.PokemonLocalDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import timber.log.Timber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PokemonLocalDataSourceTest {

    private Context context;

    private ReactiveEntityStore<Persistable> dataStore;

    private List<PokemonModel> pokemonList = setUpFakePokemonList();

    // SUT
    private PokemonLocalDataSource pokemonLocalDataSource;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;

        DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, 1);
        source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        source.setLoggingEnabled(true);

        Configuration configuration = source.getConfiguration();
        dataStore = ReactiveSupport.toReactiveStore(
                new EntityDataStore<Persistable>(configuration));

        pokemonLocalDataSource = new PokemonLocalDataSource(dataStore);

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @After
    public void teardown() {
        if (dataStore != null) {
            dataStore.delete(PokemonModel.class);
            dataStore.close();
        }
        RxAndroidPlugins.reset();
    }

    @Test
    public void getOnePokemonThatExists() {
        PokemonModel pokemonModel = pokemonList.get(0);

        insertPokemon(pokemonModel);

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        pokemonLocalDataSource.getPokemon(pokemonModel.getId())
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }
        observerPokemon.assertValue(pokemonModel);
        observerPokemon.assertValueCount(1);
        observerPokemon.assertComplete();
    }

    @Test
    public void failToGetOnePokemonThatDoesNotExist() {
        PokemonModel pokemonModel = pokemonList.get(0);

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        pokemonLocalDataSource.getPokemon(pokemonModel.getId())
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }
        observerPokemon.assertValueCount(0);
        observerPokemon.assertComplete();
    }

    @Test
    public void saveNewPokemon() {
        PokemonModel pokemonModel = pokemonList.get(0);

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        PokemonModel pokemonBefore = getPokemon(pokemonModel.getId());

        // pokemon does not exist yet
        assertNull(pokemonBefore);

        // insert it
        pokemonLocalDataSource.savePokemon(pokemonModel)
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }

        // check if insertion was successful
        observerPokemon.assertValue(pokemonModel);
        observerPokemon.assertValueCount(1);
        observerPokemon.assertComplete();

        PokemonModel pokemonAfter = getPokemon(pokemonModel.getId());
        // check if pokemon was actually inserted
        assertEquals(pokemonModel, pokemonAfter);
    }

    @Test
    public void updateExistingPokemon() {
        PokemonModel pokemonModel = pokemonList.get(0);

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        insertPokemon(pokemonModel);

        // insert it
        pokemonLocalDataSource.savePokemon(pokemonModel)
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }

        // check if insertion was successful
        observerPokemon.assertValue(pokemonModel);
        observerPokemon.assertValueCount(1);
        observerPokemon.assertComplete();

        PokemonModel pokemonAfter = getPokemon(pokemonModel.getId());
        // check if pokemon was actually inserted
        assertEquals(pokemonModel, pokemonAfter);
    }

    @Test
    public void getAllPokemonSortedById() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (PokemonModel pokemonModel : pokemonList) {
            insertPokemon(pokemonModel);
        }

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        // insert it
        pokemonLocalDataSource.getAllLocalPokemonSortedById()
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }

        observerPokemon.assertValueCount(pokemonList.size());
        // test if order is correct
        observerPokemon.assertValues(pokemonList.get(0), pokemonList.get(2), pokemonList.get(1));
        observerPokemon.assertComplete();
    }

    private List<PokemonModel> setUpFakePokemonList() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<PokemonModel> list = new ArrayList<>();

        try {
            PokemonModel pokemon1 = objectMapper.readValue("{\"id\":1,\"name\":\"bulbasaur\",\"sprites\":{\"id\":0,\"pokemonModel\":null,\"front_default\":\"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png\"},\"stats\":[{\"id\":0,\"pokemonModel\":null,\"base_stat\":45,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"speed\",\"url\":\"http://pokeapi.co/api/v2/stat/6/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":65,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"special-defense\",\"url\":\"http://pokeapi.co/api/v2/stat/5/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":65,\"effort\":1,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"special-attack\",\"url\":\"http://pokeapi.co/api/v2/stat/4/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":49,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"defense\",\"url\":\"http://pokeapi.co/api/v2/stat/3/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":49,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"attack\",\"url\":\"http://pokeapi.co/api/v2/stat/2/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":45,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"hp\",\"url\":\"http://pokeapi.co/api/v2/stat/1/\"}}]}\n",
                    PokemonModel.class);
            PokemonModel pokemon2 = objectMapper.readValue("{\"id\":2,\"name\":\"ivysaur\",\"sprites\":{\"id\":0,\"pokemonModel\":null,\"front_default\":\"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png\"},\"stats\":[{\"id\":0,\"pokemonModel\":null,\"base_stat\":60,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"speed\",\"url\":\"http://pokeapi.co/api/v2/stat/6/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":80,\"effort\":1,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"special-defense\",\"url\":\"http://pokeapi.co/api/v2/stat/5/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":80,\"effort\":1,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"special-attack\",\"url\":\"http://pokeapi.co/api/v2/stat/4/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":63,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"defense\",\"url\":\"http://pokeapi.co/api/v2/stat/3/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":62,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"attack\",\"url\":\"http://pokeapi.co/api/v2/stat/2/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":60,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"hp\",\"url\":\"http://pokeapi.co/api/v2/stat/1/\"}}]}\n",
                    PokemonModel.class);
            PokemonModel pokemon3 = objectMapper.readValue("{\"id\":3,\"name\":\"venusaur\",\"sprites\":{\"id\":0,\"pokemonModel\":null,\"front_default\":\"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png\"},\"stats\":[{\"id\":0,\"pokemonModel\":null,\"base_stat\":80,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"speed\",\"url\":\"http://pokeapi.co/api/v2/stat/6/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":100,\"effort\":1,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"special-defense\",\"url\":\"http://pokeapi.co/api/v2/stat/5/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":100,\"effort\":2,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"special-attack\",\"url\":\"http://pokeapi.co/api/v2/stat/4/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":83,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"defense\",\"url\":\"http://pokeapi.co/api/v2/stat/3/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":82,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"attack\",\"url\":\"http://pokeapi.co/api/v2/stat/2/\"}},{\"id\":0,\"pokemonModel\":null,\"base_stat\":80,\"effort\":0,\"stat\":{\"id\":0,\"statsModel\":null,\"name\":\"hp\",\"url\":\"http://pokeapi.co/api/v2/stat/1/\"}}]}",
                    PokemonModel.class);

            // ordered incorrectly to test sorting
            list.add(pokemon1);
            list.add(pokemon3);
            list.add(pokemon2);
        } catch (IOException e) {
            Timber.d(e);
        }

        return list;
    }

    private void insertPokemon(PokemonModel pokemonModel) {
        List<StatsModel> tempList = new ArrayList<>(pokemonModel.getStats());

        pokemonModel.getStats().clear();

        PokemonModel value = dataStore.insert(pokemonModel).blockingGet();

        for (StatsModel statsModel : tempList) {
            pokemonModel.getStats().add(statsModel);
        }
        dataStore.update(value).blockingGet();
        Timber.d("inserted pokemon to test");
    }

    private PokemonModel getPokemon(long pokemonId) {
        return dataStore.findByKey(PokemonModel.class, pokemonId).blockingGet();
    }
}
