package com.example.mvppokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepository;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import timber.log.Timber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PokemonRepositoryTest {

    @Mock
    PokemonDataSource localDataSource;

    @Mock
    PokemonDataSource remoteDataSource;

    //SUT
    PokemonRepository pokemonRepository;

    // mock values to return from data sources
    List<PokemonModel> pokemonList = setUpFakePokemonList();

    @Before
    public void prepareDatabase() {
        MockitoAnnotations.initMocks(this);

        pokemonRepository = new PokemonRepository(remoteDataSource, localDataSource);
    }

    @Test
    public void getOnePokemonFromLocal() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // local data source has data available
        setOnePokemonAvailable(localDataSource, pokemonModel);
        // remote data source has no data
        setOnePokemonNotAvailable(remoteDataSource);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon((int) pokemonModel.getId())
                .subscribe(observerPokemonFound);

        observerPokemonFound.assertValue(pokemonModel);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void getOnePokemonFromRemote() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // remote data source has data available
        setOnePokemonAvailable(remoteDataSource, pokemonModel);
        // local data source has no data
        setOnePokemonNotAvailable(localDataSource);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon((int) pokemonModel.getId())
                .subscribe(observerPokemonFound);

        observerPokemonFound.assertValue(pokemonModel);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void getOnePokemonFromLocalAndRemote() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // remote data source has data available
        setOnePokemonAvailable(localDataSource, pokemonModel);
        // local data source has data available
        setOnePokemonAvailable(remoteDataSource, pokemonModel);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon((int) pokemonModel.getId())
                .subscribe(observerPokemonFound);

        // returns 2 values, local and remote
        observerPokemonFound.assertValues(pokemonModel, pokemonModel);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void failToGetOnePokemonFromLocalAndRemote() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // remote data source has no data
        setOnePokemonNotAvailable(localDataSource);
        // local data source has no data
        setOnePokemonNotAvailable(remoteDataSource);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon((int) pokemonModel.getId())
                .subscribe(observerPokemonFound);

        // returns no values
        observerPokemonFound.assertValueCount(0);
        observerPokemonFound.assertComplete();
    }

    private void setOnePokemonAvailable(PokemonDataSource dataSource, PokemonModel pokemonModel) {
        when(dataSource.getPokemon((int) pokemonModel.getId())).thenReturn(Observable.just(pokemonModel));
    }

    private void setOnePokemonNotAvailable(PokemonDataSource dataSource) {
        when(dataSource.getPokemon(anyInt())).thenReturn(Observable.empty());
    }

    private void setPokemonAvailable(PokemonDataSource dataSource, List<PokemonModel> list) {
        when(dataSource.getAllLocalPokemon()).thenReturn(Single.just(list));
    }

    private void setPokemonNotAvailable(PokemonDataSource dataSource) {
        when(dataSource.getAllLocalPokemon()).thenReturn(Single.just(new ArrayList<>()));
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

            list.add(pokemon1);
            list.add(pokemon2);
            list.add(pokemon3);
        } catch (IOException e) {
            Timber.d(e);
        }
        return list;
    }
}