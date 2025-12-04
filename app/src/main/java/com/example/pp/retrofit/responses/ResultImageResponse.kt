package com.example.pp.retrofit.responses

import com.google.gson.annotations.SerializedName

data class ResultImageResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("url")
    val url: String
)
