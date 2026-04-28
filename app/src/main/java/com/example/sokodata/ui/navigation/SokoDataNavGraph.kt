package com.example.sokodata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sokodata.ui.screens.AddSellerScreen
import com.example.sokodata.ui.screens.EditSellerScreen
import com.example.sokodata.ui.screens.SellerListScreen
import com.example.sokodata.viewmodel.SellerViewModel

/**
 * NavGraph pour gérer la navigation entre les écrans
 */
@Composable
fun SokoDataNavGraph(
    navController: NavHostController,
    viewModel: SellerViewModel = viewModel()
) {
    val sellers = viewModel.sellers.collectAsState().value
    val selectedSeller = viewModel.selectedSeller.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.SellerList.route
    ) {
        // Écran de la liste des vendeurs
        composable(NavigationRoute.SellerList.route) {
            SellerListScreen(
                viewModel = viewModel,
                onAddSeller = {
                    navController.navigate(NavigationRoute.AddSeller.route)
                },
                onNavigateToEdit = { seller ->
                    viewModel.selectSeller(seller)
                    seller.id?.let { id ->
                        navController.navigate(NavigationRoute.EditSeller(id).createRoute(id))
                    }
                }
            )
        }

        // Écran d'ajout de vendeur
        composable(NavigationRoute.AddSeller.route) {
            AddSellerScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Écran d'édition de vendeur
        composable(
            route = "edit_seller/{sellerId}",
            arguments = listOf(
                navArgument("sellerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sellerId = backStackEntry.arguments?.getString("sellerId") ?: return@composable
            val seller = selectedSeller ?: sellers.find { it.id == sellerId }
            
            if (seller != null) {
                EditSellerScreen(
                    seller = seller,
                    viewModel = viewModel,
                    onNavigateBack = {
                        viewModel.clearSelection()
                        navController.popBackStack()
                    },
                    onDelete = { id ->
                        viewModel.deleteSeller(id)
                        viewModel.clearSelection()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

