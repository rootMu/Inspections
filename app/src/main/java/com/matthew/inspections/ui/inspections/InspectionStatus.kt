package com.matthew.inspections.ui.inspections

enum class InspectionStatus(val value: Int) {
    COMPLETED(0),
    NOT_STARTED( 1),
    STARTED(2),
    EDITING(3),
    NOT_READY(4),
    UNKNOWN(5);

    companion object {
        private val values = values()
        fun getByValue(value: Int) = values.firstOrNull { it.value == value }?: UNKNOWN
    }
}