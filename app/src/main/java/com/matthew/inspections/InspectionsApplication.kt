package com.matthew.inspections

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@Singleton
@HiltAndroidApp
class InspectionsApplication : Application()