package com.example.mvppokemon.presentation.fragments.search;

import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchPresenterTest {

    private List<PokemonModel> pokemonList = setUpFakePokemonList();

    @Mock
    PokemonRepositoryInterface pokemonRepository;

    @Mock
    SearchView searchView;

    private SearchPresenterInterface searchPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());

        searchPresenter = new SearchPresenter(pokemonRepository);
        // attach view to presenter
        searchPresenter.onViewAttached(searchView);
        // simulate start
        searchPresenter.onStart(true);
    }


    @Test
    public void setPokemon() {
        PokemonModel pokemonModel = pokemonList.get(0);

        searchPresenter.setPokemonData(pokemonModel);

        verify(searchView).setPokemon(pokemonModel);
    }

    @Test
    public void initialSetup() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // set pokemon so it remembers the last search when changing configuration
        searchPresenter.setPokemonData(pokemonModel);

        // simulate configuration change
        // and check if presenter sets pokemon
        searchPresenter.onStart(false);

        // pokemon should be set up on view two times, when setting pokemon for the first time
        // and when changing configuration
        verify(searchView, times(2)).setPokemon(pokemonModel);
    }

    @Test
    public void pokemonClicked() {
        PokemonModel pokemonModel = pokemonList.get(0);

        setPokemonToGetFromDataSources(pokemonModel, 2);

        // just to set pokemon as current
        searchPresenter.setPokemonData(pokemonModel);

        // simulate clicking pokemon
        searchPresenter.pokemonClicked();

        //check if proper method was started
        verify(searchView).startPokemonActivity(pokemonModel);
    }

    @Test
    public void getPokemonfromBothDataSources() {
        PokemonModel pokemonModel = pokemonList.get(0);

        setPokemonToGetFromDataSources(pokemonModel, 2);

        searchPresenter.getPokemon((int) pokemonModel.getId());

        verify(pokemonRepository).getPokemon(pokemonModel.getId());
        //set two times, when local data comes, then remote
        verify(searchView, times(2)).setPokemon(pokemonModel);
    }

    @Test
    public void getPokemonfromOneSource() {
        PokemonModel pokemonModel = pokemonList.get(0);

        // one of the sources does not have needed data
        setPokemonToGetFromDataSources(pokemonModel, 1);

        searchPresenter.getPokemon((int) pokemonModel.getId());

        verify(pokemonRepository).getPokemon(pokemonModel.getId());
        //set two times, when local data comes, then remote
        verify(searchView).setPokemon(pokemonModel);
    }

    @Test
    public void changeButtonStateWhenGettingPokemon() {
        PokemonModel pokemonModel = pokemonList.get(0);

        setPokemonToGetFromDataSources(pokemonModel, 2);

        searchPresenter.getPokemon((int) pokemonModel.getId());

        verify(searchView).setButtonEnabled(false);
        // might be called multiple times if data comes from local and remote,
        // or only once if there's no data and only onComplete is called
        verify(searchView, atLeast(1)).setButtonEnabled(true);
    }

    private void setPokemonToGetFromDataSources(PokemonModel pokemonToGet, int numberOfSources) {
        if (pokemonToGet != null) {
            // from local data source and remote data source

            List<PokemonModel> list = new ArrayList<>();
            for (int i = 0; i < numberOfSources; i++) {
                list.add(pokemonToGet);
            }

            when(pokemonRepository.getPokemon(pokemonToGet.getId()))
                    .thenReturn(Observable.fromIterable(list));
        } else {
            when(pokemonRepository.getPokemon(anyLong()))
                    .thenReturn(Observable.empty());
        }
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
