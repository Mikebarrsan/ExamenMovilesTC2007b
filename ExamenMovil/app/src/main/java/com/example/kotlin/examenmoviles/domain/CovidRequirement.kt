package com.example.kotlin.examenmoviles.domain

import com.example.kotlin.examenmoviles.data.network.model.CaseData
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import com.example.kotlin.examenmoviles.data.repositories.CovidRepository

class CovidRequirement {

    private val repository = CovidRepository()

    suspend fun fetchCovidData(country: String): List<CountryCovidData> {
        val covidDataList = repository.fetchCovidData(country)

        if (covidDataList.isNotEmpty()) {
            // Process data as per your original logic
            val monthlyData = covidDataList.flatMap { countryData ->
                countryData.cases.map { (date, caseData) ->
                    Pair(date, caseData)
                }
            }.groupBy { (date, _) ->
                // Group by year-month (YYYY-MM)
                date.substring(0, 7)
            }.map { (month, cases) ->
                // Sort dates in the month
                val sortedCases = cases.sortedBy { it.first }

                // Total cases: difference between last and first day of the month
                val totalCases = sortedCases.last().second.total - sortedCases.first().second.total

                // New cases: sum of new cases reported during the month
                val newCases = sortedCases.sumOf { it.second.newCases }

                // Create a `CountryCovidData` object for each month
                CountryCovidData(
                    country = covidDataList[0].country,
                    region = covidDataList[0].region,
                    cases = mapOf(month to CaseData(total = totalCases, newCases = newCases))
                )
            }
            return monthlyData
        } else {
            return emptyList()
        }
    }
}
