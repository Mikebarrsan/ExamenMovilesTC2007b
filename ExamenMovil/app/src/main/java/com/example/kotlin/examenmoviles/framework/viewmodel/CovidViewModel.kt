package com.example.kotlin.examenmoviles.framework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import com.example.kotlin.examenmoviles.domain.CovidRequirement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CovidViewModel : ViewModel() {

    private val covidRequirement = CovidRequirement()

    private val _covidData = MutableStateFlow<List<CountryCovidData>>(emptyList())
    val covidData: StateFlow<List<CountryCovidData>> get() = _covidData

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchCovidData(country: String) {
        viewModelScope.launch {
            try {
                val data = covidRequirement.fetchCovidData(country)
                _covidData.value = data
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
