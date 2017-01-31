package com.example.mvppokemon.presentation.adapters.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mvppokemon.presentation.fragments.list.ListFragment;
import com.example.mvppokemon.presentation.fragments.search.SearchFragment;

/**
 * Created on 10.01.2017.
 *
 * @author Maciej Lewinski
 */
public class MainScreenPagerAdapter extends FragmentPagerAdapter {
    @SuppressWarnings("FieldCanBeLocal")
    private static int NUMBER_OF_ITEMS = 2;

    public MainScreenPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchFragment.newInstance();
            case 1:
                return ListFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_ITEMS;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}
