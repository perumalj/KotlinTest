package com.app.assignment.myapplication.base

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.app.assignment.myapplication.MainActivity
import com.app.assignment.myapplication.R
import com.app.assignment.myapplication.helpers.DebugLog
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


abstract class FragmentBase<V : ViewModelBase, DataBinding : ViewDataBinding> :
    FragmentBaseWrapper() {

    companion object {

        private const val MY_REQUEST_CODE = 1111
    }

    private var _activity: MainActivity? = null
    private var viewModel: V? = null
    private lateinit var dataBinding: DataBinding

    private var appUpdateManager: AppUpdateManager? = null

    /**
     * This is the abstract method by which we are generating the
     * Databinding with View from Single Screen.
     *
     */
    abstract fun getLayoutId(): Int

    /**
     * This is the generic method where we have to setup the toolbar from single place and
     * from all other extended fragment should have to manage the logic related to the toolbar
     * from this method
     */
    abstract fun setupToolbar()

    /**
     * This is the method from where we are initialized the
     * fragment specific data members and methods.
     */
    abstract fun initializeScreenVariables()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return dataBinding.root
    }

    fun getDataBinding() = dataBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewModel is set as Binding Variable
        dataBinding.apply {
            lifecycleOwner = this@FragmentBase
        }
        setActivity(activity as MainActivity)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        initializeScreenVariables()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        setUpSnackBar()
        setUpSnackBarForLong()
        setUpProgressBar()
        checkForInAppUpdate()
    }

    /**
     * This method is used to check an In App Update feature.
     */
    private fun checkForInAppUpdate() {

        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(requireActivity())

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // For a flexible update, use AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager?.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    IMMEDIATE,
                    // The current requireActivity() making the update request.
                    requireActivity(),
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE
                )

            }
        }

    }

    /**
     * This is generic method based on the MutableLive Data Concept where value changed with Progressbar
     * will reflect and decide if value is true then it will display.
     */
    private fun setUpProgressBar() {
        viewModel?.getProgressBar()?.observe(this) { t: Boolean ->
            if (_activity != null) {
                _activity?.displayProgress(t)
            }
        }
    }

    /**
     * This is the abstract method where we are adding the logic for generating the ViewModel
     * Object.
     */
    abstract fun getViewModel(): V?

    /**
     * This is generic method based on the MutableLive Data Concept where value changed with Snakebar
     * will reflect and display the message with Snakebar.
     */
    private fun setUpSnackBar() {
        viewModel?.getSnakeBarMessage()?.observe(this, Observer { o: Any ->
            if (_activity != null) {
                if (o is Int) {
                    _activity?.resources?.getString(o)?.let { showSnackBar(it) }!!
                } else if (o is String) {
                    showSnackBar(o)
                }
            }

        } as Observer<Any>)
    }

    /**
     * This is generic method based on the MutableLive Data Concept where value changed with Snakebar
     * will reflect and display the message with Snakebar.
     */
    private fun setUpSnackBarForLong() {
        viewModel?.getSnakeBarMessageForLong()?.observe(this, Observer { o: Any ->
            if (_activity != null) {
                if (o is Int) {
                    _activity?.resources?.getString(o)?.let { showSnackBarForLong(it) }!!
                } else if (o is String) {
                    showSnackBar(o)
                }
            }

        } as Observer<Any>)
    }



    override fun onDetach() {
        super.onDetach()
        _activity = null
    }

    private fun setActivity(activity: MainActivity) {
        this._activity = activity
    }

    private fun getBaseActivity(): MainActivity? {
        return _activity
    }


    /**
     * This method is used to display the Snake Bar Toast message to user.
     *
     * @param message string to display.
     */
    fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            _activity?.findViewById(android.R.id.content)!!,
            message,
            Snackbar.LENGTH_LONG
        )
        val view = snackBar.view
        val snackTV = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackTV.gravity = Gravity.CENTER
        snackTV.maxLines = 5
        snackBar.show()
    }

    /**
     * This method is used to display the Snake Bar Toast message to user.
     *
     * @param message string to display.
     */
    fun showSnackBarForLong(message: String) {
        val snackBar = Snackbar.make(
            _activity?.findViewById(android.R.id.content)!!,
            message,
            Snackbar.LENGTH_LONG
        )
        val view = snackBar.view
        val snackTV = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackTV.maxLines = 5
        snackBar.show()
    }

    /**
     * This is the generic method from where the message will display if something went wrong.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun somethingWentWrong() {
        viewModel?.showSnackBarMessage(R.string.label_response_error)
    }

    /**
     * This is the generic method from where the message will display if Internet connection not available.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun noInternet() {
        viewModel?.showSnackBarMessage(R.string.no_internet)
    }

    override fun onRetryClicked(retryButtonType: String) {
    }

    /**
     * This is the generic method from where the message will display if No Data Found.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun dataNotFound(message: String?, messageCode: String?) {
        message?.let { viewModel?.showSnackBarMessage(it) }
    }

    /**
     * This is the generic method from where the message will display if user verified or not.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun verifyUser(message: String) {
        viewModel?.showSnackBarMessage(message)
    }

    override fun newVersionFound() {
    }



    override fun onDestroyView() {
        super.onDestroyView()
        getBaseActivity()?.displayProgress(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                DebugLog.e("Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }

    }

    // Checks that the update is not stalled during 'onResume()'.
    // However, you should execute this check at all entry points into the app.
    override fun onResume() {
        super.onResume()
        appUpdateManager
            ?.appUpdateInfo
            ?.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        requireActivity(),
                        MY_REQUEST_CODE
                    )
                }
            }
    }

    fun getAWSFileName(prefix: String, file: File): String {
        val fileExtension = file.absolutePath.substring(file.absolutePath.lastIndexOf(".") + 1)
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val min = 10
        val max = 80
        val r = Random()
        val rndNum = r.nextInt(max - min + 1) + min
        return prefix + "_" + "Android" + "_" + timeStamp + "" + rndNum + "." + fileExtension
    }
}