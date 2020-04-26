package com.example.sergey.myapplication

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Repository{

    val okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder().build()
    var retrofit = Retrofit.Builder()
        .baseUrl("http://megakohz.bget.ru/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    var service = retrofit.create(GitHubService::class.java)
    //simplified version of the retrofit call that comes from support with coroutines
    //Note that this does NOT handle errors, to be added
    suspend fun getUsers(): Resource<List<User>> {
        try {
            return Resource.success(service.listRepos())
        } catch (e: Exception) {
            return Resource.error(e, null)
        } finally {
        }
    }

    suspend fun getUser(id: Int): User {
        try {
            return service.getUser(id).get(0)
        } catch (e: Exception) {
            return User(0,"","")
        } finally {
        }
    }

}