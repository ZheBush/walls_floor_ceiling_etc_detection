package com.example.pp.retrofit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pp.retrofit.classes.User

class RetrofitViewModel: ViewModel() {

    var user by mutableStateOf<User?>(null)
    val token: MutableLiveData<String> = MutableLiveData()

    fun setToken(newToken: String) {
        token.value = newToken
    }

}