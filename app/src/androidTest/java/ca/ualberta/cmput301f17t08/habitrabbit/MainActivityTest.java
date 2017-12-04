package ca.ualberta.cmput301f17t08.habitrabbit;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username_input_field),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("z"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.username_input_field), withText("z"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("zc"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.username_input_field), withText("zc"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.username_input_field), withText("zc"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("zch"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.username_input_field), withText("zch"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.username_input_field), withText("zch"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("zchang"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.username_input_field), withText("zchang"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_button), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

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
