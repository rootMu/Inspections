package com.matthew.inspections.ui.inspections.uiModel

import java.time.LocalDateTime

class UiInspection(override val name: String, val numberOfQuestions: Int, val date: LocalDateTime, val id: Int) : InspectionUiModel