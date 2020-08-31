package com.matthew.inspections.ui.login

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.firebase.jobdispatcher.*
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityLoginBinding
import com.matthew.inspections.ui.inspections.InspectionsActivity
import com.matthew.inspections.util.ConnectivityJob
import com.matthew.inspections.util.NetworkReceiver
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var receiver: NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )
        binding.viewModel = viewModel.apply {
            handleLoginAction.observe(this@LoginActivity,
                Observer { attempt -> attempt?.handleAction() })
        }
        binding.lifecycleOwner = this


        /**
         * Example of how job would be scheduled using firebase
         */
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(applicationContext))

        val job = dispatcher.newJobBuilder()
            .setService(ConnectivityJob::class.java)
            .setTag("connectivity-job")
            .setLifetime(Lifetime.FOREVER)
            .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
            .setRecurring(true)
            .setReplaceCurrent(true)
            .setTrigger(Trigger.executionWindow(0, 0))
            .build()

        dispatcher.mustSchedule(job)
        dispatcher.schedule(job)

        /**
         * Register network receiver
         * only here as an example of how it works
         */
        receiver = NetworkReceiver()

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply{
            addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)
        }
        applicationContext.registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(receiver)
    }

    private fun LoginAttempt.handleAction(): Any = when(action) {
        LoginAction.LOGIN_SUCCESSFUL -> {
            startActivity(Intent(applicationContext, InspectionsActivity::class.java))
            finish()
        }
        LoginAction.INVALID_USERNAME_OR_PASSWORD ->
            AlertDialog.Builder(this@LoginActivity).apply {
                setTitle("Unable To Login")
                setMessage(authorisation.errorResponse ?: "Username Or Password are incorrect")
            }.show()

        else ->
            AlertDialog.Builder(this@LoginActivity).apply {
                setTitle("Unable To Login")
                setMessage(authorisation.errorResponse ?: "Generic Error Message")
            }.show()
    }

}