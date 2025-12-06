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
import androidx.compose.foundation.border
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.pp.NavRoutes
import com.example.pp.R
import com.example.pp.retrofit.RetrofitViewModel
import com.example.pp.imageDownloader.ImageViewModel
import com.example.pp.imageDownloader.LoadingImage
import com.example.pp.retrofit.MyApi
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

@Composable
fun Home(navController: NavHostController, api: MyApi, vm: RetrofitViewModel, ivm: ImageViewModel) {

    val token by vm.token.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey224)
    ) {

        val context = LocalContext.current

        val imageList = remember { mutableStateListOf<HistoryImageResponse>() }
        var currentSize by remember { mutableIntStateOf(0) }
        var isListEmpty by remember { mutableStateOf(false) }
        var isHistoryLoading by remember { mutableStateOf(true) }
        var fromNewToOld by remember { mutableStateOf(true) }

        LaunchedEffect(isHistoryLoading) {
            if (token != null) {
                Log.d("My Image Upload", "token != null")
                val history = withContext(Dispatchers.IO) {
                    api.getHistory("Bearer ${token!!}")
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
                                token = "Bearer ${token!!}"
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
            else {
                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            fromNewToOld = true
                            isHistoryLoading = true
                        },
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.sort_clock_ascending),
                            contentDescription = "sort asc"
                        )
                    }
                    IconButton(
                        onClick = {
                            fromNewToOld = false
                            isHistoryLoading = true
                        },
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.sort_clock_descending),
                            contentDescription = "sort asc"
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth()
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(if (fromNewToOld) imageList else imageList.reversed()) { _, image ->
                        ImageItem(image, ivm)
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
                .padding(
                    start = 24.dp,
                    bottom = 16.dp
                )
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
                .padding(
                    end = 24.dp,
                    bottom = 16.dp
                )
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "add image"
            )
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

fun linkConverter(link: String): String {
    return link.replace("localhost", "192.168.1.100")
}