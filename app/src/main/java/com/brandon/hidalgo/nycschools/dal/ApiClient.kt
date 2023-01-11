package com.brandon.hidalgo.nycschools.dal

import com.brandon.hidalgo.nycschools.model.SchoolModel
import com.brandon.hidalgo.nycschools.model.ScoresModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * A client for the NYC Schools API
 */
interface ApiClient {

    @GET("resource/s3k6-pzi2.json")
    suspend fun getSchools(): List<SchoolModel>

    @GET("resource/f9bf-2cp4.json")
    suspend fun getScores(): List<ScoresModel>

    /**
     * The factory for creating ApiClient instances.
     */
    class Factory {

        companion object {
            const val BASE_URL = "https://data.cityofnewyork.us"
        }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        /**
         * Create a new instance of the ApiClient.
         */
        fun createNewInstance(): ApiClient {
            return retrofit.create(ApiClient::class.java)
        }

    }

}