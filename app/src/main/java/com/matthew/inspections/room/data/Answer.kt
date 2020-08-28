package com.matthew.inspections.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer")
data class Answer(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "answerId") var answerId: Int,
    @ColumnInfo(name = "displayText") var displayText: String,
    @ColumnInfo(name = "value") var value: Int = 0,
    @ColumnInfo(name = "notApplicable") var notApplicable: Boolean = false)