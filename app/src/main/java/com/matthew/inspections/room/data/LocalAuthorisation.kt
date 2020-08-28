package com.matthew.inspections.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matthew.inspections.room.converters.OffsetDateTimeConverter
import com.matthew.inspections.user.Authorisation
import java.time.LocalDateTime
import java.time.OffsetDateTime

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
    @ColumnInfo(name = "expiryDate") var expiryDate: OffsetDateTime? = null
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
        expiryDate = OffsetDateTimeConverter().toOffsetTime(it)
    }
    return this
}