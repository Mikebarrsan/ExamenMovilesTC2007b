package com.example.kotlin.examenmoviles.framework.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import com.example.kotlin.examenmoviles.databinding.ItemCovidBinding

class CovidViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCovidBinding.bind(view)

    fun bind(data: CountryCovidData) {
        // Asignar el nombre del país
        binding.tvCountryName.text = data.country

        // Mostrar los casos totales (última fecha registrada)
        val latestDate = data.cases.keys.lastOrNull()
        val latestCaseData = latestDate?.let { data.cases[it] }
        binding.tvTotalCases.text = latestCaseData?.total?.toString() ?: "N/A"

        // Mostrar los nuevos casos (última fecha registrada)
        binding.tvNewCases.text = latestCaseData?.newCases?.toString() ?: "N/A"
    }
}
