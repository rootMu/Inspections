package com.matthew.inspections.data

import com.matthew.inspections.ui.inspections.InspectionStatus

data class InspectionDetail(
    val name: String = "",
    val status: InspectionStatus = InspectionStatus.UNKNOWN,
    val questions: List<Question> = emptyList()
)