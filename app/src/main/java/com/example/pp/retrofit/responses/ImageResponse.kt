package com.example.pp.retrofit.responses

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("url")
    val url: String,
    @SerializedName("status")
    val status: String
)
