package com.example.unittestingexample_junit4.di

import android.content.Context
import androidx.room.Room
import com.example.unittestingexample_junit4.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
        .allowMainThreadQueries()
        .build()

}