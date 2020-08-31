package com.matthew.inspections.room.data

import androidx.room.*

@Entity(tableName = "question")
data class Question(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "questionId") var questionId: Int,
    @ColumnInfo(name = "inspectionId") var inspectionId: Int,
    @ColumnInfo(name = "questionString") var question: String
)

data class QuestionWithAnswers(
    @Embedded val question: Question,
    @Relation(
        parentColumn = "questionId",
        entityColumn = "answerId"
    )
    val answers: List<Answer> = emptyList()
)