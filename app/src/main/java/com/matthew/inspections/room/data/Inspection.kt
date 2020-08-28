package com.matthew.inspections.room.data

import androidx.room.*
import com.matthew.inspections.ui.inspections.InspectionStatus
import java.time.OffsetDateTime

@Entity(tableName = "inspection")
data class Inspection(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "inspectionId") var inspectionId: Int = 0,
    @ColumnInfo(name = "areaId") var areaId: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "status") var status: InspectionStatus = InspectionStatus.UNKNOWN,
    @ColumnInfo(name = "date") var date: OffsetDateTime = OffsetDateTime.now()
)

data class InspectionWithQuestions(
    @Embedded val inspection: Inspection,
    @Relation(
        parentColumn = "inspectionId",
        entityColumn = "questionId"
    )
    val questions: List<Question>
)