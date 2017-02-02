package com.example.mvppokemon.presentation.adapters.recycler.StatsRecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvppokemon.R;
import com.example.mvppokemon.data.models.StatsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created on 12.01.2017.
 *
 * @author Maciej Lewinski
 */

public class StatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StatsModel> stats;

    private Comparator<StatsModel> comparator;

    public StatRecyclerViewAdapter() {
        stats = new ArrayList<>();

        comparator = (statsModel, t1) -> Long.compare(statsModel.getBaseStat(), t1.getBaseStat());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_stat, parent, false);
        return new StatsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StatsHolder statsHolder = (StatsHolder) holder;

        statsHolder.setStat(stats.get(position));
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public void setStats(List<StatsModel> statsList) {
        this.stats = new ArrayList<>(statsList);
        Collections.sort(stats, comparator);
        notifyDataSetChanged();
    }
}
