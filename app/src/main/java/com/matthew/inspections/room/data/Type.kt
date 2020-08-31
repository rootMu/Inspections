package com.matthew.inspections.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type" )
data class Type(
    @PrimaryKey(autoGenerate = true) var id: Int? = 0,
    @ColumnInfo(name = "typeId") var typeId: Int = -1,
    @ColumnInfo(name = "name") var name: String? = null)