package com.matthew.inspections.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.matthew.inspections.room.data.InspectionWithQuestions
import com.matthew.inspections.room.data.QuestionWithAnswers

@Dao
interface InspectionsDao {

    @Transaction
    @Query("SELECT * FROM inspection")
    fun getInspectionsWithQuestions(): List<InspectionWithQuestions>

    @Transaction
    @Query("SELECT * FROM question")
    fun getQuestionsWithAnswers(): List<QuestionWithAnswers>

}