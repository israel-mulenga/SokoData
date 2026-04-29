package com.example.sokodata.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Seller(
    val id: String? = null,
    val name: String,
    @SerialName("tablenumber") val tableNumber: String,
    val category: String,
    @SerialName("imageurl") val imageUrl: String? = null
)

