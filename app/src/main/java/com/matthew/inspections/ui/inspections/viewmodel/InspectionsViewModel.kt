package com.matthew.inspections.ui.inspections.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.matthew.inspections.data.inspections.InspectionsRepository
import com.matthew.inspections.room.data.Inspection
import com.matthew.inspections.room.data.InspectionWithQuestions
import com.matthew.inspections.room.data.Question
import com.matthew.inspections.ui.inspections.InspectionsAdapter
import com.matthew.inspections.ui.inspections.uiModel.InspectionUiModel
import com.matthew.inspections.ui.inspections.uiModel.UiInspection
import com.matthew.inspections.ui.inspections.uiModel.UiTitle
import java.util.*

class InspectionsViewModel @ViewModelInject constructor(private val repository: InspectionsRepository) : ViewModel(),
    InspectionsAdapter.InspectionListener {

    companion object {
        const val PAST = 0
        const val CURRENT = 1
        const val FUTURE = 2
    }

    val adapter = InspectionsAdapter(this)

    private val _launchDetailActivity = MutableLiveData<Int>()
    val launchDetailActivity get() = _launchDetailActivity

    //TODO() retrieve List of InspectionWithQuestions from Database depending on status
    fun getInspectionsUiData(status: Int) = liveData {
        val list = mutableListOf<InspectionUiModel>()

        val data = repository.getAllInspections(status)
        data.groupBy { it.inspection.areaId }.forEach {
            val area = TEMP_AREAS[it.key]
            list.add(UiTitle(area))
            it.value.map { inspection ->
                list.add(inspection.mapToUi())
            }
        }


        /**
         * Split list by areaId
         */
        TEMP_INSPECTIONS_WITH_QUESTIONS().groupBy { it.inspection.areaId }.forEach {
            //TODO() fetch area by id
            val area = TEMP_AREAS[it.key]
            list.add(UiTitle(area))
            it.value.map { inspection ->
                list.add(inspection.mapToUi())
            }
        }

        emit(list)
    }

    private fun InspectionWithQuestions.mapToUi() =
        UiInspection(inspection.name, questions.size, inspection.date, inspection.inspectionId)

    override fun onClickedInspection(inspectionId: Int) {
        _launchDetailActivity.value = inspectionId
    }


    /**
     * Testing values
     */

    private fun TEMP_INSPECTIONS_WITH_QUESTIONS() =
        mutableListOf<InspectionWithQuestions>(
            InspectionWithQuestions(
                Inspection(areaId = Random().nextInt().coerceAtMost(7).coerceAtLeast(0), inspectionId = 0, name = "steve"),
                mutableListOf(Question(0, 0, 0, "question"))
            ),
            InspectionWithQuestions(
                Inspection(areaId = Random().nextInt().coerceAtMost(7).coerceAtLeast(0), inspectionId = 1, name = "john"),
                mutableListOf(Question(0, 1, 1, "question"))
            ),
            InspectionWithQuestions(
                Inspection(areaId = Random().nextInt().coerceAtMost(7).coerceAtLeast(0), inspectionId = 2, name = "pete"),
                mutableListOf(Question(0, 2, 2, "question"))
            ),
            InspectionWithQuestions(
                Inspection(areaId = Random().nextInt().coerceAtMost(7).coerceAtLeast(0), inspectionId = 3, name = "rob"),
                mutableListOf(Question(0, 3, 3, "question"))
            ),
            InspectionWithQuestions(
                Inspection(areaId = Random().nextInt().coerceAtMost(7).coerceAtLeast(0), inspectionId = 4, name = "harry"),
                mutableListOf(Question(0, 4, 4, "question"))
            ),
            InspectionWithQuestions(
                Inspection(areaId = Random().nextInt().coerceAtMost(7).coerceAtLeast(0), inspectionId = 5, name = "bob"),
                mutableListOf(Question(0, 5, 5, "question"))
            )
        )


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


}