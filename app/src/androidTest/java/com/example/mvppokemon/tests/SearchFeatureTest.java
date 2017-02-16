package com.example.mvppokemon.tests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;

import com.example.mvppokemon.presentation.activities.main.MainActivity;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import com.example.mvppokemon.steps.Steps;
import timber.log.Timber;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(Parameterized.class)
public class SearchFeatureTest extends GreenCoffeeTest {

    private static final String FEATURE_TO_TEST = "assets/features/search.feature";

    @Rule
    public ActivityTestRule<? extends Activity> activity = new ActivityTestRule<>(MainActivity.class);

    @SuppressLint("CommitPrefEdits")
    @After
    public void teardown() {
        Timber.d("--------------------teardown----------------------");
        getInstrumentation().getTargetContext()
                .getSharedPreferences("PrivateStorageUtilsTest", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();
    }

    public SearchFeatureTest(Scenario scenario) {
        super(scenario);
    }

    @Parameterized.Parameters
    public static Iterable<Scenario> scenarios() throws IOException {
        return new GreenCoffeeConfig()
                .withFeatureFromAssets(FEATURE_TO_TEST)
                .scenarios();
    }

    @Test
    public void test() {
        start(new Steps());
    }
}
