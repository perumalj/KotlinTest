package com.app.assignment.myapplication.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.speech.RecognizerIntent
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.app.assignment.myapplication.MainActivity
import com.app.assignment.myapplication.R
import com.google.android.material.snackbar.Snackbar
import java.security.NoSuchAlgorithmException

class Utils {

    companion object {

        @JvmStatic
        val screenWidth = 750 //Resources.getSystem().displayMetrics.widthPixels

        @JvmStatic
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        //val screenDensity = Resources.getSystem().displayMetrics.density
        val screenDensity = 1f

        fun disableUserInteraction(context: Context) {
            try {
                (context as MainActivity).window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } catch (e: Exception) {
            }
        }

        fun enableUserInteraction(context: Context) {
            try {
                (context as MainActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e: Exception) {
            }
        }



        fun showKeyboard(view: View) {
            view.requestFocus()
            if (!isHardKeyboardAvailable(view)) {
                val inputMethodManager =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(view, 0)
            }
        }

        private fun isHardKeyboardAvailable(view: View): Boolean {
            return view.context.resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS
        }

        fun hideKeyboard(activity: Activity) {
            try {
                val inputManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (activity.currentFocus!!.windowToken != null)
                    inputManager.hideSoftInputFromWindow(
                        activity.currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
            } catch (e: Exception) {
            }
        }

        fun hideKeyboard(view: View) {
            try {
                val inputManager =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (view.windowToken != null)
                    inputManager.hideSoftInputFromWindow(
                        view.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
            } catch (e: Exception) {
            }
        }

        fun showSnackBar(message: String, _view: View) {
            val snackBar = Snackbar.make(
                _view,
                message,
                Snackbar.LENGTH_LONG
            )
            val view = snackBar.view
            val snackTV =
                view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            snackTV.maxLines = 5
            snackBar.show()
        }

        fun getMd5String(stringToConvert: String): String {
            try {
                // Create MD5 Hash
                val digest = java.security.MessageDigest
                    .getInstance("sha256")
//                        .getInstance("MD5")
                digest.update(stringToConvert.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }

        private fun getMd5PasswordString(stringToConvert: String?): String {

            try {
                // Create MD5 Hash
                val digest = java.security.MessageDigest.getInstance("MD5")
                digest.update(stringToConvert!!.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }

        fun generateUserNameForQuickBloxUserBuyer(email: String?): String? {

            return email + "_buyer"
        }

        fun generateUserNameForQuickBloxUserSeller(email: String?): String? {

            return email + "_seller"
        }

        fun generatePasswordForQuickBloxUser(email: String?, customerToken: String?): String? {

            val pwd = email + customerToken
            return getMd5PasswordString(pwd)
        }

        fun showShakeError(context: Context, viewToAnimate: View) {
            try {
                viewToAnimate.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.shake_error
                    )
                )
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }

        fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun isVoiceAvailable(context: Context): Boolean {
            return context.packageManager.queryIntentActivities(
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
                0
            ).size > 0
        }

        /**
         * Remove [] from Error Objects when there are multiple errors
         *
         * @param message as String
         * @return replacedString
         */
        fun removeArrayBrace(message: String): String {
            val replaceString: String
            replaceString = message.replace("[\"", "").replace("\"]", "").replace(".", "")
            return replaceString
        }



    }


}