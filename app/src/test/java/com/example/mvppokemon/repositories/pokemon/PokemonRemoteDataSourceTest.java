package com.example.mvppokemon.repositories.pokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRemoteDataSource;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.example.mvppokemon.data.retrofit.PokemonRetrofitInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;

import static junit.framework.Assert.assertEquals;

public class PokemonRemoteDataSourceTest {

    private List<PokemonModel> pokemonList = setUpFakePokemonList();

    private MockWebServer server;

    private ObjectMapper objectMapper = new ObjectMapper();

    // SUT
    private PokemonDataSource pokemonRemoteDataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        server = new MockWebServer();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(server.url("").toString())
                .build();

        PokemonRetrofitInterface pokemonRetrofitInterface = retrofit.create(PokemonRetrofitInterface.class);

        pokemonRemoteDataSource = new PokemonRemoteDataSource(pokemonRetrofitInterface);

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @After
    public void teardown() {
        RxAndroidPlugins.reset();

        try {
            server.close();
        } catch (IOException e) {
            Timber.d(e);
        }
    }

    @Test
    public void getOnePokemonThatExists() {
        PokemonModel pokemonModel = pokemonList.get(0);

        try {
            setFakePokemonForGet(pokemonModel);
        } catch (JsonProcessingException e) {
            Timber.d(e);
        }

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        pokemonRemoteDataSource.getPokemon(pokemonModel.getId())
                .doOnError(throwable -> countDownLatch.countDown())
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

        setNoPokemonAvailable();

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        pokemonRemoteDataSource.getPokemon(pokemonModel.getId())
                .doOnError(throwable -> countDownLatch.countDown())
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }
        observerPokemon.assertError(HttpException.class);
        HttpException exception = (HttpException) observerPokemon.errors().get(0);

        assertEquals(exception.code(), 404);
    }

    @Test
    public void savePokemon() {
        PokemonModel pokemonModel = pokemonList.get(0);

        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        pokemonRemoteDataSource.savePokemon(pokemonModel)
                .doOnError(throwable -> countDownLatch.countDown())
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }
        observerPokemon.assertError(Throwable.class);

        Throwable exception = observerPokemon.errors().get(0);
        assertEquals(exception.getMessage(), PokemonRemoteDataSource.METHOD_NOT_AVAILABLE);
    }

    @Test
    public void getAllPokemonSortedById() {
        TestObserver<PokemonModel> observerPokemon = new TestObserver<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        pokemonRemoteDataSource.getAllLocalPokemonSortedById()
                .doOnError(throwable -> countDownLatch.countDown())
                .doOnComplete(countDownLatch::countDown)
                .subscribe(observerPokemon);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Timber.d(e);
        }
        observerPokemon.assertError(Throwable.class);

        Throwable exception = observerPokemon.errors().get(0);
        assertEquals(exception.getMessage(), PokemonRemoteDataSource.METHOD_NOT_AVAILABLE);
    }

    private void setFakePokemonForGet(PokemonModel pokemonModel) throws JsonProcessingException {

        MockResponse mockResponse = new MockResponse();

        mockResponse.setResponseCode(200)
                .setBody(objectMapper.writeValueAsString(pokemonModel));

        server.enqueue(mockResponse);
    }

    private void setNoPokemonAvailable() {

        MockResponse mockResponse = new MockResponse();

        mockResponse.setResponseCode(404)
                .setBody("{\"detail\":\"Not found.\"}");

        server.enqueue(mockResponse);
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
            list.add(pokemon2);
            list.add(pokemon3);
        } catch (IOException e) {
            Timber.d(e);
        }

        return list;
    }
}
