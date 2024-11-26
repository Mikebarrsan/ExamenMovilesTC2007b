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
        val date = caseEntry.key
        val caseData = caseEntry.value

        // Muestra los datos de la fecha y casos
        binding.tvTotalCases.text = "Fecha: $date\nTotal casos: ${caseData.total}"
        binding.tvNewCases.text = "Nuevos casos: ${caseData.newCases}"
    }
}
