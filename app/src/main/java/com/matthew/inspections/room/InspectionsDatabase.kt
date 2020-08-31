package com.matthew.inspections.room


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.matthew.inspections.room.converters.InspectionStatusConverter
import com.matthew.inspections.room.converters.LocalDateTimeConverter
import com.matthew.inspections.room.data.*

@Database(
    entities = [
        Inspection::class,
        Question::class,
        Answer::class,
        LocalAuthorisation::class,
        Area::class,
        Type::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InspectionStatusConverter::class,
    LocalDateTimeConverter::class
)
abstract class InspectionsDatabase : RoomDatabase() {
    abstract fun inspectionsDao(): InspectionsDao
}