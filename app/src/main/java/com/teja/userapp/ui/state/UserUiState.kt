package com.teja.userapp.ui.state

import com.teja.userapp.data.model.User

sealed class UserUiState {
    data object Loading : UserUiState()
    data class Success(val data: List<User>) : UserUiState()
    data class Error(val message: String) : UserUiState()
}
