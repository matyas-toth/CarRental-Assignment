package com.example.carrentall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.carrentall.ui.screens.CarListScreen
import com.example.carrentall.ui.theme.CarRentallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarRentallTheme {
                CarListScreen()
            }
        }
    }
}