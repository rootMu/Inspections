package com.matthew.inspections.room.data

import androidx.room.*
import com.matthew.inspections.ui.inspections.InspectionStatus
import java.time.LocalDateTime

@Entity(tableName = "inspection")
data class Inspection(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "inspectionId") var inspectionId: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "status") var status: InspectionStatus = InspectionStatus.UNKNOWN,
    @ColumnInfo(name = "date") var date: LocalDateTime = LocalDateTime.now()
)

data class InspectionWithQuestions(
    @Embedded val inspection: Inspection,
    @Relation(
        parentColumn = "inspectionId",
        entityColumn = "questionId"
    )
    val questions: List<Question> = emptyList(),
    @Relation(
        parentColumn = "inspectionId",
        entityColumn = "areaId"
    )
    val area: Area = Area(),
    @Relation(
        parentColumn = "inspectionId",
        entityColumn = "typeId"
    )
    val type: Type = Type()
)