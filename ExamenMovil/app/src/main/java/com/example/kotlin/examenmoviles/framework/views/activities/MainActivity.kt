package com.example.kotlin.examenmoviles.framework.views.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.examenmoviles.data.network.xApiService
import com.example.kotlin.examenmoviles.framework.adapters.DogAdapter
import com.example.kotlin.examenmoviles.data.network.model.DogsResponse
import com.example.kotlin.examenmoviles.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate started")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerView()

        // Llamar a searchByName con un valor inicial
        searchByName("retriever")
        Log.d("MainActivity", "onCreate completed")
    }


    private fun initRecyclerView() {
        Log.d("MainActivity", "Initializing RecyclerView")
        adapter = DogAdapter(dogImages)
        Log.d("MainActivity", "dogImages content at initialization: $dogImages")
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
        Log.d("MainActivity", "RecyclerView initialized with adapter: $adapter")
    }



    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call: Response<DogsResponse> = getRetrofit()
                    .create(xApiService::class.java)
                    .getDogsByBreeds("$query/images")
                val puppies: DogsResponse? = call.body()
                runOnUiThread {
                    if (call.isSuccessful) {
                        val images = puppies?.images ?: emptyList()
                        dogImages.clear()
                        dogImages.addAll(images)
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
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchByName(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}
