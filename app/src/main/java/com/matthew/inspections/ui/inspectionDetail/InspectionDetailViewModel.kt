package com.matthew.inspections.ui.inspectionDetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.matthew.inspections.room.data.Answer
import com.matthew.inspections.room.data.Inspection
import com.matthew.inspections.room.data.InspectionWithQuestions
import com.matthew.inspections.room.data.Question
import com.matthew.inspections.ui.inspectionDetail.uiModel.InspectionDetailUiModel
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiAnswer
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiQuestion
import com.matthew.inspections.ui.inspections.InspectionStatus
import java.time.OffsetDateTime
import kotlin.random.Random

class InspectionDetailViewModel @ViewModelInject constructor() : ViewModel() {

    companion object {
        const val INSPECTION_ID = "INSPECTION_ID"
    }

    private var id: Int = 0

    val adapter = InspectionDetailAdapter()

    private val loadTrigger = MutableLiveData(Unit)

    private val inspectionWithQuestionsLiveData: LiveData<InspectionWithQuestions> = loadTrigger.switchMap {
        try {
            loadData()
        } catch (e: Exception) {
            //TODO() handle errors
            MutableLiveData(InspectionWithQuestions(Inspection(0,0,0, "Inspection Not Found"), emptyList()))
        }
    }

    val uiData = Transformations.map(inspectionWithQuestionsLiveData, ::mapInspectionWithQuestionsToList)
    val inspectionName =
        Transformations.map(inspectionWithQuestionsLiveData, ::mapInspectionWithQuestionsToName)

    fun retrieveInspection(id: Int) {
        this.id = id
        loadTrigger.value = Unit
    }

    private fun mapInspectionWithQuestionsToName(data: InspectionWithQuestions?) = data?.inspection?.name ?: ""

    private fun mapInspectionWithQuestionsToList(data: InspectionWithQuestions?): List<InspectionDetailUiModel> {

        val list = mutableListOf<InspectionDetailUiModel>()

        data?.apply {
            questions.forEach {
                list.add(it.mapToUi())

                //TODO() Fetch QuestionWithAnswers from database
                repeat(Random.nextInt()) {
                    val answer = Answer(0, Random.nextInt(), "Answer")
                    list.add(answer.mapToUi())
                }
            }
        }

        return list
    }

    private fun Question.mapToUi() = UiQuestion(questionId, question)
    private fun Answer.mapToUi() = UiAnswer(answerId, displayText)


    private fun loadData() = liveData {
        //TODO() obtain InspectionWithQuestions from database
        val found = true
        val inspectionWithQuestions = InspectionWithQuestions(Inspection(
            0,
            id,
            0,
            "Inspection",
            InspectionStatus.COMPLETED,
            OffsetDateTime.now()),emptyList())

        if (!found) {
            throw InspectionDetailException("no such inspection found with id: $id")
        }

        emit(inspectionWithQuestions)
    }

    inner class InspectionDetailException(message: String) : Exception(message)
}