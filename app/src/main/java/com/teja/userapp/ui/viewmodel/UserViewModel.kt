package com.teja.userapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teja.userapp.data.repository.UserRepository
import com.teja.userapp.ui.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _userUiState = MutableLiveData<UserUiState>()
    val userUiState: LiveData<UserUiState> = _userUiState

    fun getUsers(usersCount: Int) {
        _userUiState.value = UserUiState.Loading
        viewModelScope.launch {
            try {
                val users = repository.fetchUsers(usersCount = usersCount)
                if (users.isEmpty()) {
                    _userUiState.value = UserUiState.Error("No Users available")
                }
                _userUiState.value = UserUiState.Success(users)
            } catch (e: Exception) {
                _userUiState.value = UserUiState.Error("Something went wrong.")
            }
        }
    }
}
