package com.amt.instasport.util

fun formatToUSPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }

    return if (digits.startsWith("1")) {
        "+$digits"
    } else {
        "+1$digits"
    }
}