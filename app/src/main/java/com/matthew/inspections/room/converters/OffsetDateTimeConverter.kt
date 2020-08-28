package com.matthew.inspections.room.converters

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Converter to use for OffsetDateTime
 * Times are converted to and from using ISO_Standard
 */
class OffsetDateTimeConverter {
    @TypeConverter
    fun fromOffsetTime(value: OffsetDateTime): String{
        return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @TypeConverter
    fun toOffsetTime(value: String): OffsetDateTime {
        return OffsetDateTime
            .parse(value,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}