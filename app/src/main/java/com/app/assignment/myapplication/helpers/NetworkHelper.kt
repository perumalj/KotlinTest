package com.app.assignment.myapplication.helpers

import android.content.Context
import com.app.assignment.myapplication.R
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkHelper {

    companion object {

        fun getErrorMessage(context: Context?, error: Throwable): String? {
            if (context != null) {
                return if (error is ConnectionShutdownException || error is UnknownHostException)
                    context.getString(R.string.error_internet)
                else if (error is HttpException && error.code() >= 500 && error.code() < 600)
                    context.getString(R.string.error_server)
                else if (error is SocketTimeoutException || error is SocketException)
                    context.getString(R.string.error_timeout)
                else
                    context.getString(R.string.error_request)
            }
            return null
        }
    }
}