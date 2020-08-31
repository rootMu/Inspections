package com.matthew.inspections.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.matthew.inspections.room.data.*
import java.time.LocalDateTime


@Dao
abstract class InspectionsDao {

    @Query("SELECT * FROM authentication WHERE userId = :userId")
    abstract fun has(userId: Int): LocalAuthorisation?

    @Query("SELECT * FROM authentication WHERE username = :username")
    abstract fun hasUsername(username: String): LocalAuthorisation?

    @Update
    abstract fun update(authorisation: LocalAuthorisation?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(authorisation: LocalAuthorisation)

    fun updateAnswerWithId(id: Int, newValue: Int){
        getAnswerById(id)?.apply {
            value = newValue
        }?.let{
            update(it)
        }?:run{

        /**
            pass the question id in here as well and it can be added into the database if it wasn't found

            fun updateAnswerWithId(aId: Int, qId, newValue: Int, na: Boolean){
            getAnswerById(aId)?.apply {
                        value = newValue
                        questionId = qId
                        notApplicable = na
                    }?.let{
                  update(it)
             }?:run{
                  insertAnswer(Answer(aId,qId,newValue,notApplicable))
            }

         **/
        }

    }

    @Query("SELECT * FROM answer WHERE answerId = :id")
    abstract fun getAnswerById(id: Int): Answer?

    @Update
    abstract fun update(answer: Answer)

    @Insert
    fun insertQuestionsWithAnswers(questions: List<QuestionWithAnswers>){
        for (question in questions) {
            if (question.answers.isNotEmpty()) {
                insertAnswersForQuestion(question.question, question.answers)
            }
        }
        insertQuestions(questions.map{
            it.question
        }.toList())
    }

    private fun insertAnswersForQuestion(question: Question, answers: List<Answer>) {
        for (answer in answers) {
            answer.questionId = question.questionId
        }
        insertAnswers(answers)
    }

    @Insert
    abstract fun insertAnswers(answers: List<Answer>)
    
    @Insert
    abstract fun insertAnswer(answer: Answer)

    @Insert
    abstract fun insertQuestions(questions: List<Question>)

    @Query("SELECT name FROM area WHERE areaId = :areaId")
    abstract fun getAreaNameById(areaId: Int): String?

    @Query("SELECT name FROM type WHERE typeId = :typeId")
    abstract fun getTypeNameById(typeId: Int): String?

    /**
     * Past Inspections
     */
    @Transaction
    @Query("SELECT * FROM inspection WHERE status = 0 OR datetime(date) < datetime(:time)")
    abstract fun getPastInspections(time: LocalDateTime = LocalDateTime.now()): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM inspection WHERE (status = 0 OR datetime(date) < datetime(:time)) AND name LIKE :name")
    abstract fun getPastInspectionsByName(time: LocalDateTime = LocalDateTime.now(), name: String): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM inspection WHERE (status = 0 OR datetime(date) < datetime(:time)) AND inspectionId = :inspectionId")
    abstract fun getPastInspectionsById(
        time: LocalDateTime = LocalDateTime.now(),
        inspectionId: Int
    ): LiveData<List<InspectionWithQuestions>>

    /**
     * Current Inspections
     */
    @Transaction
    @Query("SELECT * FROM inspection WHERE status = 1 OR status = 2 OR (datetime(date) > datetime(:time) AND status != 0)")
    abstract fun getCurrentInspections(time: LocalDateTime = LocalDateTime.now()): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM inspection WHERE (status = 1 OR status = 2 OR (datetime(date) > datetime(:time) AND status != 0)) AND name LIKE :name")
    abstract fun getCurrentInspectionsByName(
        time: LocalDateTime = LocalDateTime.now(),
        name: String
    ): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM inspection WHERE (status = 1 OR status = 2 OR (datetime(date) > datetime(:time) AND status != 0)) AND inspectionId = :inspectionId")
    abstract fun getCurrentInspectionsById(
        time: LocalDateTime = LocalDateTime.now(),
        inspectionId: Int
    ): LiveData<List<InspectionWithQuestions>>

    /**
     * Future Inspections
     */
    @Transaction
    @Query("SELECT * FROM inspection WHERE (status = 3 OR status = 4) AND datetime(date) > datetime(:time)")
    abstract fun getFutureInspections(time: LocalDateTime = LocalDateTime.now()): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM inspection WHERE ((status = 3 OR status = 4) AND datetime(date) > datetime(:time)) AND name LIKE :name")
    abstract fun getFutureInspectionsByName(time: LocalDateTime = LocalDateTime.now(), name: String): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM inspection WHERE ((status = 3 OR status = 4) AND datetime(date) > datetime(:time)) AND inspectionId = :inspectionId")
    abstract fun getFutureInspectionsById(
        time: LocalDateTime = LocalDateTime.now(),
        inspectionId: Int
    ): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM inspection")
    abstract fun getAllInspections(): LiveData<List<InspectionWithQuestions>>

    @Transaction
    @Query("SELECT * FROM question WHERE inspectionId = :inspectionId")
    abstract fun getQuestionsForInspection(inspectionId: Int): LiveData<List<QuestionWithAnswers>>




}