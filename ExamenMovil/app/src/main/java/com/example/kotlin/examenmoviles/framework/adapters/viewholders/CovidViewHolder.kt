package com.example.kotlin.examenmoviles.framework.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import com.example.kotlin.examenmoviles.databinding.ItemCovidBinding

class CovidViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCovidBinding.bind(view)

    fun bind(data: CountryCovidData) {
        // Muestra el nombre del país
        binding.tvCountryName.text = data.country

        // Extrae la única entrada del mapa `cases`
        val caseEntry = data.cases.entries.first()
        val month = caseEntry.key // YYYY-MM
        val caseData = caseEntry.value

        // Muestra los datos del mes y casos
        binding.tvTotalCases.text = "Mes: $month\nCasos totales (incremento): ${caseData.total}"
        binding.tvNewCases.text = "Nuevos casos: ${caseData.newCases}"
    }
}
