package com.example.myapplication

import android.content.pm.ActivityInfo
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    var mainRule = ActivityScenarioRule(MainActivity::class.java)

    //проходим вперед по кнопкам, затем возвращаемся pressBack'ом (навигация назад)

    @Test
    fun testFrom1to3toAboutBack2back1back() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        openAbout()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    // исключительно кнопочки

    @Test
    fun testAllButtons() {
        launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        openAbout()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))
    }

    // навигация вверх

    @Test
    fun testFrom1to3toAboutUp3Up2Up1() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        openAbout1()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))

        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //тестируем различные пути к About + возвращение

    @Test
    fun testFrom1to2back1toAbout() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        openAbout()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun testFrom1to2back1toAbout1() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        openAbout()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun testFrom1to2back1toAbout2() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        openAbout2()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //Для проверки работоспособности кнопок после смены ориентации экрана

    private fun checkButton(id: Int, text: Int) {
        onView(withId(id))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
            .check(matches(withText(text)))
    }

    private fun landscapeOrientation(activityScenario: ActivityScenario<MainActivity>) {
        activityScenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
    }

    private fun portraitOrientation(activityScenario: ActivityScenario<MainActivity>) {
        mainRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
    }

    //Переворачиваем экраны на каждом фрагменте

    @Test
    fun testRotate1() {
        val activityScenario = launchActivity<MainActivity>()

        landscapeOrientation(activityScenario)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        portraitOrientation(activityScenario)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        landscapeOrientation(activityScenario)
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
    }

    @Test
    fun testRotate2Back() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())

        landscapeOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        portraitOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        landscapeOrientation(activityScenario)
        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        landscapeOrientation(activityScenario)

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        landscapeOrientation(activityScenario)

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun testRotate3() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        landscapeOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        portraitOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        landscapeOrientation(activityScenario)
        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
    }

    @Test
    fun testRotateAboutUp() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        openAbout()
        landscapeOrientation(activityScenario)

        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun testRotateAboutBack() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        openAbout()
        landscapeOrientation(activityScenario)

        pressBackUnconditionally()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }
}