package com.amt.instasport.model

data class Event(
    val eventId: String,
    val hostUserId: String,
    val hostUserName: String,
    val title: String,
    val sportType: SportsInterest,
    val eventLocation: String,
    val eventDistance: Double,
    val dateTime: String,
    val maxParticipants: Int,
    val description: String,
    val level: String,
)