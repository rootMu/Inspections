package com.matthew.inspections.ui.inspections

enum class InspectionStatus(val value: Int) {
    COMPLETED(0),
    NOT_STARTED( 1),
    STARTED(2),
    NOT_READY(3),
    UNKNOWN(4)
}