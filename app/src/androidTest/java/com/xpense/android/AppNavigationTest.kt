package com.xpense.android

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.xpense.android.data.Transaction
import com.xpense.android.data.TransactionRepository
import com.xpense.android.util.DataBindingIdlingResource
import com.xpense.android.util.EspressoIdlingResource
import com.xpense.android.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    private lateinit var repository: TransactionRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        repository = ServiceLocator.provideTransactionRepository(
            ApplicationProvider.getApplicationContext()
        )
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your idling resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun mainScreen_clickDrawerIcon_OpensNavigation() {
        // Start activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Check drawer closed at startup
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START)))

        // Open drawer by clicking drawer icon
        onView(withContentDescription("Open navigation drawer"))
            .check(matches(isDisplayed()))
            .perform(click())

        // Check drawer open
        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.START)))

        // Press system back button
        pressBack()

        // Check drawer closed
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START)))

        // When using ActivityScenario.launch(), always call close()
        activityScenario.close()
    }

    @Test
    fun transactionScreen_upButton() = runBlocking {
        // Set initial state
        val txn = Transaction(transactionId = 1, amount = 12.34, description = "description")
        repository.saveTransaction(txn)

        // Start activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click list item
        onView(withText("description"))
            .perform(click())

        // Check correct screen displayed
        onView(withText("Transaction"))
            .check(matches(isDisplayed()))

        // Click up button
        onView(withContentDescription("Navigate up"))
            .check(matches(isDisplayed()))
            .perform(click())

        // Check correct screen displayed
        onView(withId(R.id.transaction_list))
            .check(matches(isDisplayed()))

        // When using ActivityScenario.launch(), always call close()
        activityScenario.close()
    }

    @Test
    fun transactionScreen_backButton() = runBlocking {
        // Set initial state
        val txn = Transaction(transactionId = 1, amount = 12.34, description = "description")
        repository.saveTransaction(txn)

        // Start activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click list item
        onView(withText("description"))
            .perform(click())

        // Check correct screen displayed
        onView(withText("Transaction"))
            .check(matches(isDisplayed()))

        // Press system back button
        pressBack()

        // Check correct screen displayed
        onView(withId(R.id.transaction_list))
            .check(matches(isDisplayed()))

        // When using ActivityScenario.launch(), always call close()
        activityScenario.close()
    }
}
