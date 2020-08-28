package com.matthew.inspections.room.data

import androidx.room.*
import com.matthew.inspections.ui.inspections.InspectionStatus
import java.time.OffsetDateTime

/**
 * Retrieve the
 */


//@Entity(foreignKeys = [ForeignKey(entity = Inspection::class, parentColumns =  arrayOf("inspection"),
//    childColumns =  arrayOf("question"),
//    onDelete = ForeignKey.NO_ACTION)]
//)
@Entity(tableName = "question")
data class Inspection(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "inspectionId") var inspectionId: Int = 0,
    @ColumnInfo(name = "areaId") var areaId: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "status") val status: InspectionStatus = InspectionStatus.UNKNOWN,
    @ColumnInfo(name = "date") val date: OffsetDateTime = OffsetDateTime.now()
)

//Example of how to get InspectionWithQuestions from dao
//@Transaction
//@Query("SELECT * FROM inspection")
//fun getInspectionsWithQuestions(): List<InspectionWithQuestions>

data class InspectionWithQuestions(
    @Embedded val inspection: Inspection,
    @Relation(
        parentColumn = "inspectionId",
        entityColumn = "questionId"
    )
    val questions: List<Question>
)