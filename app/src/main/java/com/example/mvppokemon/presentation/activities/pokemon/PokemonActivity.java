package com.example.mvppokemon.presentation.activities.pokemon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mvppokemon.R;
import com.example.mvppokemon.common.dictionaries.BundleKey;
import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.data.models.StatsModel;
import com.example.mvppokemon.presentation.base.BaseActivity;
import com.example.mvppokemon.presentation.base.PresenterFactory;
import com.example.mvppokemon.presentation.activities.pokemon.dagger.DaggerPokemonViewComponent;
import com.example.mvppokemon.presentation.activities.pokemon.dagger.PokemonViewModule;
import com.example.mvppokemon.presentation.adapters.recycler.StatsRecyclerViewAdapter.StatRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class PokemonActivity extends BaseActivity<PokemonPresenter, PokemonView> implements PokemonView {

    @BindView(R.id.pokemon_name)
    TextView pokemonName;

    @BindView(R.id.pokemon_number)
    TextView pokemonNumber;

    @BindView(R.id.pokemon_sprite)
    ImageView pokemonSprite;

    @BindView(R.id.stats_container)
    RecyclerView statsRecyclerView;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Inject
    PresenterFactory<PokemonPresenter> presenterFactory;

    StatRecyclerViewAdapter statRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setUpActionBar();
        setUpRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupComponent(@NonNull ApplicationComponent parentComponent) {
        DaggerPokemonViewComponent.builder()
                .applicationComponent(parentComponent)
                .pokemonViewModule(new PokemonViewModule())
                .build()
                .injectTo(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<PokemonPresenter> getPresenterFactory() {
        return presenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();

        getIntentData();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setUpRecyclerView() {
        statRecyclerViewAdapter = new StatRecyclerViewAdapter();
        statsRecyclerView.setAdapter(statRecyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        statsRecyclerView.setLayoutManager(linearLayoutManager);
        statRecyclerViewAdapter.setStats(new ArrayList<>());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            long pokemonId = intent.getExtras().getLong(BundleKey.KEY_POKEMON_ID);

            if (presenter != null) {
                presenter.getPokemonId((int) pokemonId);
            }
        }
    }

    @Override
    public void setPokemonSprite(String url) {
        Glide.with(this)
                .load(url)
                .into(pokemonSprite);
    }

    @Override
    public void setPokemonName(String name) {
        pokemonName.setText(name);
    }

    @Override
    public void setPokemonId(int id) {
        pokemonNumber.setText(String.valueOf(id));
    }

    @Override
    public void setPokemonStats(List<StatsModel> stats) {
        statRecyclerViewAdapter.setStats(stats);
    }

    @Override
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
