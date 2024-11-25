package com.example.kotlin.examenmoviles.framework.views.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.examenmoviles.data.network.CovidApiService
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import com.example.kotlin.examenmoviles.databinding.ActivityMainCovidBinding
import com.example.kotlin.examenmoviles.framework.adapters.CovidAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CovidActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainCovidBinding
    private lateinit var adapter: CovidAdapter
    private val countryCovidDataList = mutableListOf<CountryCovidData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainCovidActivity", "onCreate started")
        binding = ActivityMainCovidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        fetchCovidData()
        Log.d("MainCovidActivity", "onCreate completed")
    }

    private fun initRecyclerView() {
        Log.d("MainCovidActivity", "Initializing RecyclerView")
        adapter = CovidAdapter(countryCovidDataList)
        binding.rvCovidData.layoutManager = LinearLayoutManager(this)
        binding.rvCovidData.adapter = adapter
        Log.d("MainCovidActivity", "RecyclerView initialized with adapter: $adapter")
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun fetchCovidData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call: Response<List<CountryCovidData>> = getRetrofit()
                    .create(CovidApiService::class.java)
                    .getCovidData("https://api.api-ninjas.com/v1/covid19")

                val covidDataList: List<CountryCovidData>? = call.body()
                runOnUiThread {
                    if (call.isSuccessful && covidDataList != null) {
                        countryCovidDataList.clear()
                        countryCovidDataList.addAll(covidDataList)
                        adapter.notifyDataSetChanged()
                    } else {
                        showError()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showError()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error al obtener los datos", Toast.LENGTH_SHORT).show()
    }
}
