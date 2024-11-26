package com.example.kotlin.examenmoviles.data.network.model

import com.google.gson.annotations.SerializedName

data class CaseData(
    @SerializedName("total") val total: Int,
    @SerializedName("new") @JvmField val newCases: Int // "new" es una palabra reservada en Kotlin
)
