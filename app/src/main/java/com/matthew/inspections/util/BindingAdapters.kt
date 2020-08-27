package com.matthew.inspections.util

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.matthew.inspections.R
import com.matthew.inspections.ui.inspectionDetail.InspectionDetailAdapter
import com.matthew.inspections.ui.inspectionDetail.uiModel.InspectionDetailUiModel
import com.matthew.inspections.ui.inspections.InspectionsAdapter
import com.matthew.inspections.ui.inspections.uiModel.InspectionUiModel

@BindingAdapter("doOnTextChanged")
fun setOnTextChanged(view: EditText, listener: (
    text: CharSequence?,
    start: Int,
    before: Int,
    count: Int
) -> Unit) {
    view.doOnTextChanged(listener)
}

@BindingAdapter("submitInspectionList")
fun setInspectionAdapterItems(view: RecyclerView, items: LiveData<List<InspectionUiModel>>) {
    items.value?.let {
        with(view.adapter as InspectionsAdapter){
            submitList(it)
        }
    }
}

@BindingAdapter("displayQuestions")
fun setInspectionDetailUiModels(view: RecyclerView, items: LiveData<List<InspectionDetailUiModel>>) {
    items.value?.let {
        with(view.adapter as InspectionDetailAdapter) {
            submitList(it)
        }
    }
}

@BindingAdapter("tabLayout")
fun connectTabLayoutToViewpager(view: ViewPager2, tablayout: TabLayout) {
    TabLayoutMediator(tablayout, view){ tab, position ->
        tab.text = view.resources.getStringArray(R.array.inspection_tense)[position]
    }.attach()
}