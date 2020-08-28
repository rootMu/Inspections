package com.matthew.inspections.room

import androidx.room.*
import com.matthew.inspections.room.data.InspectionWithQuestions
import com.matthew.inspections.room.data.LocalAuthorisation
import com.matthew.inspections.room.data.QuestionWithAnswers

@Dao
interface InspectionsDao {

    @Query("SELECT * FROM authentication WHERE userId = :userId")
    fun has(userId: Int): LocalAuthorisation?

    @Query("SELECT * FROM authentication WHERE username = :username")
    fun hasUsername(username: String): LocalAuthorisation?

    @Update
    fun update(authorisation: LocalAuthorisation?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authorisation: LocalAuthorisation)

    @Transaction
    @Query("SELECT * FROM inspection")
    fun getInspectionsWithQuestions(): List<InspectionWithQuestions>

    @Transaction
    @Query("SELECT * FROM question")
    fun getQuestionsWithAnswers(): List<QuestionWithAnswers>

}