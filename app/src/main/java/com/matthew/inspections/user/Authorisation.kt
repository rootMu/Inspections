package com.matthew.inspections.user

import com.matthew.inspections.room.data.LocalAuthorisation

data class User(
    val id: Int,
    val groups: List<Int>,
    val inspections: List<Int>,
    val authorisation: Authorisation,
    val localAuthorisation: LocalAuthorisation
)

data class Authorisation(
    val authorised: Boolean = false,
    val userId: Int = 0,
    val validationDate: String? = null,
    val expiryDate: String? = null,
    val token: String? = null,
    val errorResponse: String? = null
)