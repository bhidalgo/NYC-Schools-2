package com.brandon.hidalgo.nycschools.model

import com.google.gson.annotations.SerializedName

/**
 * Data class for Scores JSON deserialization.
 *
 * TODO Add more data for a richer UI.
 */
data class ScoresModel(
    val dbn: String,
    @SerializedName("sat_critical_reading_avg_score")
    val criticalReadingAverage: String,
    @SerializedName("sat_math_avg_score")
    val mathAverage: String,
    @SerializedName("sat_writing_avg_score")
    val writingAverage: String
)
