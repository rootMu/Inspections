package com.matthew.inspections.util

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.matthew.inspections.R
import com.matthew.inspections.room.data.Answer
import com.matthew.inspections.ui.inspectionDetail.InspectionDetailAdapter
import com.matthew.inspections.ui.inspectionDetail.uiModel.InspectionDetailUiModel
import com.matthew.inspections.ui.inspections.InspectionStatus
import com.matthew.inspections.ui.inspections.InspectionsAdapter
import com.matthew.inspections.ui.inspections.uiModel.InspectionUiModel

@BindingAdapter("doOnTextChanged")
fun setOnTextChanged(
    view: EditText, listener: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit
) {
    view.doOnTextChanged(listener)
}

@BindingAdapter("submitInspectionList")
fun setInspectionAdapterItems(view: RecyclerView, items: LiveData<List<InspectionUiModel>>) {
    items.value?.let {
        with(view.adapter as InspectionsAdapter) {
            submitList(it)
        }
    }
}

@BindingAdapter("displayQuestions")
fun setInspectionDetailUiModels(
    view: RecyclerView,
    items: LiveData<List<InspectionDetailUiModel>>
) {
    items.value?.let {
        with(view.adapter as InspectionDetailAdapter) {
            submitList(it)
        }
    }
}

@BindingAdapter("tabLayout")
fun connectTabLayoutToViewpager(view: ViewPager2, tablayout: TabLayout) {
    TabLayoutMediator(tablayout, view) { tab, position ->
        tab.text = view.resources.getStringArray(R.array.inspection_tense)[position]
    }.attach()
}

@BindingAdapter("isSavable")
fun isInspectionSavable(view: Button, status: InspectionStatus) {
    val savable = when(status){
       InspectionStatus.EDITING -> true
        else -> false
    }
    view.isEnabled = savable
}

@BindingAdapter("isEditable")
fun isInspectionEditable(view: Button, status: InspectionStatus) {
    val editable = when(status){
        InspectionStatus.NOT_STARTED -> true
        InspectionStatus.STARTED -> true
        else -> false
    }
    view.isEnabled = editable
}

@BindingAdapter("isSubmitable")
fun isInspectionSubmittable(view: Button, status: InspectionStatus) {
    val submittable = when(status){
        InspectionStatus.EDITING -> true
        else -> false
    }
    view.isEnabled = submittable
}

@BindingAdapter("isSubmitVisible")
fun isSubmitInspectionButtonVisible(view: Button, answers: List<Answer>) {
    answers.firstOrNull { !(it.value == -1 && !it.notApplicable) }?.let{
        view.visibility = View.VISIBLE
    }?:run {
        view.visibility = View.GONE
    }

}
