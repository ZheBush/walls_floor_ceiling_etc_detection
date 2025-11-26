package com.example.pp

sealed class NavRoutes(val route: String) {
    object Login: NavRoutes("login")
    object ForgotPassword: NavRoutes("forgotPassword")
    object Register: NavRoutes("register")
    object Home: NavRoutes("home")
}