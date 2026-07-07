package com.pdm0126.proyectopdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pdm0126.proyectopdm.ui.theme.ProyectoPDMTheme
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.pdm0126.proyectopdm.data.repository.ProductRepository
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ProductRepository(applicationContext)

        lifecycleScope.launch {
            try {

                repository.syncProducts()

                val products = repository.getProductsOnce()

                products.forEach { product ->
                    Log.d("ROOM", product.name)
                }

            } catch (e: Exception) {
                Log.e("ROOM", e.message ?: "Error")
            }
        }

        enableEdgeToEdge()

        setContent {
            ProyectoPDMTheme {

            }
        }
    }
}