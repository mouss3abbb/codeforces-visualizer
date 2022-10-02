package com.example.codeforcesvisualizer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CodeforcesApi {

    @GET("user.info?")
    fun getUser(
        @Query("handles") handle:String
    ): Call<User>
    @GET("user.rating?")
    fun getUserRating(
        @Query("handle") handle: String
    ): Call<UserRating>

    @GET("user.status?")
    fun getUserStatus(
        @Query("handle") handle: String
    ): Call<UserStatus>

}