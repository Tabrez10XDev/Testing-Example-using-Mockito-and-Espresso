package com.example.unittestingexample_junit4.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.unittestingexample_junit4.R
import com.example.unittestingexample_junit4.adapters.ShoppingItemAdapter
import com.example.unittestingexample_junit4.data.local.ShoppingItem
import com.example.unittestingexample_junit4.getOrAwaitValue
import com.example.unittestingexample_junit4.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject
import com.google.common.truth.Truth.assertThat



@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Inject
    lateinit var testFragmentFactory: ShoppingFragmentFactoryAndroidTest

    @Test
    fun swipeShoppingItem_deleteItemInDb(){
        val shoppingItem = ShoppingItem("shopping item", 5, 5.5f, "TEST", 1)
        var testViewModel: ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            viewModel?.insertShoppingItemIntoDb(shoppingItem)
        }

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.shoppingItems?.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<ShoppingFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        verify(navController).navigate(
            R.id.action_shoppingFragment_to_addShoppingItemFragment
        )

    }

}