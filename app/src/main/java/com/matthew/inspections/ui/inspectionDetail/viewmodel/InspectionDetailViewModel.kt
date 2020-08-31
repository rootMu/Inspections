package com.matthew.inspections.ui.inspectionDetail.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.matthew.inspections.dagger.qualifier.ForDatabase
import com.matthew.inspections.data.inspections.InspectionsRepository.Companion.randomString
import com.matthew.inspections.room.InspectionsDao
import com.matthew.inspections.room.data.*
import com.matthew.inspections.ui.inspectionDetail.InspectionDetailAdapter
import com.matthew.inspections.ui.inspectionDetail.uiModel.InspectionDetailUiModel
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiAnswer
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiQuestion
import com.matthew.inspections.ui.inspections.InspectionStatus
import java.util.concurrent.Executor
import kotlin.random.Random

class InspectionDetailViewModel @ViewModelInject constructor(
    private var dao: InspectionsDao,
    @ForDatabase private var executor: Executor
) : ViewModel(), InspectionDetailAdapter.InspectionDetailListener {

    companion object {
        const val INSPECTION_ID = "INSPECTION_ID"
    }

    private var id: Int = 0

    val adapter =
        InspectionDetailAdapter(this)

    private var submitted = false

    private val loadTrigger = MutableLiveData(Unit)

    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> get() = errorLiveData

    private val inspectionWithQuestionsLiveData: LiveData<InspectionWithQuestions> =
        loadTrigger.switchMap {
            try {
                loadData(submitted)
            } catch (e: Exception) {
                //TODO() handle errors
                MutableLiveData(
                    InspectionWithQuestions(Inspection(name = "Inspection Not Found"))
                )
            }
        }

    val uiData =
        Transformations.map(inspectionWithQuestionsLiveData, ::mapInspectionWithQuestionsToList)
    val inspectionName =
        Transformations.map(inspectionWithQuestionsLiveData, ::mapInspectionWithQuestionsToName)
    val inspectionStatus =
        Transformations.map(inspectionWithQuestionsLiveData, ::mapInspectionWithQuestionsToStatus)
    val answers =
        Transformations.map(inspectionWithQuestionsLiveData, ::mapInspectionWithQuestionsToAnswers)

    fun retrieveInspection(id: Int) {
        this.id = id
        loadTrigger.value = Unit
    }

    private fun mapInspectionWithQuestionsToName(data: InspectionWithQuestions?) =
        data?.inspection?.name ?: randomString()

    private fun mapInspectionWithQuestionsToStatus(data: InspectionWithQuestions?) =
        data?.inspection?.status ?: InspectionStatus.UNKNOWN

    private fun mapInspectionWithQuestionsToAnswers(data: InspectionWithQuestions?) =
        mutableListOf<Answer>().apply {
            data?.let { inspection ->
                inspection.questions.forEach { question ->

                    repeat(Random.nextInt().coerceAtMost(15).coerceAtLeast(2)) {
                        val answer = Answer(
                            answerId = Random.nextInt().coerceAtLeast(2).coerceAtMost(10),
                            questionId = question.questionId,
                            displayText = randomString()
                        )
                        add(answer)
                    }
                }
            }
        }


    private fun mapInspectionWithQuestionsToList(data: InspectionWithQuestions?): List<InspectionDetailUiModel> {

        return mutableListOf<InspectionDetailUiModel>().apply {
            data?.apply {
                questions.forEach { question ->
                    add(question.mapToUi())

                    //TODO() Fetch QuestionWithAnswers from database
                    repeat(Random.nextInt().coerceAtMost(15).coerceAtLeast(2)) {
                        val answer = Answer(
                            answerId = Random.nextInt().coerceAtLeast(2).coerceAtMost(10),
                            questionId = question.questionId,
                            displayText = randomString()
                        )
                        add(answer.mapToUi())
                    }
                }
            }
        }
    }

    private fun Question.mapToUi() = UiQuestion(questionId, question)
    private fun Answer.mapToUi() = UiAnswer(answerId, displayText)

    fun submit() {
        //call repo to save inspection and add to upload queue
        //if saved successfully then update status
        submitted = true
        loadTrigger.value = Unit

    }

    private fun loadData(submitted: Boolean = false) =
        liveData {

            //TODO() obtain InspectionWithQuestions from database
            val found = true
            val area = Random.nextInt().coerceAtLeast(0).coerceAtMost(7)
            val type = Random.nextInt().coerceAtLeast(0).coerceAtMost(5)
            val numberOfQuestions = Random.nextInt().coerceAtLeast(1).coerceAtMost(10)
            val questions = mutableListOf<Question>()
            repeat(numberOfQuestions) { questionId ->

                questions.add(
                    Question(
                        0,
                        questionId,
                        id,
                        randomString()
                    )
                )
            }
            val inspectionWithQuestions = InspectionWithQuestions(
                inspection = Inspection(
                    inspectionId = id,
                    name = if (submitted) "SUBMITTED!!!" else randomString(),
                    status = if (submitted) InspectionStatus.COMPLETED else InspectionStatus.getByValue(
                        Random.nextInt().coerceAtLeast(1).coerceAtMost(5)
                    )
                ),
                questions = questions,
                area = Area(areaId = area, name = TEMP_AREAS[area]),
                type = Type(typeId = type, name = TEMP_TYPE[type])
            )

            if (!found) {
                throw InspectionDetailException("no such inspection found with id: $id")
            }

            emit(inspectionWithQuestions)
        }


    inner class InspectionDetailException(message: String) : Exception(message)

    private val TEMP_AREAS = arrayListOf(
        "Accident and emergency",
        "Anaesthetics",
        "Breast screening",
        "Cardiology",
        "Chaplaincy",
        "Critical care",
        "Diagnostic imaging",
        "Discharge lounge"
    )
    private val TEMP_TYPE = arrayListOf(
        "Medical",
        "Post Op",
        "Pre Op",
        "Catering",
        "Surgical",
        "Counts"
    )

    override fun onValueEntered(answerId: Int, value: Int) {
        //update answer repo with value
        try{
            executor.execute {
                dao.updateAnswerWithId(answerId, value)
            }
        }catch(e: Exception){
            //catch error
            //some sort of error updating value,
            //give ui a heads up with some sort
            //of livedata
            errorLiveData.value = e.message
        }

    }
}