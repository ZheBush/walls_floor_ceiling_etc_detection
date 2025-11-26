package com.example.pp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pp.retrofit.classes.User
import com.example.pp.retrofit.response.TokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyViewModel: ViewModel() {

    var user by mutableStateOf<User?>(null)
    val token: MutableLiveData<TokenResponse> = MutableLiveData()

    fun setToken(newToken: TokenResponse) {
        token.value = newToken
    }

}