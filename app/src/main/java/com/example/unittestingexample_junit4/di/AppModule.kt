package com.example.unittestingexample_junit4.di

import android.content.Context
import androidx.room.Room
import com.example.unittestingexample_junit4.data.local.ShoppingDao
import com.example.unittestingexample_junit4.util.Constants.BASE_URL
import com.example.unittestingexample_junit4.util.Constants.DATABASE_NAME
import com.example.unittestingexample_junit4.data.local.ShoppingItemDatabase
import com.example.unittestingexample_junit4.data.remote.PixabayAPI
import com.example.unittestingexample_junit4.repositories.DefaultShoppingRepository
import com.example.unittestingexample_junit4.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
            dao: ShoppingDao,
            api: PixabayAPI
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository


    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
            @ApplicationContext context : Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
            database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayAPI() : PixabayAPI{
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(PixabayAPI::class.java)
    }
}