package com.teja.userapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.teja.userapp.ui.navigation.AppNavHost
import com.teja.userapp.ui.theme.UserAppTheme
import com.teja.userapp.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserAppTheme {
                val viewModel: UserViewModel = hiltViewModel()
                AppNavHost(viewModel = viewModel)
            }
        }
    }
}
