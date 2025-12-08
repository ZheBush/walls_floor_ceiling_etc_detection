package com.example.pp.main

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pp.imageDownloader.ImageViewModel
import com.example.pp.imageDownloader.LoadingImage
import com.example.pp.retrofit.responses.HistoryImageResponse
import com.example.pp.ui.theme.Blue64
import com.example.pp.ui.theme.Grey224

@Composable
fun ImageItem(image: HistoryImageResponse, ivm: ImageViewModel) {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Log.d("My Upload Image", image.originalURL)

        LoadingImage(
            url = image.originalURL,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "down arrow"
        )
        LoadingImage(
            url = image.resultURL,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Button(
            onClick = {
                ivm.downloadImage(image.resultURL)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Grey224,
                contentColor = Blue64
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = Blue64
                )
        ) {
            Text(
                text = "Download image",
                fontSize = 14.sp,
                fontWeight = FontWeight(300)
            )
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}