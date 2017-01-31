package com.example.mvppokemon.presentation.fragments.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mvppokemon.R;
import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvppokemon.presentation.BaseFragment;
import com.example.mvppokemon.presentation.PresenterFactory;
import com.example.mvppokemon.presentation.fragments.search.dagger.DaggerSearchViewComponent;
import com.example.mvppokemon.presentation.fragments.search.dagger.SearchViewModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class SearchFragment extends BaseFragment<SearchPresenter, SearchView> implements SearchView {

    @BindView(R.id.pokemon_name)
    TextView pokemonName;

    @BindView(R.id.pokemon_number)
    TextView pokemonNumber;

    @BindView(R.id.pokemon_sprite)
    ImageView pokemonSprite;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.number_input)
    EditText numberInput;

    @Inject
    PresenterFactory<SearchPresenter> presenterFactory;

    // Your presenter is available using the presenter variable

    public SearchFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();

        // bundle arguments here

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Your code here
        // Do not call presenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull ApplicationComponent parentComponent) {
        DaggerSearchViewComponent.builder()
                .applicationComponent(parentComponent)
                .searchViewModule(new SearchViewModule())
                .pokemonRepositoryModule(new PokemonRepositoryModule())
                .build()
                .injectTo(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<SearchPresenter> getPresenterFactory() {
        return presenterFactory;
    }

    @Override
    public void setPokemonNumber(long number) {
        pokemonNumber.setText(String.valueOf(number));
    }

    @Override
    public void setPokemonName(String name) {
        pokemonName.setText(name);
    }

    @Override
    public void setPokemonSprite(String url) {
        Glide.with(this)
                .load(url)
                .into(pokemonSprite);
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    @OnClick(R.id.button)
    public void getPokemon() {
        if (presenter != null) {
            int number = Integer.parseInt(numberInput.getText().toString());
            presenter.getPokemon(number);
        }
    }
}
