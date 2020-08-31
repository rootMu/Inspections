package com.matthew.inspections.ui.login

import android.app.job.JobScheduler
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.firebase.jobdispatcher.*
import com.matthew.inspections.data.login.AuthenticationRepository
import com.matthew.inspections.room.data.LocalAuthorisation
import com.matthew.inspections.room.data.update
import com.matthew.inspections.user.Authorisation
import com.matthew.inspections.util.ConnectivityJob
import com.matthew.inspections.util.NetworkReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginViewModel @ViewModelInject constructor(
    private val repository: AuthenticationRepository,
    @ApplicationContext private var context: Context
) :
    ViewModel() {

    companion object {
        const val MIN_USERNAME_LENGTH = 6
        const val MIN_PASSWORD_LENGTH = 6
        const val INVALID_PASSWORD = "InvalidPassword"
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

    private var tempLocalAuthorisation: LocalAuthorisation? = null

    private val _loginAuthorisation: MutableLiveData<Authorisation> = MutableLiveData()
    private val loginAuthorisation: LiveData<Authorisation> get() = _loginAuthorisation

    private val loginAction =
        Transformations.map(loginAuthorisation, ::mapAuthorisationToLoginAction)
    val handleLoginAction: LiveData<LoginAttempt?> get() = loginAction

    private fun mapAuthorisationToLoginAction(authorisation: Authorisation): LoginAttempt {
        val action = if (!authorisation.authorised) {
            when (authorisation.errorResponse) {
                INVALID_PASSWORD -> LoginAction.INVALID_USERNAME_OR_PASSWORD
                else -> LoginAction.UNSUCCSESSFUL_LOGIN_ATTEMPT
            }
        } else {
            //if authorised by server than save to database
            viewModelScope.launch(Dispatchers.IO) {
                tempLocalAuthorisation?.update(authorisation)

                saveLocalAuthorisation()
                tempLocalAuthorisation = null
            }

            LoginAction.LOGIN_SUCCESSFUL
        }

        return LoginAttempt(action, authorisation)
    }

    val onTextChangedUsername: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { text, _, _, _ ->
        text?.let {
            username.value = text.toString()
        }
    }

    val onTextChangedPassword: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { text, _, _, _ ->
        text?.let {
            password.value = text.toString()
        }
    }

    private fun saveLocalAuthorisation() {
        tempLocalAuthorisation?.let {
            repository.save(it)
        }
        tempLocalAuthorisation = null
    }

    fun validateLogin(username: String, password: String) {
        tempLocalAuthorisation = LocalAuthorisation(username = username, password = password)
        viewModelScope.launch(Dispatchers.IO) {
            _loginAuthorisation.postValue(repository.login(username, password))
        }
    }

}