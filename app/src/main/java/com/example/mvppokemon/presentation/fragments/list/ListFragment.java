package com.example.mvppokemon.presentation.fragments.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvppokemon.R;
import com.example.mvppokemon.common.dictionaries.BundleKey;
import com.example.mvppokemon.dagger.component.ApplicationComponent;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.PokemonRepositoryModule;
import com.example.mvppokemon.presentation.activities.pokemon.PokemonActivity;
import com.example.mvppokemon.presentation.adapters.recycler.PokemonRecyclerViewAdapter.PokemonRecyclerViewAdapter;
import com.example.mvppokemon.presentation.base.BaseFragment;
import com.example.mvppokemon.presentation.base.PresenterFactory;
import com.example.mvppokemon.presentation.events.PokemonClickedEvent;
import com.example.mvppokemon.presentation.fragments.list.dagger.DaggerListViewComponent;
import com.example.mvppokemon.presentation.fragments.list.dagger.ListViewModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
    public void setRefreshing(boolean visibility) {
        swipeRefreshLayout.setRefreshing(visibility);
    }

    @Override
    public void showPokemon(PokemonModel pokemonModel) {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, PokemonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(BundleKey.KEY_POKEMON_ID, pokemonModel.getId());
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPokemonClicked(PokemonClickedEvent event) {
        showPokemon(event.getPokemonModel());
    }
}
