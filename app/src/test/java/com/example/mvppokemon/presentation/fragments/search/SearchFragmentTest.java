package com.example.mvppokemon.presentation.fragments.search;

import android.support.v4.app.Fragment;

import com.example.mvppokemon.BuildConfig;
import com.example.mvppokemon.presentation.activities.main.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SearchFragmentTest {

    // SUT
    private Fragment searchView;

    @Before
    public void setup() {
        searchView = SearchFragment.newInstance();

//        waiting for https://github.com/robolectric/robolectric/issues/1932
//        startFragment(searchView, MainActivity.class);
    }

    @Test
    public void checkIntentStart() {

    }
}
