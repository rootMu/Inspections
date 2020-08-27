package com.matthew.inspections.ui.inspectionDetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.matthew.inspections.data.Answer
import com.matthew.inspections.data.InspectionDetail
import com.matthew.inspections.data.Question
import com.matthew.inspections.ui.inspectionDetail.uiModel.InspectionDetailUiModel
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiAnswer
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiQuestion
import com.matthew.inspections.ui.inspections.InspectionStatus

class InspectionDetailViewModel @ViewModelInject constructor() : ViewModel() {

    companion object {
        const val INSPECTION_ID = "INSPECTION_ID"
    }

    private var id: Int = 0

    val adapter = InspectionDetailAdapter()

    private val loadTrigger = MutableLiveData(Unit)

    private val inspectionDetailLiveData: LiveData<InspectionDetail> = loadTrigger.switchMap {
        loadData()
    }

    val uiData = Transformations.map(inspectionDetailLiveData, ::mapInspectionDetailToList)
    val inspectionDetailName =
        Transformations.map(inspectionDetailLiveData, ::mapInspectionDetailName)

    fun retrieveInspection(id: Int) {
        this.id = id
        loadTrigger.value = Unit
    }

    private fun mapInspectionDetailName(data: InspectionDetail?) = data?.name ?: ""


    private fun mapInspectionDetailToList(data: InspectionDetail?): List<InspectionDetailUiModel> {

        val list = mutableListOf<InspectionDetailUiModel>()

        data?.apply {
            questions.forEach { question ->
                list.add(question.mapToUi())
                question.answers.forEach { answer ->
                    list.add(answer.mapToUi())
                }
            }
        }

        return list
    }

    private fun Question.mapToUi() = UiQuestion(id, question)
    private fun Answer.mapToUi() = UiAnswer(id, displayText)


    private fun loadData() = liveData {
        emit(
            when (id) {
                -1 -> InspectionDetail(name = "No Inspection Found")
                else -> InspectionDetail(
                    id,
                    "Hospital Stores Inspection",
                    InspectionStatus.COMPLETED,
                    listOf(
                        Question(
                            0,
                            "Quantity of blood",
                            listOf(
                                Answer(0, "O-"),
                                Answer(1, "B+"),
                                Answer(2, "AB+"),
                                Answer(3, "A-")
                            )
                        ),
                        Question(
                            1,
                            "How many beds are there?",
                            listOf(
                                Answer(4, "In the pink ward?"),
                                Answer(5, "In the blue ward?")
                            )
                        ),
                        Question(
                            2,
                            "Drug quantities",
                            listOf(
                                Answer(6, "Paracetamol"),
                                Answer(7, "Codeine"),
                                Answer(8, "Aspirin")
                            )
                        )
                    )
                )
            }
        )
    }
}