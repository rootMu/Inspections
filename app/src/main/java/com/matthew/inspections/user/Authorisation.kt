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
    val authorised: Boolean,
    val validationDate: String?,
    val expiryDate: String?,
    val token: String?,
    val errorResponse: String?
)