package com.example.unittestingexample_junit4.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.unittestingexample_junit4.R
import com.example.unittestingexample_junit4.adapters.ImageAdapter
import com.example.unittestingexample_junit4.getOrAwaitValue
import com.example.unittestingexample_junit4.launchFragmentInHiltContainer
import com.example.unittestingexample_junit4.repositories.FakeShoppingRepositoryAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject
import com.google.common.truth.Truth.assertThat
@MediumTest
@HiltAndroidTest
class ImagePickFragmentTest{

    @Inject
    lateinit var fragmentFactory : ShoppingFragmentFactory

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun clickImage_popBackStackAndSetImageUrl(){
        val navController = mock(NavController::class.java)
        val imageUrl = "TEST"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)
            viewModel = testViewModel
        }

        onView(withId(R.id.rvImages)).perform(
                RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                        0,
                        click()
                )
        )

        verify(navController).popBackStack()

        assertThat(testViewModel.currImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)

    }
}