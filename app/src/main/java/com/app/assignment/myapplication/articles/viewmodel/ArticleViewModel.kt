package com.app.assignment.myapplication.articles.viewmodel

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.app.assignment.myapplication.network.RequestInterface
import com.app.assignment.myapplication.articles.adapter.ArticleAdapter
import com.app.assignment.myapplication.articles.model.response.ArticlesResponseModel
import com.app.assignment.myapplication.base.ViewModelBase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticleViewModel() : ViewModelBase() {

    private var articleList: ArrayList<ArticlesResponseModel>? = null

    init {
        articleList = ArrayList()
    }

    fun loadJSON(rvArticles: RecyclerView) {
        val retrofit = Retrofit.Builder().baseUrl("  https://5e99a9b1bc561b0016af3540.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val requestInterface: RequestInterface = retrofit.create(
            RequestInterface::class.java)
        val call: Call<MutableList<ArticlesResponseModel?>?>? = requestInterface.getJSON()
        call?.enqueue(object : Callback<MutableList<ArticlesResponseModel?>?> {

            override fun onResponse(
                call: Call<MutableList<ArticlesResponseModel?>?>,
                response: retrofit2.Response<MutableList<ArticlesResponseModel?>?>
            ) {
                // Utils.showSnackBar(response.toString(), getDataBinding().rvArticles)
                var articlesResponseModel = ArticlesResponseModel()
                for (position in response.body()!!.indices) {
                    articlesResponseModel = response.body()!![position]!!
                    articleList?.add(articlesResponseModel)
                }

                val articleAdapter = articleList?.let { ArticleAdapter(it) }
                rvArticles.adapter = articleAdapter
            }

            override fun onFailure(call: Call<MutableList<ArticlesResponseModel?>?>, t: Throwable) {
                t.message?.let { Log.e("Error", it) }
            }

        })
    }
}