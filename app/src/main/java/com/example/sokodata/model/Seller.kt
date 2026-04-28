package com.example.sokodata.model;

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
 data class Seller (
  val id: String? = null,
  val name: String,
  val tableNumber: String,
  val category: String,
  val imageUrl: String? = null
)
