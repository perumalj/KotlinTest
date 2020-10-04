package com.app.assignment.myapplication.bind


import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.app.assignment.myapplication.R
import com.app.assignment.myapplication.helpers.DebugLog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

class BindAdapters {

    companion object {

        @BindingAdapter(value = ["bind:image"], requireAll = true)
        @JvmStatic
        fun bindImage(imageView: ImageView, url: String?) {
            if (url.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load(R.drawable.ic_placeholder)
                    .into(imageView)
                return
            } else {
                if ((url.contains("https://") || url.contains("http://"))) {
                    DebugLog.i("image url ==> $url")
                    Glide.with(imageView.context)
                        .load(url)
                        .placeholder(R.drawable.ic_placeholder)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                                return true
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                imageView.setImageDrawable(resource)
                                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                                return true
                            }
                        })
                        .into(imageView)
                } else {
                    Glide.with(imageView.context)
                        .load(File(url))
                        .placeholder(R.drawable.ic_placeholder)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                                return true
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                imageView.setImageDrawable(resource)
                                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                                return true
                            }
                        })
                        .into(imageView)
                }
            }
        }




    }
}