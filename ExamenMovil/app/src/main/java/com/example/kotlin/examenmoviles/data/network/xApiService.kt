package com.example.kotlin.examenmoviles.data.network

import com.example.kotlin.examenmoviles.data.network.model.DogsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface xApiService {
    @GET
    suspend fun getDogsByBreeds(@Url url:String): Response<DogsResponse>
}