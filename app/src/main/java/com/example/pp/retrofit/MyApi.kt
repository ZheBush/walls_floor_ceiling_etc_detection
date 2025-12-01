package com.example.pp.retrofit

import com.example.pp.retrofit.classes.RegisterData
import com.example.pp.retrofit.response.HistoryImageResponse
import com.example.pp.retrofit.response.TokenResponse
import com.example.pp.retrofit.response.RegisterResponse
import com.example.pp.retrofit.response.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyApi {

    @GET("images/history")
    suspend fun getHistory(
        @Header("Authorization") token: String
    ): List<HistoryImageResponse>

    @POST("auth/register")
    suspend fun register(
        @Body req: RegisterData
    ): retrofit2.Response<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("scope") scope: String? = null,
        @Field("client_id") clientId: String? = null,
        @Field("client_secret") clientSecret: String? = null
    ): retrofit2.Response<TokenResponse>

    @Multipart
    @POST("images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): retrofit2.Response<UploadImageResponse>



}