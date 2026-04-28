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
import com.example.sokodata.ui.screens.SellerListScreen
import com.example.sokodata.ui.theme.SokoDataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SokoDataTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    SellerListScreen(
                        onAddSeller = { 
                            // TODO: Naviguer vers l'écran d'ajout
                        },
                        onSellerClick = { seller ->
                            // TODO: Afficher les détails du vendeur
                        },
                        onNavigateToEdit = { seller ->
                            // TODO: Naviguer vers l'écran d'édition
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    SokoDataTheme {
        SellerListScreen()
    }
}
