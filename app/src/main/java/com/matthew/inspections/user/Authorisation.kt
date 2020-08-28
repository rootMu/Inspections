package com.matthew.inspections.user

data class User(
    val id: Int,
    val groups: List<Int>,
    val inspections: List<Int>,
    val authorisation: Authorisation,
    val localAuthorisation: Authorisation
)

data class Authorisation(
    val authorised: Boolean,
    val validationDate: String?,
    val expiryDate: String?,
    val token: String?,
    val errorResponse: String?
)

/**
 * Room class for local authorisation
 * only saved once server authorisation
 * has been received
 * TODO() make more secure
 */
data class LocalAuthorisation(
    val userName: String,
    val password: String
)