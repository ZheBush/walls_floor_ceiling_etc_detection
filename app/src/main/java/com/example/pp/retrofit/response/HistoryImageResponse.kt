package com.example.pp.retrofit.response

import com.google.gson.annotations.SerializedName

data class HistoryImageResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("original_url")
    val originalURL: String,
    @SerializedName("result_url")
    val resultURL: String
)
