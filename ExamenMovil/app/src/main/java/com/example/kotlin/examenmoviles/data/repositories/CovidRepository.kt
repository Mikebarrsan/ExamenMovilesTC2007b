package com.example.kotlin.examenmoviles.data.repositories

import com.example.kotlin.examenmoviles.data.network.CovidApiClient
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData

class CovidRepository {

    private val apiClient = CovidApiClient()

    suspend fun fetchCovidData(country: String): List<CountryCovidData> {
        return apiClient.fetchCovidData(country)
    }
}
