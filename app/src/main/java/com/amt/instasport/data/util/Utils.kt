package com.amt.instasport.data.util

fun formatToUSPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }

    return if (digits.startsWith("1")) {
        "+$digits"
    } else {
        "+1$digits"
    }
}