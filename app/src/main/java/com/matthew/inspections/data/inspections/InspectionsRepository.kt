package com.matthew.inspections.data.inspections

import androidx.annotation.NonNull
import com.matthew.inspections.dagger.qualifier.ForDatabase
import com.matthew.inspections.network.InspectionsService
import com.matthew.inspections.room.InspectionsDao
import com.matthew.inspections.room.data.InspectionWithQuestions
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel.Companion.CURRENT
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel.Companion.FUTURE
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel.Companion.PAST
import java.time.OffsetDateTime
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectionsRepository @Inject constructor(
    private val service: InspectionsService,
    private val dao: InspectionsDao,
    @ForDatabase @NonNull private val databaseExecutor: Executor
) {
    suspend fun getAllInspections(status: Int): List<InspectionWithQuestions> {

            return try {
                var list = listOf<InspectionWithQuestions>()
                 databaseExecutor.execute {
                     list = when(status){
                         PAST -> dao.getPastInspectionsWithQuestions(OffsetDateTime.now())
                         CURRENT -> dao.getPastInspectionsWithQuestions(OffsetDateTime.now())
                         FUTURE -> dao.getPastInspectionsWithQuestions(OffsetDateTime.now())
                         else -> emptyList()
                     }
                 }
                list
            }catch (e: Exception){
                emptyList()
            }

    }
}