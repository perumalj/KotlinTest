package com.app.assignment.myapplication.network

import com.app.assignment.myapplication.articles.model.response.ArticlesResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface RequestInterface {
    @GET("jet2/api/v1/blogs?page=1&limit=10")
    fun getJSON(): Call<MutableList<ArticlesResponseModel?>?>?
}