package com.example.kotlin.examenmoviles.data.network

import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CovidApiService {
    @GET("covid19")
    @Headers("X-Api-Key: wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf")
    suspend fun getCovidData(@Query("country") country: String): Response<List<CountryCovidData>>
}
