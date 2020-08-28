package com.matthew.inspections.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "userName") var userName: String,
    @ColumnInfo(name = "password") var password: String
)