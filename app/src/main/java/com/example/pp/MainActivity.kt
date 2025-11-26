package com.example.pp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.pp.retrofit.MyApi
import com.example.pp.retrofit.RetrofitClient
import com.example.pp.retrofit.classes.RegisterData
import com.example.pp.retrofit.classes.User
import com.example.pp.retrofit.response.HistoryImageResponse
import com.example.pp.retrofit.response.TokenResponse
import com.example.pp.retrofit.response.RegisterResponse
import com.example.pp.retrofit.response.UploadImageResponse
import com.example.pp.ui.theme.Blue64
import com.example.pp.ui.theme.Grey153
import com.example.pp.ui.theme.Grey224
import com.example.pp.ui.theme.Red127
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                Main(Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier) {
    val api = RetrofitClient.getInstance().create(MyApi::class.java)
    val navController = rememberNavController()
    val viewModel: MyViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login.route
    ) {
        composable(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(100)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(100)
                )
            },
            route = NavRoutes.Login.route
        ) { Login(navController, api, viewModel) }
        composable(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(100)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(100)
                )
            },
            route = NavRoutes.ForgotPassword.route
        ) { ForgotPassword(navController, api, viewModel) }
        composable(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(100)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(100)
                )
            },
            route = NavRoutes.Register.route
        ) { Register(navController, api, viewModel) }
        composable(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(100)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(100)
                )
            },
            route = NavRoutes.Home.route
        ) { Home(navController, api, viewModel) }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Login(navController: NavHostController, api: MyApi, vm: MyViewModel) {

    val user = vm.user

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
                                color = Grey153
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(200),
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
                                color = Grey153
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight(200),
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
                                onPasswordFocused = state.isFocused
                            }
                            .focusRequester(passwordFocusReq)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {

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

                                val call: Call<TokenResponse>? = api.login(
                                    userName = email,
                                    password = password
                                )

                                call!!.enqueue(object: Callback<TokenResponse?> {

                                    override fun onResponse(
                                        call: Call<TokenResponse?>,
                                        response: Response<TokenResponse?>
                                    ) {

                                        if (!response.isSuccessful) {
                                            val errorBody = response.errorBody()?.string()
                                            Log.e("My Login", "Error body: $errorBody")
                                        }

                                        val token: TokenResponse? = response.body()
                                        vm.setToken(token!!)

                                        Log.d("My Login", "token: ${token.token} tokenType: ${token.tokenType}")
                                    }

                                    override fun onFailure(
                                        call: Call<TokenResponse?>,
                                        t: Throwable
                                    ) {
                                        Log.d("My Reg", "onFailure error ${t.message.toString()}")
                                    }

                                })

                                navController.navigate(NavRoutes.Home.route)

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

@Composable
fun ForgotPassword(navController: NavHostController, api: MyApi, vm: MyViewModel) {

}

@Composable
fun Register(navController: NavHostController, api: MyApi, vm: MyViewModel) {

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

            var hint by remember { mutableStateOf("") }

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
                            top = 8.dp,
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
                            fontWeight = FontWeight(200),
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
                            fontWeight = FontWeight(200),
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
                            fontWeight = FontWeight(200),
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
                            fontWeight = FontWeight(200),
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
                    if (password != confirmPassword) {
                        Text(
                            text = "Passwords are different",
                            color = Red127,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(300)
                        )
                    }
                    Button(
                        onClick = {
                            val data = RegisterData(
                                email = email,
                                password = password,
                                fullName = fullName
                            )
                            val call: Call<RegisterResponse>? = api.register(data)
                            call!!.enqueue(object: Callback<RegisterResponse?> {

                                override fun onResponse(
                                    call: Call<RegisterResponse?>,
                                    response: Response<RegisterResponse?>
                                ) {

                                    Log.d(
                                        "My Reg",
                                        "error text: ${response.message()} error code: ${response.code()}"
                                    )
                                    if (!response.isSuccessful) {
                                        val errorBody = response.errorBody()?.string()
                                        Log.e("My Reg", "Error body: $errorBody")
                                    }

                                    val user: RegisterResponse? = response.body()

                                    vm.user = User(
                                        id = user!!.id,
                                        email = user.email,
                                        password = password,
                                        fullName = user.fullName
                                    )

                                    Log.d("My Reg", "id: ${user.id}, email: ${user.email} fullName: ${user.fullName}")
                                }

                                override fun onFailure(
                                    call: Call<RegisterResponse?>,
                                    t: Throwable
                                ) {
                                    Log.d("My Reg", "onFailure error ${t.message.toString()}")
                                }

                            })

                            navController.navigate(NavRoutes.Home.route)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue64,
                            contentColor = Grey224
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Create account",
                            fontWeight = FontWeight(300),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            TextButton(
                onClick = {
                    navController.navigate(NavRoutes.Login.route)
                },
            ) {
                Text(
                    text = hint,
//                    text = "I already have account",
                    color = Grey224,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(300)
                )
            }
        }
    }
}

@Composable
fun Home(navController: NavHostController, api: MyApi, vm: MyViewModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey224)
    ) {

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        var imageList by remember { mutableStateOf<List<HistoryImageResponse>>(emptyList()) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var imageId by remember {mutableStateOf("")}
        var isListEmpty by remember { mutableStateOf(false) }

        val user = vm.user
        val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1QGV4YW1wbGUuY29tIiwiZXhwIjoxNzY0Mjg0NTkyfQ.Pw5E9pFclY9bHguUz-Lz3bn4SUIU4Fu8qRisM6YofLE"

        LaunchedEffect(Unit) {
            val history = withContext(Dispatchers.IO) {
                api.getHistory(token)
            }
            imageList = history
            if (imageList.isEmpty()) {
                isListEmpty = true
            }
        }

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                result.data?.data?.let { uri ->

                    vm.viewModelScope.launch {
                        val uploadSuccess = withContext(Dispatchers.IO) {
                            val file = getFileFromUri(context, uri)
                            val imagePart = createImagePart(file, "image/jpeg")
                            val response = api.uploadImage(
                                file = imagePart,
                                token = token
                            )
                            if (response.isSuccessful) {
                                val uploadResponse = response.body()
                                imageId = uploadResponse!!.id
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.d("My Upload Image", "Upload failed: $errorBody")
                            }
                            file.delete()
                        }
                        if (uploadSuccess) {
                            Log.d("My Upload Image", "uploadSuccess")
                            val history = withContext(Dispatchers.IO) {
                                api.getHistory(token)
                            }
                            imageList = history
                            Log.d("My Upload Image", "history size: ${history.size}")
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            if (isListEmpty) {
                Text(
                    text = "It's empty here...",
                    color = Grey153,
                    fontSize = 24.sp,
                    fontWeight = FontWeight(300)
                )
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(imageList) { _, image ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Log.d("My Upload Image", image.originalURL)

                            val original = rememberAsyncImagePainter(
                                model = image.originalURL
                            )
                            val result = rememberAsyncImagePainter(
                                model = image.resultURL
                            )

                            AsyncImage(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Fit,
                                model = image.originalURL,
                                contentDescription = "image"
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "down arrow"
                            )
                            LoadingImage(
                                url = image.resultURL,
                                modifier = Modifier
                            )
                            Button(
                                onClick = {

                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue64,
                                    contentColor = Grey224
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .padding(6.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Download image",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(300)
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                navController.navigate(NavRoutes.Login.route)
            },
            containerColor = Blue64,
            contentColor = Grey224,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "log out"
            )
        }
        FloatingActionButton(
            onClick = {
                openGallery(galleryLauncher)
            },
            containerColor = Blue64,
            contentColor = Grey224,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "add image"
            )
        }
    }
}

fun openGallery(launcher: androidx.activity.result.ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK).apply {
        setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
    }
    launcher.launch(intent)
}

fun createImagePart(file: File, mimeType: String): MultipartBody.Part {
    val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("file", file.name, requestFile)
}

private fun getFileFromUri(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File.createTempFile("upload", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Main(Modifier)
}
