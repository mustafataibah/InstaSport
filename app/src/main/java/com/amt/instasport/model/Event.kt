package com.amt.instasport.model

data class Event(
    val eventId: String = "",
    val hostUserId: String = "",
    val hostUserName: String = "",
    val title: String = "",
    val sportType: SportsInterest = SportsInterest("", ""),
    val eventLocation: String = "",
    val eventDistance: Double = 0.0,
    val dateTime: String = "",
    val maxParticipants: Int = 0,
    val description: String = "",
    val level: String = "",
)

