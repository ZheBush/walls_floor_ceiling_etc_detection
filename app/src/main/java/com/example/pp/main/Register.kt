package com.example.pp.main

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.pp.NavRoutes
import com.example.pp.retrofit.RetrofitViewModel
import com.example.pp.retrofit.MyApi
import com.example.pp.retrofit.classes.RegisterData
import com.example.pp.ui.theme.Blue64
import com.example.pp.ui.theme.Grey153
import com.example.pp.ui.theme.Grey224
import com.example.pp.ui.theme.Red127
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Register(navController: NavHostController, api: MyApi, vm: RetrofitViewModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue64),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        start = 40.dp,
                        end = 40.dp
                    )
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .height(400.dp)
                    .fillMaxWidth()
                    .background(
                        color = Grey224,
                        shape = RoundedCornerShape(12.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 4.dp
                        ),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    var email by remember { mutableStateOf("") }
                    var fullName by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    var confirmPassword by remember { mutableStateOf("") }

                    val emailFocusReq = remember { FocusRequester() }
                    val fullNameFocusReq = remember { FocusRequester() }
                    val passwordFocusReq = remember { FocusRequester() }
                    val confirmPasswordFocusReq = remember { FocusRequester() }

                    var onEmailFocused by remember { mutableStateOf(false) }
                    var onFullNameFocused by remember { mutableStateOf(false) }
                    var onPasswordFocused by remember { mutableStateOf(false) }
                    var onConfirmPasswordFocused by remember { mutableStateOf(false) }

                    var oldEmail by remember { mutableStateOf("") }
                    var oldFullName by remember { mutableStateOf("") }
                    var oldPassword by remember { mutableStateOf("") }

                    var isButtonClicked by remember { mutableStateOf(false) }
                    var isUserExists by remember { mutableStateOf(false) }
                    val isDataChanged = remember { derivedStateOf {
                        email != oldEmail || fullName != oldFullName || password != oldPassword
                    } }
                    val isAnyFieldEmpty = remember { derivedStateOf { (email == "" || fullName == "" || password == "") } }
                    val arePasswordsSame = remember { derivedStateOf { password == confirmPassword } }
                    val isButtonEnable = remember { derivedStateOf {
                        !isButtonClicked || !isAnyFieldEmpty.value && !isUserExists && arePasswordsSame.value
                                || isDataChanged.value && arePasswordsSame.value
                    } }

                    var isRegisterSuccess by remember { mutableStateOf(false) }
                    var isTokenLoading by remember { mutableStateOf(false) }

                    Text(
                        text = "Create new account",
                        fontWeight = FontWeight(300),
                        fontSize = 20.sp,
                        color = Blue64
                    )
                    TextField(
                        value = email,
                        onValueChange = { newText -> email = newText.trim() },
                        label = {
                            Text(
                                text = "Email",
                                fontWeight = FontWeight(200),
                                fontSize =
                                    if (onEmailFocused || email.isNotEmpty())
                                        11.sp
                                    else
                                        14.sp,
                                color = Grey153
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(300),
                            fontSize = 16.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Grey153,
                            focusedTextColor = Blue64,
                            unfocusedContainerColor = Grey224,
                            focusedContainerColor = Grey224,
                            unfocusedIndicatorColor = Blue64,
                            focusedIndicatorColor = Blue64,
                            cursorColor = Blue64
                        ),
                        modifier = Modifier
                            .onFocusChanged { state ->
                                onEmailFocused = state.isFocused
                            }
                            .focusRequester(emailFocusReq)
                    )
                    TextField(
                        value = fullName,
                        onValueChange = { newText -> fullName = newText.trim() },
                        label = {
                            Text(
                                text = "Name",
                                fontWeight = FontWeight(200),
                                fontSize =
                                    if (onFullNameFocused || fullName.isNotEmpty())
                                        11.sp
                                    else
                                        14.sp,
                                color = Grey153
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(300),
                            fontSize = 16.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Grey153,
                            focusedTextColor = Blue64,
                            unfocusedContainerColor = Grey224,
                            focusedContainerColor = Grey224,
                            unfocusedIndicatorColor = Blue64,
                            focusedIndicatorColor = Blue64,
                            cursorColor = Blue64
                        ),
                        modifier = Modifier
                            .onFocusChanged { state ->
                                onFullNameFocused = state.isFocused
                            }
                            .focusRequester(fullNameFocusReq)
                    )
                    TextField(
                        value = password,
                        onValueChange = { newText -> password = newText.trim() },
                        label = {
                            Text(
                                text = "Password",
                                fontWeight = FontWeight(200),
                                fontSize =
                                    if (onPasswordFocused || password.isNotEmpty())
                                        11.sp
                                    else
                                        14.sp,
                                color = Grey153
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(300),
                            fontSize = 16.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Grey153,
                            focusedTextColor = Blue64,
                            unfocusedContainerColor = Grey224,
                            focusedContainerColor = Grey224,
                            unfocusedIndicatorColor = Blue64,
                            focusedIndicatorColor = Blue64,
                            cursorColor = Blue64
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .onFocusChanged { state ->
                                onPasswordFocused = state.isFocused
                            }
                            .focusRequester(passwordFocusReq)
                    )
                    TextField(
                        value = confirmPassword,
                        onValueChange = { newText -> confirmPassword = newText.trim() },
                        label = {
                            Text(
                                text = "Confirm password",
                                fontWeight = FontWeight(200),
                                fontSize =
                                    if (onConfirmPasswordFocused || password.isNotEmpty())
                                        11.sp
                                    else
                                        14.sp,
                                color = Grey153
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(300),
                            fontSize = 16.sp,
                            color = Blue64
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Grey153,
                            focusedTextColor = Blue64,
                            unfocusedContainerColor = Grey224,
                            focusedContainerColor = Grey224,
                            unfocusedIndicatorColor = Blue64,
                            focusedIndicatorColor = Blue64,
                            cursorColor = Blue64
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .onFocusChanged { state ->
                                onConfirmPasswordFocused = state.isFocused
                            }
                            .focusRequester(confirmPasswordFocusReq)
                    )
                    Button(
                        enabled = isButtonEnable.value,
                        onClick = {
                            isButtonClicked = true
                            if (isButtonEnable.value) {
                                isTokenLoading = true
                                val registerData = RegisterData(
                                    email = email,
                                    password = password,
                                    fullName = fullName
                                )
                                vm.viewModelScope.launch {
                                    withContext(Dispatchers.IO) {
                                        val response = api.register(registerData)
                                        if (response.isSuccessful) {
                                            Log.d("MY Reg", "response is successful")
                                            isRegisterSuccess = true
                                        }
                                        else {
                                            Log.d("My Reg", "${response.errorBody()}")
                                            oldEmail = email
                                            oldFullName = fullName
                                            oldPassword = password
                                            isUserExists = true
                                        }
                                    }
                                    if (isRegisterSuccess) {
                                        val response = api.login(
                                            userName = email,
                                            password = password
                                        )
                                        if (response.isSuccessful) {
                                            val token = response.body()
                                            Log.d("My Login", "token: $token")
                                            vm.setToken(token!!)
                                            Log.d("My Login", "vm token: ${vm.token.value}")
                                            isTokenLoading = false
                                            navController.navigate(NavRoutes.Home.route)
                                        }
                                        else {
                                            Log.d("My Login", response.errorBody().toString())
                                            isTokenLoading = false
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue64,
                            contentColor = Grey224,
                            disabledContainerColor = Grey224,
                            disabledContentColor = Red127
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isButtonEnable.value) Blue64 else Red127,
                        ),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth()
                    ) {
                        if (isTokenLoading) {
                            CircularProgressIndicator(
                                color = Grey224,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        else if (!isButtonEnable.value) {
                            if (isButtonClicked && isAnyFieldEmpty.value) {
                                Text(
                                    text = "Some field is empty",
                                    color = Red127,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(300)
                                )
                            }
                            else if (!arePasswordsSame.value) {
                                Text(
                                    text = "Passwords are different",
                                    color = Red127,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(300)
                                )
                            }
                            else {
                                Text(
                                    text = "User already exists",
                                    color = Red127,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(300)
                                )
                            }
                        }
                        else {
                            Text(
                                text = "Create account",
                                fontSize = 14.sp,
                                fontWeight = FontWeight(300)
                            )
                        }
                    }
                }
            }
            TextButton(
                onClick = {
                    navController.navigate(NavRoutes.Login.route)
                },
            ) {
                Text(
                    text = "I already have an account",
                    color = Grey224,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(300)
                )
            }
        }
    }
}