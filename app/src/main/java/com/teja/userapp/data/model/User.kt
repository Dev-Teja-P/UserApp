package com.teja.userapp.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results")
    val userList: List<User>
)

data class User(
    @SerializedName("name")
    val name: Name,
    @SerializedName("location")
    val location: Location,
    @SerializedName("picture")
    val picture: Picture
)

data class Name(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String
)

data class Location(
    @SerializedName("street")
    val street: Street
)

data class Street(
    @SerializedName("number")
    val number: Int,
    @SerializedName("name")
    val name: String
)

data class Picture(
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("large")
    val large: String
)
