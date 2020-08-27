package com.matthew.inspections.ui.inspections.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.matthew.inspections.ui.inspections.InspectionsAdapter
import com.matthew.inspections.ui.inspections.uiModel.UiEmpty
import com.matthew.inspections.ui.inspections.uiModel.UiInspection
import com.matthew.inspections.ui.inspections.uiModel.UiTitle
import java.util.*

class InspectionsViewModel @ViewModelInject constructor() : ViewModel(), InspectionsAdapter.InspectionListener {

    companion object {
        const val PAST = 0
        const val CURRENT = 1
        const val FUTURE = 2
    }

    val adapter = InspectionsAdapter(this)

    private val _launchDetailActivity = MutableLiveData<Int>()
    val launchDetailActivity get() = _launchDetailActivity

    //get the right list data to display
    fun getInspectionsUiData(position: Int) = liveData {
        val list = when (position) {
            PAST -> listOf(
                UiTitle("Area One"),
                UiInspection("steve", 15, Date(2020, 7, 12), 0),
                UiInspection("pete", 2, Date(2020, 8, 25), 1),
                UiInspection("bob", 56, Date(2020, 6, 3), 2),
                UiTitle("Area Two"),
                UiInspection("harry", 7, Date(2020, 3, 8), 3),
                UiInspection("pete", 2, Date(2019, 9, 29), 4)
            )
            CURRENT -> listOf(
                UiTitle("Area One"),
                UiInspection("geoff", 2, Date(2020, 7, 30), 5)
            )
            FUTURE -> listOf(UiEmpty())
            else -> emptyList()
        }
        emit(list)
    }

    override fun onClickedInspection(inspectionId: Int) {
        _launchDetailActivity.value = inspectionId
    }

}