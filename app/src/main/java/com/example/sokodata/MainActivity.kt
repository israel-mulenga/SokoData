package com.example.sokodata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sokodata.ui.navigation.SokoDataNavGraph
import com.example.sokodata.ui.theme.SokoDataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SokoDataTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    SokoDataNavGraph(navController = navController)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    SokoDataTheme {
        val navController = rememberNavController()
        SokoDataNavGraph(navController = navController)
    }
}

