package com.example.pp.retrofit.responses

import com.google.gson.annotations.SerializedName

    data class TokenResponse (
        @SerializedName("access_token")
        val token: String,
        @SerializedName("token_type")
        val tokenType: String
    )