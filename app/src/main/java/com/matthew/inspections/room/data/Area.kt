package com.matthew.inspections.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area" )
data class Area(
    @PrimaryKey(autoGenerate = true) var id: Int? = 0,
    @ColumnInfo(name = "areaId") var areaId: Int = -1,
    @ColumnInfo(name = "name") var name: String? = null)