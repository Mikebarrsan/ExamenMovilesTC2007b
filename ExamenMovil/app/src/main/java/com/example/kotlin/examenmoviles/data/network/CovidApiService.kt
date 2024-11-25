package com.example.kotlin.examenmoviles.data.network

import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CovidApiService {
    @GET
    suspend fun getCovidData(@Url url:String): Response<List<CountryCovidData>>
}
