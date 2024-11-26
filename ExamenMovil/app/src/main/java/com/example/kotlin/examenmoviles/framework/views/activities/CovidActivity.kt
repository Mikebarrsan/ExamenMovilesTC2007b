package com.example.kotlin.examenmoviles.framework.views.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.examenmoviles.data.network.CovidApiService
import com.example.kotlin.examenmoviles.data.network.model.CaseData
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import com.example.kotlin.examenmoviles.databinding.ActivityMainCovidBinding
import com.example.kotlin.examenmoviles.framework.adapters.CovidAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            .baseUrl("https://api.api-ninjas.com/v1/") // Asegúrate de que termina con '/'
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun fetchCovidData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("MainCovidActivity", "Iniciando solicitud a la API")
                val covidApiService = getRetrofit().create(CovidApiService::class.java)
                Log.d("MainCovidActivity", "CovidApiService creado: $covidApiService")

                val response = covidApiService.getCovidData("Mexico")
                Log.d("MainCovidActivity", "Respuesta recibida: $response")

                runOnUiThread {
                    if (response.isSuccessful) {
                        val covidDataList = response.body()
                        Log.d("MainCovidActivity", "Datos obtenidos: $covidDataList")

                        if (covidDataList != null && covidDataList.isNotEmpty()) {
                            // Agrupa los datos por mes y calcula correctamente los totales
                            val monthlyData = covidDataList.flatMap { countryData ->
                                countryData.cases.map { (date, caseData) ->
                                    Pair(date, caseData)
                                }
                            }.groupBy { (date, _) ->
                                // Agrupa por año-mes (YYYY-MM)
                                date.substring(0, 7) // Formato: "YYYY-MM"
                            }.map { (month, cases) ->
                                // Ordena las fechas del mes
                                val sortedCases = cases.sortedBy { it.first }

                                // Casos totales: diferencia entre el último día y el primer día del mes
                                val totalCases = sortedCases.last().second.total - sortedCases.first().second.total

                                // Nuevos casos: suma de los "newCases" reportados durante el mes
                                val newCases = sortedCases.sumOf { it.second.newCases }

                                // Crea un objeto `CountryCovidData` para cada mes
                                CountryCovidData(
                                    country = covidDataList[0].country,
                                    region = covidDataList[0].region,
                                    cases = mapOf(month to CaseData(total = totalCases, newCases = newCases))
                                )
                            }

                            countryCovidDataList.clear()
                            countryCovidDataList.addAll(monthlyData)
                            adapter.notifyDataSetChanged()
                            Log.d("MainCovidActivity", "Datos cargados en RecyclerView")
                        } else {
                            Log.d("MainCovidActivity", "La lista de datos está vacía o es nula")
                            showError()
                        }
                    } else {
                        Log.e("MainCovidActivity", "Error en la respuesta: código ${response.code()}, mensaje ${response.message()}")
                        val errorBody = response.errorBody()?.string()
                        Log.e("MainCovidActivity", "Cuerpo del error: $errorBody")
                        showError()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainCovidActivity", "Error al obtener los datos", e)
                runOnUiThread {
                    showError()
                }
            }
        }
    }


    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error al obtener los datos", Toast.LENGTH_SHORT).show()
    }
}
