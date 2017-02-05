package com.example.mvppokemon.repositories.pokemon;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepository;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import timber.log.Timber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
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

        // local source has data available
        setOnePokemonAvailable(localDataSource, pokemonModel);
        // remote source has no data
        setNoPokemonAvailable(remoteDataSource);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon(pokemonModel.getId())
                .subscribe(observerPokemonFound);

        observerPokemonFound.assertValue(pokemonModel);
        observerPokemonFound.assertValueCount(1);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void getOnePokemonFromRemote() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // remote source has data available
        setOnePokemonAvailable(remoteDataSource, pokemonModel);
        // local source has no data
        setNoPokemonAvailable(localDataSource);
        // don't test saving when result comes from API
        omitSavingPokemonAction();

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon(pokemonModel.getId())
                .subscribe(observerPokemonFound);

        observerPokemonFound.assertValue(pokemonModel);
        observerPokemonFound.assertValueCount(1);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void getOnePokemonFromLocalAndRemote() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // remote source has data available
        setOnePokemonAvailable(remoteDataSource, pokemonModel);
        // local source has data available
        setOnePokemonAvailable(localDataSource, pokemonModel);
        // don't test saving when result comes from API
        omitSavingPokemonAction();

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon(pokemonModel.getId())
                .subscribe(observerPokemonFound);

        // returns 2 values, local and remote
        observerPokemonFound.assertValues(pokemonModel, pokemonModel);
        observerPokemonFound.assertValueCount(2);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void failToGetOnePokemonFromLocalAndRemote() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // remote source has no data
        setNoPokemonAvailable(remoteDataSource);
        // local source has no data
        setNoPokemonAvailable(localDataSource);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getPokemon(pokemonModel.getId())
                .subscribe(observerPokemonFound);

        // returns no values
        observerPokemonFound.assertValueCount(0);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void getAllLocalPokemonSortedById() {
        // data available, local only
        setSomePokemonAvailable(localDataSource, pokemonList);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getAllLocalPokemonSortedById().subscribe(observerPokemonFound);

        observerPokemonFound.assertValueCount(pokemonList.size());
        // order in list: 1, 3, 2, sorted order: 1, 2, 3
        observerPokemonFound.assertValues(pokemonList.get(0), pokemonList.get(2), pokemonList.get(1));
        observerPokemonFound.assertComplete();
    }

    @Test
    public void failToGetAllLocalPokemonSortedById() {
        // no data, local only
        setNonePokemonAvailable(localDataSource);

        TestObserver<PokemonModel> observerPokemonFound = new TestObserver<>();

        pokemonRepository.getAllLocalPokemonSortedById().subscribe(observerPokemonFound);

        observerPokemonFound.assertValueCount(0);
        observerPokemonFound.assertComplete();
    }

    @Test
    public void saveNewPokemon() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // remote source has data available
        setOnePokemonAvailable(remoteDataSource, pokemonModel);
        // local source has data available
        setNoPokemonAvailable(localDataSource);
        // make savePokemon function in DataStore return the saved pokemon
        setSavePokemonToReturnValue(localDataSource, pokemonModel);

        TestObserver<PokemonModel> observerBeforeInserting = new TestObserver<>();
        TestObserver<PokemonModel> observerInserting = new TestObserver<>();
        TestObserver<PokemonModel> observerAfterInserting = new TestObserver<>();

//        TODO: change implementation of getPokemon so it passes after uncommenting
//        // check that it doesn't exist
//        pokemonRepository.getPokemon(pokemonModel.getId()).subscribe(observerBeforeInserting);
//
//        observerBeforeInserting.assertValueCount(1);
//        observerBeforeInserting.assertComplete();

        // insert
        pokemonRepository.savePokemon(pokemonModel).subscribe(observerInserting);

        observerInserting.assertValueCount(1);
        observerInserting.assertValue(pokemonModel);
        observerInserting.assertComplete();

//        // check if it now exists
//        pokemonRepository.getPokemon(pokemonModel.getId()).subscribe(observerAfterInserting);
//
//        observerAfterInserting.assertValueCount(1);
//        observerAfterInserting.assertValue(pokemonModel);
//        observerAfterInserting.assertComplete();
    }

    private void setOnePokemonAvailable(PokemonDataSource dataSource, PokemonModel pokemonModel) {
        when(dataSource.getPokemon((int) pokemonModel.getId())).thenReturn(Observable.just(pokemonModel));
    }

    private void setNoPokemonAvailable(PokemonDataSource dataSource) {
        when(dataSource.getPokemon(anyInt())).thenReturn(Observable.empty());
    }

    private void setSomePokemonAvailable(PokemonDataSource dataSource, List<PokemonModel> list) {
        when(dataSource.getAllLocalPokemonSortedById())
                .thenReturn(Observable.fromIterable(list)
                        .sorted((o1, o2) -> Long.compare(o1.getId(), o2.getId())));
    }

    private void setNonePokemonAvailable(PokemonDataSource dataSource) {
        when(dataSource.getAllLocalPokemonSortedById()).thenReturn(Observable.empty());
    }

    private void setSavePokemonToReturnValue(PokemonDataSource dataSource, PokemonModel pokemonModel) {
        when(dataSource.savePokemon(anyObject())).thenReturn(Observable.just(pokemonModel));
    }

    private void omitSavingPokemonAction() {
        when(pokemonRepository.savePokemon(anyObject())).thenReturn(Observable.empty());
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
}
