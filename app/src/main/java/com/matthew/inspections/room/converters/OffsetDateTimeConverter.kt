package com.matthew.inspections.room.converters

import androidx.room.TypeConverter
import java.time.OffsetTime
import java.time.format.DateTimeFormatter

/**
 * Converter to use for OffsetTime
 * Times are converted to and from using ISO_Standard
 */
class OffsetTimeConverter {
    @TypeConverter
    fun fromOffsetTime(value: OffsetTime): String{
        return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @TypeConverter
    fun toOffsetTime(value: String): OffsetTime {
        return OffsetTime
            .parse(value,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}