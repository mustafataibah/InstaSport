package com.amt.instasport.model

data class User(
    val uid: String = "",
    var name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val followedSports: List<String> = listOf()
)

