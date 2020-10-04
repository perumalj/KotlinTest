package com.app.assignment.myapplication.articles.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.assignment.myapplication.articles.model.response.ArticlesResponseModel
import com.app.assignment.myapplication.databinding.AdapterArticlesItemsBinding


class ArticleAdapter(
   private val articleList: List<ArticlesResponseModel>
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var context: Context? = null

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val adapterArticleItemsBinding = AdapterArticlesItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterArticleItemsBinding)
    }

    override fun getItemCount(): Int {
       return articleList.size

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Adding data from arraylist to data model
        val reviewInfo = articleList[position]
          holder.binding.dataModel = reviewInfo
        holder.binding.dataModelUser = reviewInfo.user?.get(0)
        holder.binding.dataModelMedia = reviewInfo.media?.get(0)

        // Adding image from arraylist and assigning the image to bind view by data binding method
        holder.binding.userImage = reviewInfo.user?.get(0)?.avatar
        holder.binding.articleImage = reviewInfo.media?.get(0)?.image

        holder.binding.txtCommentsCount.text = reviewInfo.comments.toString()
        holder.binding.txtLikeCount.text = reviewInfo.likes.toString()


    }

    //the class is hodling the list view
    class ViewHolder(val binding: AdapterArticlesItemsBinding) :
        RecyclerView.ViewHolder(binding.root)


}