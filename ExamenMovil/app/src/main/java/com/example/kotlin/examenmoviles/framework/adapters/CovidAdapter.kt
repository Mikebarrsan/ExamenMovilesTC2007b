package com.example.kotlin.examenmoviles.framework.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examenmoviles.R
import com.example.kotlin.examenmoviles.framework.adapters.viewholders.CovidViewHolder
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData

class CovidAdapter(private val covidData: List<CountryCovidData>) : RecyclerView.Adapter<CovidViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovidViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CovidViewHolder(layoutInflater.inflate(R.layout.item_covid, parent, false))
    }

    override fun getItemCount(): Int = covidData.size

    override fun onBindViewHolder(holder: CovidViewHolder, position: Int) {
        val item = covidData[position]
        holder.bind(item)
    }
}
