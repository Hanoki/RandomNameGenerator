package com.anoki.randomfakenamegenerator.repository

import com.anoki.randomfakenamegenerator.model.Person
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    @GET("/api/?ext")
    fun getPersons(
        @Query("amount") amount: Int = 2, @Query("gender") gender: String = "random", @Query("region") region: String = "random"
    ): Call<List<Person>>

    @GET("/api/?ext")
    fun getPerson(@Query("gender") gender: String = "random", @Query("region") region: String = "random"): Call<Person>
}