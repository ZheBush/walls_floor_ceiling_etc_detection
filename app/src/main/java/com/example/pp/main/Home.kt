package com.example.pp.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.pp.NavRoutes
import com.example.pp.R
import com.example.pp.animations.DotCircle
import com.example.pp.imageDownloader.ImageViewModel
import com.example.pp.retrofit.MyApi
import com.example.pp.retrofit.RetrofitViewModel
import com.example.pp.retrofit.responses.HistoryImageResponse
import com.example.pp.ui.theme.Blue64
import com.example.pp.ui.theme.Grey153
import com.example.pp.ui.theme.Grey224
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.math.roundToInt

@Composable
fun Home(navController: NavHostController, api: MyApi, vm: RetrofitViewModel, ivm: ImageViewModel) {

    val token by vm.token.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var floatingElementHeight by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    val screenWidth = LocalWindowInfo.current.containerSize.width.toFloat()
    val screenHeight = LocalWindowInfo.current.containerSize.height.toFloat()

    val density = LocalDensity.current

    val elementSize = 180.dp
    val elementSizePx = with(density) { elementSize.toPx() }

    var offsetX by remember {
        mutableFloatStateOf((screenWidth / 2) - (elementSizePx / 2))
    }
    var offsetY by remember {
        mutableFloatStateOf(screenHeight - elementSizePx + 270)
    }

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = with(LocalDensity.current) { (screenHeight - offsetY).toDp() - 40.dp }
                    )
                    .clip(RoundedCornerShape(16.dp))
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Blue64,
                        contentColor = Grey224,
                        actionOnNewLine = false,
                        actionColor = Grey224
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Grey224)
                .padding(innerPadding)
        ) {

            val context = LocalContext.current

            val imageList = remember { mutableStateListOf<HistoryImageResponse>() }
            var currentSize by remember { mutableIntStateOf(0) }
            var isListEmpty by remember { mutableStateOf(false) }
            var isHistoryLoading by remember { mutableStateOf(true) }
            var fromNewToOld by remember { mutableStateOf(false) }

            LaunchedEffect(isHistoryLoading) {
                if (token != null) {
                    Log.d("My Image Upload", "token != null")
                    val history = withContext(Dispatchers.IO) {
                        api.getHistory("${token!!.tokenType} ${token!!.token}")
                    }
                    history.forEachIndexed { index, img ->
                        if (index >= currentSize) {
                            imageList.add(img)
                        }
                    }
                    currentSize = imageList.size
                    isListEmpty = imageList.isEmpty()
                    isHistoryLoading = false
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
                                    token = "${token!!.tokenType} ${token!!.token}"
                                )

                                Log.d("My Upload Image", "response: ${response.body()}")

                                if (response.isSuccessful) {
                                    val uploadResponse = response.body()
                                    if (uploadResponse!!.status != "done") {
                                        Log.d("My Upload Image", "error")
                                    }
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.d("My Upload Image", "Upload failed: $errorBody")
                                }
                                file.delete()
                            }
                            if (uploadSuccess) {
                                Log.d("My Upload Image", "uploadSuccess")
                                isHistoryLoading = true
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
                else if (isHistoryLoading) {
                    DotCircle(
                        dotCount = 8,
                        circleRadius = 4,
                        dotRadius = 6f,
                        dotColor = Blue64
                    )
                }
                else {
                    Row(
                        modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                fromNewToOld = false
                                isHistoryLoading = true
                            },
                        ) {
                            Image(
                                imageVector =
                                    if (!fromNewToOld) {
                                        ImageVector.vectorResource(R.drawable.sort_clock_descending)
                                    }
                                    else {
                                        ImageVector.vectorResource(R.drawable.sort_clock_ascending_outline)
                                    },
                                contentDescription = "sort desc"
                            )
                        }
                        IconButton(
                            onClick = {
                                fromNewToOld = true
                                isHistoryLoading = true
                            },
                        ) {
                            Image(
                                imageVector =
                                    if (fromNewToOld) {
                                        ImageVector.vectorResource(R.drawable.sort_clock_ascending)
                                    }
                                    else {
                                        ImageVector.vectorResource(R.drawable.sort_clock_descending_outline)
                                    },
                                contentDescription = "sort asc"
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        itemsIndexed(if (fromNewToOld) imageList else imageList.reversed()) { _, image ->
                            ImageItem(image, ivm)
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(elementSize)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = Blue64,
                        shape = RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(NavRoutes.Login.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "log out",
                            tint = Grey224
                        )
                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                val snackbarData = snackbarHostState.showSnackbar(
                                    message = "Green - floor, red - walls, blue - ceiling, yellow - windows, cyan - doors",
                                    withDismissAction = true
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "legend",
                            tint = Grey224
                        )
                    }
                    IconButton(
                        onClick = {
                            openGallery(galleryLauncher)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "add image",
                            tint = Grey224
                        )
                    }
                }
            }
        }
    }
}


fun openGallery(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK).apply {
        setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
    }
    launcher.launch(intent)
}

fun createImagePart(file: File, mimeType: String): MultipartBody.Part {
    val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("file", file.name, requestFile)
}

fun getFileFromUri(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File.createTempFile("upload", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}