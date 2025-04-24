package com.teja.userapp.data.repository

import com.teja.userapp.data.model.User
import com.teja.userapp.data.remote.UserApi
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    suspend fun fetchUsers(usersCount: Int): List<User> {
        return userApi.getUsers(usersCount = usersCount).userList
    }
}
