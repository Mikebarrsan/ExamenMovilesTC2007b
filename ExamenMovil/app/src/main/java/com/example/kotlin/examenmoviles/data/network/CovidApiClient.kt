package com.example.kotlin.examenmoviles.data.network

import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData

class CovidApiClient {

    private val apiService: CovidApiService = CovidNetworkModule.apiService

    suspend fun fetchCovidData(country: String): List<CountryCovidData> {
        val response = apiService.getCovidData(country)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error fetching data: ${response.code()} ${response.message()}")
        }
    }
}
