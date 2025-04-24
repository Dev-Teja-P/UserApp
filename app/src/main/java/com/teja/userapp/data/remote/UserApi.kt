package com.teja.userapp.data.remote

import com.teja.userapp.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("api/")
    suspend fun getUsers(@Query("results") usersCount: Int): UserResponse
}
