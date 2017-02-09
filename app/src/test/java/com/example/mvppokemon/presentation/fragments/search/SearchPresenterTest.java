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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchPresenterTest {

    private List<PokemonModel> pokemonList = setUpFakePokemonList();

    @Mock
    PokemonRepositoryInterface pokemonRepository;

    @Mock
    SearchView searchView;

    private SearchPresenter searchPresenter;

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
    public void initialSetup() {
        PokemonModel pokemonModel = pokemonList.get(0);

        setPokemonToGet(pokemonModel.getId());
        // find pokemon
        searchPresenter.getPokemon((int) pokemonModel.getId());
        // simulate configuration change
        // and check if presenter sets pokemon
        searchPresenter.onStart(false);

        // pokemon should be set up on view two times, once from local, once from remote
        verify(searchView, times(2)).setPokemonBackgroundVisbility(true);
        verify(searchView, times(2)).setPokemonName(pokemonModel.getName());
        verify(searchView, times(2)).setPokemonNumber(pokemonModel.getId());
        verify(searchView, times(2)).setPokemonSprite(pokemonModel.getSprites().getFrontDefault());
    }

    @Test
    public void pokemonClicked() {
        PokemonModel pokemonModel = pokemonList.get(0);

        setPokemonToGet(pokemonModel.getId());

        // just to set pokemon as current
        searchPresenter.getPokemon((int) pokemonModel.getId());

        // simulate clicking pokemon
        searchPresenter.pokemonClicked();

        verify(searchView).startPokemonActivity(pokemonModel);
    }

    private void setPokemonToGet(long number) {
        PokemonModel pokemon = null;
        for (PokemonModel pokemonModel : pokemonList) {
            if (pokemonModel.getId() == number) {
                pokemon = pokemonModel;
                break;
            }
        }
        if (pokemon != null) {
            when(pokemonRepository.getPokemon(number)).thenReturn(Observable.just(pokemon));
        } else {
            when(pokemonRepository.getPokemon(number)).thenReturn(Observable.empty());
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
