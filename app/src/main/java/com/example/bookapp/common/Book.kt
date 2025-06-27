package com.example.bookapp.common

import android.provider.Telephony.Mms.Rate
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id:String="",
    val title:String="",
    val rate: String="",
    val genre:String="",
    val imageUrl:String="",
    val description:String=""
)