package com.matthew.inspections.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel @ViewModelInject constructor() : ViewModel() {

    companion object {
        const val MIN_USERNAME_LENGTH = 6
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val username = MutableLiveData<String>()
    private val password = MutableLiveData<String>()

    private val login = MediatorLiveData<Boolean>().apply {
        addSource(username) {
            value =
                it.length >= MIN_USERNAME_LENGTH && password.value?.length ?: 0 >= MIN_PASSWORD_LENGTH
        }
        addSource(password) {
            value =
                it.length >= MIN_PASSWORD_LENGTH && password.value?.length ?: 0 >= MIN_USERNAME_LENGTH
        }
    }
    val canLogin get() = login

    //Stores actions for view.
    private val loginAction: MutableLiveData<LoginAction> = MutableLiveData()
    val handleLoginAction: LiveData<LoginAction> get() = loginAction

    val onTextChangedUsername: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { text, start, before, count ->
        text?.let {
            username.value = text.toString()
        }
    }

    val onTextChangedPassword: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { text, start, before, count ->
        text?.let {
            password.value = text.toString()
        }
    }

    fun validateLogin(username: String, password: String){
        val usernameValid = username.contains(Regex("a"))
        val passwordValid = true
        loginAction.value = if(usernameValid && passwordValid){
            LoginAction.LOGIN_SUCCESSFUL
        }else{
            LoginAction.INVALID_USERNAME_OR_PASSWORD
        }

    }


}