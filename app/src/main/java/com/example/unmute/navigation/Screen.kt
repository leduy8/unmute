package com.example.unmute.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")

    object Camera : Screen("camera")
}
