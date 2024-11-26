package com.example.kotlin.examenmoviles.framework.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.examenmoviles.data.network.model.CountryCovidData
import com.example.kotlin.examenmoviles.databinding.ActivityMainCovidBinding
import com.example.kotlin.examenmoviles.framework.adapters.CovidAdapter
import com.example.kotlin.examenmoviles.framework.viewmodel.CovidViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CovidActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainCovidBinding
    private lateinit var adapter: CovidAdapter
    private val countryCovidDataList = mutableListOf<CountryCovidData>()

    private val viewModel: CovidViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCovidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        observeViewModel()

        viewModel.fetchCovidData("Mexico")
    }

    private fun initRecyclerView() {
        adapter = CovidAdapter(countryCovidDataList)
        binding.rvCovidData.layoutManager = LinearLayoutManager(this)
        binding.rvCovidData.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.covidData.collectLatest { data ->
                if (data.isNotEmpty()) {
                    countryCovidDataList.clear()
                    countryCovidDataList.addAll(data)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.error.collectLatest { errorMsg ->
                errorMsg?.let {
                    Toast.makeText(this@CovidActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
