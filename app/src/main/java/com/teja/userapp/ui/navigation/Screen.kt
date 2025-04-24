package com.teja.userapp.ui.navigation

sealed class Screen(val route: String) {
    data object ListScreen : Screen("list")
    data object DetailScreen : Screen("detail/{name}/{address}/{image}") {
        fun createRoute(
            name: String,
            address: String,
            image: String,
        ) =
            "detail/$name/$address/$image"
    }
}
