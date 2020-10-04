package com.app.assignment.myapplication.articles.model.response

import androidx.databinding.BaseObservable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.google.gson.annotations.SerializedName

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ArticlesResponseModel: BaseObservable(){

    @SerializedName("id")
    var id: String? = null

    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("comments")
    var comments: Int = 0

    @SerializedName("likes")
    var likes: Int = 0

    @SerializedName("media")
    val media: ArrayList<MediaListItem?> = ArrayList()

    @SerializedName("user")
    val user: ArrayList<UserListItem?>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class MediaListItem(

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("blogId")
        val blogId: String? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("image")
        val image: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("url")
        val url: String? = null
    )

    class UserListItem(

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("blogId")
        val blogId: String? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("avatar")
        val avatar: String? = null,

        @SerializedName("lastname")
        val lastname: String? = null,

        @SerializedName("city")
        val city: String? = null,

        @SerializedName("designation")
        val designation: String? = null,

        @SerializedName("about")
        val about: String? = null
    )


}