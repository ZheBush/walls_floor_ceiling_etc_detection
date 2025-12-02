package com.example.pp.retrofit.response

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("url")
    val url: String,
    @SerializedName("status")
    val status: String
)
