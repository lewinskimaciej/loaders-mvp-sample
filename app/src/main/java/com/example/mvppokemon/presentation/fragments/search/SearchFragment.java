package com.example.mvppokemon.presentation.fragments.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mvppokemon.R;
import com.example.mvppokemon.common.dictionaries.BundleKey;
import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvppokemon.presentation.activities.pokemon.PokemonActivity;
import com.example.mvppokemon.presentation.base.BaseFragment;
import com.example.mvppokemon.presentation.base.PresenterFactory;
import com.example.mvppokemon.presentation.fragments.search.dagger.DaggerSearchViewComponent;
import com.example.mvppokemon.presentation.fragments.search.dagger.SearchViewModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class SearchFragment extends BaseFragment<SearchPresenter, SearchView> implements SearchView {

    public static final String TAB_NAME = "Search";

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

    @BindView(R.id.pokemon_container)
    LinearLayout pokemonContainer;

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
    public void setPokemon(PokemonModel pokemon) {
        pokemonContainer.setVisibility(View.VISIBLE);
        pokemonNumber.setText(String.valueOf(pokemon.getId()));
        pokemonName.setText(pokemon.getName());

        if (pokemon.getSprites() != null) {
            Glide.with(this)
                    .load(pokemon.getSprites().getFrontDefault())
                    .into(pokemonSprite);
        }
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    @Override
    public void startPokemonActivity(PokemonModel pokemonModel) {
        Context context = getActivity();
        Intent intent = new Intent(context, PokemonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(BundleKey.KEY_POKEMON_ID, pokemonModel.getId());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick(R.id.button)
    public void getPokemon() {
        if (presenter != null) {
            int number = Integer.parseInt(numberInput.getText().toString());
            presenter.getPokemon(number);
        }
    }

    @OnClick(R.id.pokemon_container)
    public void onPokemonClick() {
        if (presenter != null) {
            presenter.pokemonClicked();
        }
    }
}
