package com.example.sokodata.ui.navigation

/**
 * Routes de navigation de l'application
 */
sealed class NavigationRoute(val route: String) {
    data object SellerList : NavigationRoute("seller_list")
    data object AddSeller : NavigationRoute("add_seller")
    data class EditSeller(val sellerId: String) : NavigationRoute("edit_seller/{sellerId}") {
        fun createRoute(sellerId: String) = "edit_seller/$sellerId"
    }
}
