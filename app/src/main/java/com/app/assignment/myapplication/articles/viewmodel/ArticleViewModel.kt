package com.app.assignment.myapplication.articles.viewmodel

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.assignment.myapplication.articles.adapter.ArticleAdapter
import com.app.assignment.myapplication.articles.model.response.ArticlesResponseModel
import com.app.assignment.myapplication.base.ViewModelBase
import com.app.assignment.myapplication.network.RequestInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticleViewModel() : ViewModelBase() {

    private var articleList: ArrayList<ArticlesResponseModel>? = null

    init {
        articleList = ArrayList()
    }

    // API call method
    fun loadJSON(rvArticles: RecyclerView, txtNoDataFound: TextView) {

        val retrofit = Retrofit.Builder().baseUrl("  https://5e99a9b1bc561b0016af3540.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val requestInterface: RequestInterface = retrofit.create(
            RequestInterface::class.java
        )
        val call: Call<MutableList<ArticlesResponseModel?>?>? = requestInterface.getJSON()
        call?.enqueue(object : Callback<MutableList<ArticlesResponseModel?>?> {

            override fun onResponse(
                call: Call<MutableList<ArticlesResponseModel?>?>,
                response: retrofit2.Response<MutableList<ArticlesResponseModel?>?>
            ) {
                var articlesResponseModel = ArticlesResponseModel()
                for (position in response.body()!!.indices) {
                    articlesResponseModel = response.body()!![position]!!
                    articleList?.add(articlesResponseModel)
                }
                when (articleList.isNullOrEmpty()) {
                    true -> {

                        when (articleList?.isEmpty()) {

                            true -> {
                                rvArticles.visibility = View.GONE
                                txtNoDataFound.visibility = View.VISIBLE
                            }
                        }
                    }
                    false -> {
                        val articleAdapter = articleList?.let { ArticleAdapter(it) }
                        rvArticles.adapter = articleAdapter
                    }
                }

            }

            override fun onFailure(call: Call<MutableList<ArticlesResponseModel?>?>, t: Throwable) {
                t.message?.let { Log.e("Error", it) }
            }

        })
    }
}