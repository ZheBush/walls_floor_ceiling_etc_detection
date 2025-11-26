package com.example.pp.retrofit.response

import com.google.gson.annotations.SerializedName

data class ResultImageResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("url")
    val url: String
)
