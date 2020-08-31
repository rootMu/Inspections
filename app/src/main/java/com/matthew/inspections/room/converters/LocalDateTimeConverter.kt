package com.matthew.inspections.room.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * Converter to use for LocalDateTime
 * Times are converted to and from
 */
class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime {
        return try {
            if (dateString.isEmpty()) {
                LocalDateTime.now()
            } else {
                LocalDateTime.parse(dateString)
            }
        } catch (e: Exception) {
            val time = LocalDateTime.now()
            time
        }
    }
}