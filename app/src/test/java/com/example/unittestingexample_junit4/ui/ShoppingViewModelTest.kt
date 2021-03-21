package com.example.unittestingexample_junit4.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.unittestingexample_junit4.MainCoroutineRule
import com.example.unittestingexample_junit4.getOrAwaitValueTest
import com.example.unittestingexample_junit4.repositories.FakeShoppingRepository
import com.example.unittestingexample_junit4.util.Constants
import com.example.unittestingexample_junit4.util.Status
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, returns error`(){
        viewModel.insertShoppingItem("name", "", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too long name, returns error`(){
        val string = buildString {
            for(i in 1..Constants.MAX_NAME_LENGTH + 1){
                append(1)
            }
        }

        viewModel.insertShoppingItem(string, "1", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, returns error`(){
        val string = buildString {
            for(i in 1..Constants.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }

        viewModel.insertShoppingItem("name", "1", string)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`(){

        viewModel.insertShoppingItem("name", "9999999999999999", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`(){

        viewModel.insertShoppingItem("nam", "5", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }


}