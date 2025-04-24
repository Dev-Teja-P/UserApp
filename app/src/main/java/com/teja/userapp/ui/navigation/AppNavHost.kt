package com.teja.userapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teja.userapp.ui.screens.UserDetailsScreen
import com.teja.userapp.ui.screens.UserListScreen
import com.teja.userapp.ui.viewmodel.UserViewModel
import java.net.URLDecoder

@Composable
fun AppNavHost(viewModel: UserViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ListScreen.route
    ) {
        composable(route = Screen.ListScreen.route) {
            UserListScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = Screen.DetailScreen.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                val name = args.getString("name") ?: ""
                val address = args.getString("address") ?: ""
                val image = URLDecoder.decode(args.getString("image"), "UTF-8") ?: ""
                UserDetailsScreen(name = name, address = address, image = image)
            }
        }
    }
}
