package com.matthew.inspections.room


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.matthew.inspections.room.converters.InspectionStatusConverter
import com.matthew.inspections.room.converters.OffsetDateTimeConverter
import com.matthew.inspections.room.data.Answer
import com.matthew.inspections.room.data.Inspection
import com.matthew.inspections.room.data.LocalAuthorisation
import com.matthew.inspections.room.data.Question

@Database(entities = [Inspection::class, Question::class, Answer::class, LocalAuthorisation::class], version = 1, exportSchema = false)
@TypeConverters(InspectionStatusConverter::class, OffsetDateTimeConverter::class)
abstract class InspectionsDatabase: RoomDatabase() {
    abstract fun inspectionsDao(): InspectionsDao
}