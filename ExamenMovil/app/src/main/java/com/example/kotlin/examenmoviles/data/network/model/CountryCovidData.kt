package com.example.kotlin.examenmoviles.data.network.model

import com.google.gson.annotations.SerializedName

data class CountryCovidData(
    @SerializedName("country") val country: String,
    @SerializedName("region") val region: String,
    @SerializedName("cases") val cases: Map<String, CaseData>
)
