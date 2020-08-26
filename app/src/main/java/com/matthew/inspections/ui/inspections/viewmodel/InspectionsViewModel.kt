package com.matthew.inspections.ui.inspections.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.matthew.inspections.ui.inspections.InspectionsAdapter
import com.matthew.inspections.ui.inspections.uiModel.InspectionUiModel
import com.matthew.inspections.ui.inspections.uiModel.UiEmpty
import com.matthew.inspections.ui.inspections.uiModel.UiInspection
import com.matthew.inspections.ui.inspections.uiModel.UiTitle
import java.util.*

class InspectionsViewModel @ViewModelInject constructor() : ViewModel() {

    companion object {
        const val PAST = 0
        const val CURRENT = 1
        const val FUTURE = 2
    }

    val adapter = InspectionsAdapter()

    //get the right list data to display
    fun getInspectionsUiData(position: Int) = liveData {
        val list = when (position) {
            PAST -> listOf(
                UiTitle("Area One"),
                UiInspection("steve", 15, Date(2020, 7, 12)),
                UiInspection("pete", 2, Date(2020, 8, 25)),
                UiInspection("bob", 56, Date(2020, 6, 3)),
                UiTitle("Area Two"),
                UiInspection("harry", 7, Date(2020, 3, 8)),
                UiInspection("pete", 2, Date(2019, 9, 29))
            )
            CURRENT -> listOf(
                UiTitle("Area One"),
                UiInspection("geoff", 2, Date(2020, 7, 30)))
            FUTURE -> listOf(UiEmpty())
            else -> emptyList()
        }
        emit(list)
    }


}