package com.example.pp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pp.NavRoutes
import com.example.pp.retrofit.RetrofitViewModel
import com.example.pp.imageDownloader.ImageDownloader
import com.example.pp.imageDownloader.ImageViewModel
import com.example.pp.imageDownloader.ImageViewModelFactory
import com.example.pp.retrofit.MyApi
import com.example.pp.retrofit.RetrofitClient

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

    val viewModel: RetrofitViewModel = viewModel()

    val context = LocalContext.current
    val imageDownloader = remember { ImageDownloader(context) }
    val imageViewModel: ImageViewModel = viewModel(
        factory = ImageViewModelFactory(imageDownloader)
    )

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
        ) { Home(navController, api, viewModel, imageViewModel) }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Main(Modifier)
}
