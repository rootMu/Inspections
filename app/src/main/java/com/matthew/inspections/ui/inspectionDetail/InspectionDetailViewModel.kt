package com.matthew.inspections.ui.inspectionDetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.matthew.inspections.data.Answer
import com.matthew.inspections.data.InspectionDetail
import com.matthew.inspections.data.Question
import com.matthew.inspections.ui.inspections.InspectionStatus

class InspectionDetailViewModel @ViewModelInject constructor() : ViewModel() {

    companion object{
        const val INSPECTION_ID = "INSPECTION_ID"
    }

    fun retrieveInspection(id: Int) = when(id){
        -1 -> InspectionDetail("No Inspection Found")
        else -> InspectionDetail(id.toString(), InspectionStatus.COMPLETED, listOf(Question(listOf(Answer(title="bob"),Answer(title="bob"),Answer(title="bob"),Answer(title="bob")))))
    }
}