package com.app.assignment.myapplication.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.app.assignment.myapplication.R
import com.app.assignment.myapplication.helpers.DebugLog
import com.app.assignment.myapplication.helpers.Utils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

abstract class ActivityBase<V : ViewModelBase> : AppCompatActivity()
     {

    val viewModel by viewModels<ViewModelBase>()
    var navHostFragment: NavHostFragment? = null
    var mCompositeDisposable = CompositeDisposable()
    var mCustomDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpHideKeyBoard()
        initRxErrorHandler()

    }

    private fun initRxErrorHandler() {
        RxJavaPlugins.setErrorHandler { throwable ->
            if (throwable is UndeliverableException) {
                throwable.cause?.let {
                    Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(
                        Thread.currentThread(),
                        it
                    )
                    return@setErrorHandler
                }
            }
            if (throwable is IOException || throwable is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (throwable is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (throwable is NullPointerException || throwable is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(
                    Thread.currentThread(),
                    throwable
                )
                return@setErrorHandler
            }
            if (throwable is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(
                    Thread.currentThread(),
                    throwable
                )
                return@setErrorHandler
            }
            throwable.message?.let { DebugLog.w(it) }
        }
    }

    private fun setUpHideKeyBoard() {
        viewModel.getHideKeyBoardEvent().observe(this, Observer { hideKeyboard() })
    }

    /**
     * This method is used to hide the keyboard.
     */
    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * This method is used for Navigating from One Screen to Next Screen using Navigation
     * Direction graph.
     * @param navigationId This is the Id of the Navigation Graph Action
     */
    fun navigateToNextScreen(navigationId: Int) {
        try {
            navHostFragment?.findNavController()?.navigate(navigationId)
        } catch (e: Exception) {
            DebugLog.print(e)
        }
    }

    /**
     * This method is used for Navigating from One Screen to Next Screen using Navigation
     * Direction graph.
     * @param action This is the Id of the Navigation Graph Action
     */
    fun navigateToNextScreen(action: NavDirections) {
        try {
            navHostFragment?.findNavController()?.navigate(action)
        } catch (e: Exception) {
            DebugLog.print(e)
        }
    }

    /**
     * This is the Method to initialize the variable at base level for Navigating from Single Class.
     * @param navHostFragment This is the Id of the NavHost Fragment or  FragmentContainerView.
     */
    fun setNavigationHostFragment(navHostFragment: NavHostFragment?) {
        this.navHostFragment = navHostFragment
    }

    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.FirstFragment)
    }



    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
        Utils.enableUserInteraction(this)

    }

}