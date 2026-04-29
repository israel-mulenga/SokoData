package com.example.sokodata.data
import com.example.sokodata.model.Seller

interface SellerRepository {
    suspend fun getAllSellers(): List<Seller>
    suspend fun addSeller(seller: Seller, imageBytes: ByteArray?): Boolean
    suspend fun updateSeller(seller: Seller, imageBytes: ByteArray? = null): Boolean
    suspend fun deleteSeller(sellerId: String): Boolean
}