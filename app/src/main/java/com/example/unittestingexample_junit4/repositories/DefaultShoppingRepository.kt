package com.example.unittestingexample_junit4.repositories

import androidx.lifecycle.LiveData
import com.example.unittestingexample_junit4.data.local.ShoppingDao
import com.example.unittestingexample_junit4.data.local.ShoppingItem
import com.example.unittestingexample_junit4.data.remote.PixabayAPI
import com.example.unittestingexample_junit4.data.remote.responses.ImageResponse
import com.example.unittestingexample_junit4.util.Resource
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject


class DefaultShoppingRepository @Inject constructor(
        private val shoppingDao: ShoppingDao,
        private val pixabayAPI: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown error occured", null)
            } else{
                Resource.error("An Unknown error occured", null)
            }
        }catch (e : Exception){
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}