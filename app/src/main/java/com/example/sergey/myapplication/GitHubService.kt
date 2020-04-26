package com.example.sergey.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GitHubService {
    @GET("/test_task/test.php")
    suspend  fun listRepos(): List<User>
    @GET("/test_task/test.php")
    suspend  fun getUser(@Query("id")id: Int): List<User>
}