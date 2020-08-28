package com.matthew.inspections.room.converters

import androidx.room.TypeConverter
import com.matthew.inspections.ui.inspections.InspectionStatus

class InspectionStatusConverter {
    @TypeConverter
    fun fromInspectionStatus(value: InspectionStatus): Int{
        return value.ordinal
    }

    @TypeConverter
    fun toInspectionStatus(value: Int): InspectionStatus{
        return InspectionStatus.getByValue(value)
    }
}