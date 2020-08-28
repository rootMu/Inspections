package com.matthew.inspections.ui.login

import com.matthew.inspections.user.Authorisation

data class LoginAttempt(val action: LoginAction, val authorisation: Authorisation)

enum class LoginAction(val value: Int) {
    LOGIN_SUCCESSFUL(0),
    INVALID_USERNAME_OR_PASSWORD( 1),
    UNSUCCSESSFUL_LOGIN_ATTEMPT(2)
}