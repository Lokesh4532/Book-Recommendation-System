package com.example.bookapp.supabase

import android.content.Context
import android.net.Uri
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.util.UUID

class SupabaseStorageUtils(val context: Context){
    val supabase = createSupabaseClient(
        "https://tdovyupwjgfbkxpyxgxe.supabase.co",
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRkb3Z5dXB3amdmYmt4cHl4Z3hlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzQ0NjYwODYsImV4cCI6MjA1MDA0MjA4Nn0.q376kK-oU4Sb7XIQUc6MTLOJazhL3XP0UlBgQ1pU_5o"
    ){
        install(Storage)
    }

    suspend fun uploadImage(uri: Uri):String? {
        try {
            val extension = uri.path?.substringAfterLast(".") ?: "jpg"
            val fileName = "${UUID.randomUUID()}.$extension"
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            supabase.storage.from(BUCKET_NAME).upload(fileName, inputStream.readBytes())

            val publicUrl = supabase.storage.from(BUCKET_NAME).publicUrl(fileName)
            return publicUrl
        }
        catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }
    companion object{
        const val BUCKET_NAME = "tasty_bites"
    }
}