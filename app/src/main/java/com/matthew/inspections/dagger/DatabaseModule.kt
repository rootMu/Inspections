package com.matthew.inspections.dagger

import android.content.Context
import androidx.room.Room
import com.matthew.inspections.dagger.qualifier.ForDatabase
import com.matthew.inspections.room.InspectionsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun providesInspectionDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context.applicationContext,
        InspectionsDatabase::class.java, "inspections.db")
        .build()

    @Provides
    fun providesInspectionsDao(database: InspectionsDatabase) = database.inspectionsDao()

    @Provides
    @Singleton
    @ForDatabase
    fun providesDatabaseExecutor(): Executor = Executors.newSingleThreadExecutor()

}