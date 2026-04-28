package com.example.sokodata.model

import kotlinx.serialization.Serializable

@Serializable
data class Seller(
    val id: String? = null,
    val name: String,
    val tableNumber: String,
    val category: String,
    val imageUrl: String? = null
)

