package com.matthew.inspections.ui.inspections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ItemEmptyBinding
import com.matthew.inspections.databinding.ItemInspectionBinding
import com.matthew.inspections.databinding.ItemTitleBinding
import com.matthew.inspections.ui.inspections.uiModel.InspectionUiModel
import com.matthew.inspections.ui.inspections.uiModel.UiEmpty
import com.matthew.inspections.ui.inspections.uiModel.UiInspection
import com.matthew.inspections.ui.inspections.uiModel.UiTitle

class InspectionsAdapter(private val listener: InspectionListener) :
    ListAdapter<InspectionUiModel, BaseViewHolder<*>>(DiffCallback()) {

    companion object {
        const val TYPE_EMPTY = 0
        const val TYPE_TITLE = 1
        const val TYPE_INSPECTION = 2
    }

    interface InspectionListener {
        fun onClickedInspection(inspectionId: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiTitle -> TYPE_TITLE
            is UiInspection -> TYPE_INSPECTION
            else -> TYPE_EMPTY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_TITLE ->
                TitleViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_title,
                        parent,
                        false
                    )
                )
            TYPE_INSPECTION ->
                InspectionViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_inspection,
                        parent,
                        false
                    )
                )
            else -> EmptyViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_empty,
                    parent,
                    false
                )
            )

        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = getItem(position)
        when (holder) {
            is TitleViewHolder -> holder.bind(element as UiTitle)
            is InspectionViewHolder -> holder.bind(element as UiInspection)
            is EmptyViewHolder -> holder.bind(element as UiEmpty)
            else -> throw IllegalArgumentException()
        }
    }

    inner class TitleViewHolder(private val binding: ItemTitleBinding) :
        BaseViewHolder<UiTitle>(binding.root) {
        override fun bind(item: UiTitle) {
            binding.title = item
        }
    }

    inner class InspectionViewHolder(private val binding: ItemInspectionBinding) :
        BaseViewHolder<UiInspection>(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun bind(item: UiInspection) {
            binding.inspection = item
        }

        override fun onClick(view: View) {
            listener.onClickedInspection(binding.inspection!!.id)
        }
    }

    inner class EmptyViewHolder(binding: ItemEmptyBinding) :
        BaseViewHolder<UiEmpty>(binding.root) {
        override fun bind(item: UiEmpty) {}
    }


    class DiffCallback<T : InspectionUiModel> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return false
        }
    }
}