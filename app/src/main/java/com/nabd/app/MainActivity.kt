package com.nabd.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nabd.app.navigation.NabdNavGraph
import com.nabd.app.ui.theme.NABDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NABDTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NabdNavGraph(navController = navController)
                }
            }
        }
    }
}
