package com.example.sokodata.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = SecretConfig.SUPABASE_URL,
        supabaseKey = SecretConfig.SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Storage)
    }
}
