package com.asapp.churros

fun Int.toOrdinalNumeral(): String {
    val lastDigit = this % 10
    val suffix = when {
        this in listOf(11, 12, 13) -> "th"
        lastDigit == 1 -> "st"
        lastDigit == 2 -> "nd"
        lastDigit == 3 -> "rd"
        else -> "th"
    }
    return toString() + suffix
}
