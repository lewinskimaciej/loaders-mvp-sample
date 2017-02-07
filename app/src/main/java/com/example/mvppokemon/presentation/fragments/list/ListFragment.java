package com.example.mvppokemon.presentation.fragments.list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mvppokemon.R;
import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvppokemon.presentation.BaseFragment;
import com.example.mvppokemon.presentation.PresenterFactory;
import com.example.mvppokemon.presentation.adapters.recycler.PokemonRecyclerViewAdapter.PokemonRecyclerViewAdapter;
import com.example.mvppokemon.presentation.fragments.list.dagger.DaggerListViewComponent;
import com.example.mvppokemon.presentation.fragments.list.dagger.ListViewModule;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ListFragment extends BaseFragment<ListPresenter, ListView> implements ListView {

    public static final String TAB_NAME = "Downloaded";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    PresenterFactory<ListPresenter> presenterFactory;

    private PokemonRecyclerViewAdapter pokemonRecyclerViewAdapter;

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

        setUpPokemonRecyclerView();

        return view;
    }

    private void setUpPokemonRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (presenter != null) {
                presenter.refreshData();
            }
        });

        Context context = getActivity();

        pokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter(context);
        recyclerView.setAdapter(pokemonRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
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
    public void setElementsInAdapter(List<PokemonModel> pokemonList) {
        pokemonRecyclerViewAdapter.setAdapterData(pokemonList);
    }

    @Override
    public void addElementToAdapter(PokemonModel pokemon) {
        pokemonRecyclerViewAdapter.addPokemon(pokemon);
    }

    @Override
    public void setRefreshing(boolean visibility) {
        swipeRefreshLayout.setRefreshing(visibility);
    }

    @Override
    public Activity getParentActivity() {
        return getActivity();
    }


}
