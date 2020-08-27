package com.matthew.inspections.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityLoginBinding
import com.matthew.inspections.ui.inspections.InspectionsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )
        binding.viewModel = viewModel.apply{
            handleLoginAction.observe(this@LoginActivity,
                Observer<LoginAction?> { action -> action?.handleAction() })
        }
        binding.lifecycleOwner = this
    }

    private fun LoginAction.handleAction(): Any = when(this){
        LoginAction.LOGIN_SUCCESSFUL -> {
            startActivity(Intent(applicationContext, InspectionsActivity::class.java))
            finish()
        }
        LoginAction.INVALID_USERNAME_OR_PASSWORD -> {
            AlertDialog.Builder(this@LoginActivity).apply {
                setTitle("Unable To Login")
                setMessage("Username Or Password are incorrect")
            }.show()
        }
    }
}