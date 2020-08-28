package com.matthew.inspections.ui.inspections.uiModel

import java.time.OffsetDateTime

class UiInspection(override val name: String, val numberOfQuestions: Int, val date: OffsetDateTime, val id: Int) : InspectionUiModel