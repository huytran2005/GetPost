package com.example.api.Model.Api

import com.example.api.Model.data.post
import retrofit2.Response
import retrofit2.http.GET

interface  ApiService{
    @GET("/posts/")
    suspend fun getPost(): Response<post>
}