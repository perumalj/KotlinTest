package com.app.assignment.myapplication.articles.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.assignment.myapplication.R
import com.app.assignment.myapplication.articles.viewmodel.ArticleViewModel
import com.app.assignment.myapplication.base.FragmentBase
import com.app.assignment.myapplication.databinding.FragmentArticlesBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ArticleFragment : FragmentBase<ArticleViewModel, FragmentArticlesBinding>() {

    companion object{
        val TAG = "ArticleFragment"
    }

    private var articleViewModel: ArticleViewModel? = null


    override fun getLayoutId(): Int = R.layout.fragment_articles

    override fun setupToolbar() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDataBinding().viewModel = articleViewModel
    }

    override fun initializeScreenVariables() {
        // API call method
        articleViewModel?.loadJSON(getDataBinding().rvArticles,getDataBinding().txtNoDataFound)

    }

    override fun getViewModel(): ArticleViewModel? {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        return articleViewModel
    }







}


