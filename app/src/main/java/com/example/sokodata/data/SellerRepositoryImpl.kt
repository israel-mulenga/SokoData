package com.example.sokodata.data

import com.example.sokodata.model.Seller
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import java.util.UUID

class SellerRepositoryImpl : SellerRepository {
    private val client = SupabaseClient.client

    override suspend fun getAllSellers(): List<Seller> {
        return client.postgrest["sellers"]
            .select()
            .decodeList<Seller>()
    }

    override suspend fun addSeller(seller: Seller, imageBytes: ByteArray?): Boolean {
        return try {
            var imageUrl: String? = null

            // 1. Upload image logic
            if (imageBytes != null) {
                val bucket = client.storage.from("seller_images") // Assuming bucket name is "seller_images"
                val fileName = "${UUID.randomUUID()}.jpg"
                bucket.upload(fileName, imageBytes)
                imageUrl = bucket.publicUrl(fileName)
            }

            val sellerToInsert = seller.copy(imageUrl = imageUrl)

            // 2. Insert seller into database with the image URL
            client.postgrest["sellers"].insert(sellerToInsert)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun updateSeller(seller: Seller, imageBytes: ByteArray?): Boolean {
        return try {
            var imageUrl: String? = seller.imageUrl

            if (imageBytes != null) {
                val bucket = client.storage.from("seller_images")
                val fileName = "${UUID.randomUUID()}.jpg"
                bucket.upload(fileName, imageBytes)
                imageUrl = bucket.publicUrl(fileName)
            }

            seller.id?.let { id ->
                val updateData = mapOf(
                    "name" to seller.name,
                    "tablenumber" to seller.tableNumber,
                    "category" to seller.category,
                    "imageurl" to imageUrl
                ).filterValues { it != null }
                
                client.postgrest["sellers"].update(updateData) {
                    filter {
                        eq("id", id)
                    }
                }
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteSeller(sellerId: String): Boolean {
        return try {
            client.postgrest["sellers"].delete {
                filter {
                    eq("id", sellerId)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
