package com.brandon.hidalgo.nycschools.model

import com.google.gson.annotations.SerializedName

/**
 * Data class for School JSON deserialization.
 *
 * TODO: Add more data to build a richer UI.
 */
data class SchoolModel(
    val dbn: String,
    @SerializedName("school_name")
    val schoolName: String,
    val boro: String
)
