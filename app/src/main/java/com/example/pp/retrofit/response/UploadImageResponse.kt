package com.example.pp.retrofit.response

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: String
)
