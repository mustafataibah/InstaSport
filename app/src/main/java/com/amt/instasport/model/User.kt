package com.amt.instasport.model

data class User(
    val uid: String = "",
    val name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val followedSports: List<String> = listOf()
)

