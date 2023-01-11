package com.brandon.hidalgo.nycschools.extensions

/**
 * Converts the String value to an associated borough. If the String does not match the mapping, then the String's value will be returned.
 */
fun String.fullBoroughName(): String {
    return when (this) {
        "M" -> "Manhattan"
        "K" -> "Brooklyn"
        "Q" -> "Queens"
        "X" -> "Bronx"
        "R" -> "Staten Island"
        else -> this
    }
}