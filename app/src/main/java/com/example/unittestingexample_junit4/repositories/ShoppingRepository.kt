package com.example.unittestingexample_junit4.repositories

import androidx.lifecycle.LiveData
import com.example.unittestingexample_junit4.data.local.ShoppingItem
import com.example.unittestingexample_junit4.data.remote.responses.ImageResponse
import com.example.unittestingexample_junit4.util.Resource
import retrofit2.Response

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems() : LiveData<List<ShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchForImage(imageQuery : String) : Resource<ImageResponse>
}