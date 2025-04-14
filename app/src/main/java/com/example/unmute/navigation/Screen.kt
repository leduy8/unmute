package com.example.unmute.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("signin")

    object Greeting : Screen("greeting")

    object Camera : Screen("camera")
}
