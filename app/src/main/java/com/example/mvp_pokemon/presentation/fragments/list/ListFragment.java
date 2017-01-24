package com.example.mvp_pokemon.presentation.fragments.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

import com.example.mvp_pokemon.R;
import com.example.mvp_pokemon.data.models.PokemonModel;
import com.example.mvp_pokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvp_pokemon.presentation.BaseFragment;
import com.example.mvp_pokemon.presentation.PresenterFactory;
import com.example.mvp_pokemon.dagger.component.ApplicationComponent;
import com.example.mvp_pokemon.presentation.adapters.recycler.PokemonRecyclerViewAdapter;
import com.example.mvp_pokemon.presentation.fragments.list.dagger.ListViewModule;
import com.example.mvp_pokemon.presentation.fragments.list.dagger.DaggerListViewComponent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import javax.inject.Inject;

public final class ListFragment extends BaseFragment<ListPresenter, ListView> implements ListView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    PresenterFactory<ListPresenter> presenterFactory;

    private PokemonRecyclerViewAdapter pokemonRecyclerViewAdapter;
    private Context context;

    // Your presenter is available using the presenter variable

    public ListFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();

        // bundle arguments here

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        context = getActivity();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (presenter != null) {
                presenter.refreshData();
            }
        });

        pokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter(context);
        recyclerView.setAdapter(pokemonRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

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
        DaggerListViewComponent.builder()
                .applicationComponent(parentComponent)
                .listViewModule(new ListViewModule())
                .pokemonRepositoryModule(new PokemonRepositoryModule())
                .build()
                .injectTo(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<ListPresenter> getPresenterFactory() {
        return presenterFactory;
    }

    @Override
    public void setAdapterData(ArrayList<PokemonModel> pokemonList) {
        pokemonRecyclerViewAdapter.setPokemonList(pokemonList);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void addPokemonToAdapter(PokemonModel pokemonModel) {
        pokemonRecyclerViewAdapter.addPokemon(pokemonModel);
    }

    @Override
    public void setLoaderVisibility(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}