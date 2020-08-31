package com.matthew.inspections.ui.inspections

enum class InspectionListStatus(val value: Int) {
    Past(0),
    Current( 1),
    Future(2);

    companion object {
        private val values = values()
        fun getByValue(value: Int) = values.firstOrNull { it.value == value }?: Past
    }
}