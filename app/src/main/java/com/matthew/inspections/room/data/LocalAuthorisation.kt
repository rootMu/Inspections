package com.matthew.inspections.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matthew.inspections.room.converters.LocalDateTimeConverter
import com.matthew.inspections.user.Authorisation
import java.time.LocalDateTime

/**
 * Room class for local authorisation
 * only saved once server authorisation
 * has been received
 * TODO() make more secure
 */
@Entity(tableName = "authentication")
data class LocalAuthorisation(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "userId") var userId: Int = 0,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "token") var token: String? = null,
    @ColumnInfo(name = "expiryDate") var expiryDate: LocalDateTime? = null
)

infix fun LocalAuthorisation.update(authorisation: LocalAuthorisation): LocalAuthorisation{
    userId = authorisation.userId
    username = authorisation.username
    password = authorisation.password
    token = authorisation.token
    expiryDate = authorisation.expiryDate
    return this
}

fun LocalAuthorisation.update(authorisation: Authorisation): LocalAuthorisation{
    userId = authorisation.userId
    token = authorisation.token
    authorisation.expiryDate?.let{
        expiryDate = LocalDateTimeConverter().toLocalDateTime(it)
    }
    return this
}