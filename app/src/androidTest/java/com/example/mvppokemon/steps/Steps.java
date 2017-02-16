package com.example.mvppokemon.steps;

import android.support.annotation.IdRes;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.ViewInteraction;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.And;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@SuppressWarnings("unused")
public class Steps extends GreenCoffeeSteps {

    @When("^I click back once")
    public void clickBack() {
        try {
            pressBack();
        } catch (NoActivityResumedException e) {
            Timber.e(e);
        }
    }

    @When("^I clear \"([^\"]*)\"")
    public void clearInputWithId(String id) throws Throwable {
        onView(withId(resolve(id))).perform(clearText());
    }

    @When("^I click \"([^\"]*)\"")
    public void clickWithId(String buttonId) throws NoSuchFieldException, IllegalAccessException {
        onView(withId(resolve(buttonId))).perform(click());
    }

    @And("^I wait for \"([^\"]*)\" seconds")
    public void waitFor(String secondsAmount) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Thread.sleep(Integer.parseInt(secondsAmount) * 1000);
    }

    @When("^I click button with text \"([^\"]*)\"")
    public void clickButtonWithText(String text) {
        onView(withText(text)).perform(click());
    }

    @When("^I add text \"([^\"]*)\" to \"([^\"]*)\"$")
    public void addText(String text, String id) throws Throwable {
        onView(withId(resolve(id))).perform(click(), clearText(), typeText(text), closeSoftKeyboard());
        Thread.sleep(1000);
    }

    @When("^I add text \"([^\"]*)\" to android id \"([^\"]*)\"$")
    public void addTextToAndroidView(String text, String id) throws Throwable {
        onView(withId(resolveAndroid(id))).perform(click(), clearText(), typeText(text), closeSoftKeyboard());
        Thread.sleep(1000);
    }

    @Then("^I should see \"([^\"]*)\"$")
    public void shouldSeeViewWithText(String text) {
        onView(withText(text)).check(matches(isDisplayed()));
    }

    @Then("^I should see view with id \"([^\"]*)\"")
    public void shouldSeeViewWithId(String viewId) throws Throwable {
        onView(withId(resolve(viewId))).check(matches(isDisplayed()));
    }

    @Then("^I should see \"([^\"]*)\" dialog text")
    public void shouldSeeDialogWithText(String dialog) {
        onView(withText(dialog)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Then("^I should see \"([^\"]*)\" is checked")
    public void shouldSeeCheckedCheckbox(String id) throws Throwable {
        onView(withId(resolve(id))).check(matches(isChecked()));
    }

    @Then("^I should see \"([^\"]*)\" is not checked")
    public void shouldSeeUncheckedCheckbox(String id) throws Throwable {
        onView(withId(resolve(id))).check(matches(isNotChecked()));
    }

    @Then("^(.*) shouldn't be available")
    public void shouldNotBeAvailable(String id) throws Throwable {
        onView(withId(resolve(id))).check(matches(not(isEnabled())));
    }

    @Then("^I shouldn't see view with text \"([^\"]*)\"")
    public void shouldNotSeeViewWithText(String text) {
        onView(withText(text)).check(doesNotExist());
    }

    public static ViewInteraction onEditTextWithinTilWithId(@IdRes int textInputLayoutId) {
        return onView(allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(AppCompatEditText.class)));
    }

    private int resolve(String id) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = com.example.mvppokemon.R.id.class;
        Field field = clazz.getField(id);
        return field.getInt(field);
    }

    private int resolveAndroid(String id) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = android.R.id.class;
        Field field = clazz.getField(id);
        return field.getInt(field);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
