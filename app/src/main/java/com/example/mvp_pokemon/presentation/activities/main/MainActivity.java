package com.example.mvp_pokemon.presentation.activities.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.example.mvp_pokemon.R;
import com.example.mvp_pokemon.dagger.component.ApplicationComponent;
import com.example.mvp_pokemon.presentation.BaseActivity;
import com.example.mvp_pokemon.presentation.PresenterFactory;
import com.example.mvp_pokemon.presentation.activities.main.dagger.DaggerMainViewComponent;
import com.example.mvp_pokemon.presentation.activities.main.dagger.MainViewModule;
import com.example.mvp_pokemon.presentation.adapters.pager.MainScreenPagerAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class MainActivity extends BaseActivity<MainPresenter, MainView> implements MainView {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    PagerAdapter viewPagerAdapter;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @Inject
    PresenterFactory<MainPresenter> presenterFactory;
    // Your presenter is available using the presenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        viewPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    hideKeyboard();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
    }

    @Override
    protected void setupComponent(@NonNull ApplicationComponent parentComponent) {
        DaggerMainViewComponent.builder()
                .applicationComponent(parentComponent)
                .mainViewModule(new MainViewModule())
                .build()
                .injectTo(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<MainPresenter> getPresenterFactory() {
        return presenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // presenter variable is available here
    }

}