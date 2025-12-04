package com.example.pp.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.pp.ui.theme.Blue64
import com.example.pp.ui.theme.Grey153
import com.example.pp.ui.theme.Grey224
import com.example.pp.ui.theme.Red127
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Login(navController: NavHostController, api: MyApi, vm: RetrofitViewModel) {

    val context = LocalContext.current

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
                    .height(350.dp)
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
                            top = 8.dp,
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 4.dp
                        ),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    var email by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    val emailFocusReq = remember { FocusRequester() }
                    val passwordFocusReq = remember { FocusRequester() }
                    var onEmailFocused by remember { mutableStateOf(false) }
                    var onPasswordFocused by remember { mutableStateOf(false) }
                    var isDataCorrect by remember { mutableStateOf(true) }

                    Text(
                        text = "Log in to your account",
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
                                color = if (isDataCorrect) Grey153 else Red127
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(300),
                            fontSize = 16.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = if (isDataCorrect) Blue64 else Red127,
                            focusedTextColor = if (isDataCorrect) Blue64 else Red127,
                            unfocusedContainerColor = Grey224,
                            focusedContainerColor = Grey224,
                            unfocusedIndicatorColor = if (isDataCorrect) Blue64 else Red127,
                            focusedIndicatorColor = if (isDataCorrect) Blue64 else Red127,
                            cursorColor = if (isDataCorrect) Blue64 else Red127
                        ),
                        modifier = Modifier
                            .onFocusChanged { state ->
                                onEmailFocused = state.isFocused
                            }
                            .focusRequester(emailFocusReq)
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
                                color = if (isDataCorrect) Grey153 else Red127
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(300),
                            fontSize = 16.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = if (isDataCorrect) Blue64 else Red127,
                            focusedTextColor = if (isDataCorrect) Blue64 else Red127,
                            unfocusedContainerColor = Grey224,
                            focusedContainerColor = Grey224,
                            unfocusedIndicatorColor = if (isDataCorrect) Blue64 else Red127,
                            focusedIndicatorColor = if (isDataCorrect) Blue64 else Red127,
                            cursorColor = if (isDataCorrect) Blue64 else Red127
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .onFocusChanged { state ->
                                onPasswordFocused = state.isFocused
                            }
                            .focusRequester(passwordFocusReq)
                    )
                    if (!isDataCorrect) {
                        Text(
                            text = "Incorrect login or password",
                            color = Red127,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(300)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                Toast.makeText(context, "Unlucky", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Grey224,
                                contentColor = Blue64
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Blue64
                            )
                        ) {
                            Text(
                                text = "I forgot password",
                                fontWeight = FontWeight(300),
                                fontSize = 14.sp
                            )
                        }
                        Button(
                            onClick = {
                                vm.viewModelScope.launch {
                                    val response = api.login(
                                        userName = email,
                                        password = password
                                    )
                                    if (response.isSuccessful) {
                                        isDataCorrect = true
                                        val token = response.body()
                                        Log.d("My Login", "token: $token")
                                        vm.setToken(token!!.token)
                                        Log.d("My Login", "vm token: ${vm.token.value}")
                                        navController.navigate(NavRoutes.Home.route)
                                    }
                                    else {
                                        isDataCorrect = false
                                        Log.d("My Login", response.errorBody().toString())
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue64,
                                contentColor = Grey224
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Log in",
                                fontWeight = FontWeight(300),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            TextButton(
                onClick = { navController.navigate(NavRoutes.Register.route) },
            ) {
                Text(
                    text = "If you have not account click here",
                    color = Grey224,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(300)
                )
            }
        }
    }
}