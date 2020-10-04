package com.app.assignment.myapplication.base

import android.content.res.Configuration
import androidx.fragment.app.Fragment

abstract class FragmentBaseWrapper : Fragment() {

    abstract fun somethingWentWrong()

    abstract fun noInternet()

    abstract fun onRetryClicked(retryButtonType: String)

    abstract fun dataNotFound(message: String?, messageCode: String?)

    abstract fun verifyUser(message: String)

    abstract fun newVersionFound()


}