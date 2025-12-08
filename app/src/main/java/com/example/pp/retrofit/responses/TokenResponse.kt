package com.example.pp.retrofit.responses

import com.google.gson.annotations.SerializedName

    data class TokenResponse (
        @SerializedName("token_type")
        val tokenType: String,
        @SerializedName("access_token")
        val token: String
    )