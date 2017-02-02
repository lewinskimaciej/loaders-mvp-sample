package com.example.mvppokemon.presentation.adapters.recycler.StatsRecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mvppokemon.R;
import com.example.mvppokemon.data.models.StatsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 02.02.2017.
 *
 * @author Maciej Lewinski
 */

public class StatsHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.stat_name)
    TextView statName;

    @BindView(R.id.stat_base)
    TextView statBase;

    StatsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setStat(StatsModel stat) {
        statName.setText(stat.getStatModel().getName());
        statBase.setText(String.valueOf(stat.getBaseStat()));
    }
}
