package com.example.unittestingexample_junit4.data.remote

import com.example.unittestingexample_junit4.BuildConfig
import com.example.unittestingexample_junit4.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
            @Query("q") searchQuery : String,
            @Query("key") apiKey : String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}