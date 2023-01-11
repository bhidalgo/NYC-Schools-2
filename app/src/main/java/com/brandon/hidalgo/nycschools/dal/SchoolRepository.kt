package com.brandon.hidalgo.nycschools.dal

import com.brandon.hidalgo.nycschools.model.SchoolModel
import com.brandon.hidalgo.nycschools.model.ScoresModel

/**
 * A repository for grabbing school data
 */
class SchoolRepository(private val apiClient: ApiClient) {

    /**
     * Get all schools from the API client.
     */
    suspend fun getSchools(): List<SchoolModel> {
        return apiClient.getSchools()
    }

    /**
     * Get all scores from the API client.
     */
    suspend fun getScores(): List<ScoresModel> {
        return apiClient.getScores()
    }

}