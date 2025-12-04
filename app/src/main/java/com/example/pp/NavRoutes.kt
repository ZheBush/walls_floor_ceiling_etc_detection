package com.example.pp

sealed class NavRoutes(val route: String) {
    object Login: NavRoutes("login")
    object Register: NavRoutes("register")
    object Home: NavRoutes("home")
}