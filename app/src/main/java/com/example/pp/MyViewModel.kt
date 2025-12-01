package com.example.pp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pp.retrofit.classes.User
import com.example.pp.retrofit.response.TokenResponse

class MyViewModel: ViewModel() {

    var user by mutableStateOf<User?>(null)
    val token: MutableLiveData<String> = MutableLiveData()

    fun setToken(newToken: String) {
        token.value = newToken
    }

}