package com.matthew.inspections.ui.inspectionDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.matthew.inspections.R
import com.matthew.inspections.databinding.*
import com.matthew.inspections.ui.inspectionDetail.uiModel.InspectionDetailUiModel
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiAnswer
import com.matthew.inspections.ui.inspectionDetail.uiModel.UiQuestion
import com.matthew.inspections.ui.inspections.BaseViewHolder

class InspectionDetailAdapter :
    ListAdapter<InspectionDetailUiModel, BaseViewHolder<*>>(DiffCallback()) {

    companion object {
        private const val TYPE_QUESTION = 0
        private const val TYPE_ANSWER = 1
    }

    interface InspectionListener {
        fun onClickedInspection(inspectionId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_QUESTION -> QuestionViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_question,
                    parent,
                    false
                )
            )
            TYPE_ANSWER ->
                AnswerViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_answer,
                        parent,
                        false
                    )
                )
            else -> throw IllegalArgumentException("Invalid viewType " + viewType)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiQuestion -> TYPE_QUESTION
            is UiAnswer -> TYPE_ANSWER
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = getItem(position)
        when (holder) {
            is QuestionViewHolder -> holder.bind(element as UiQuestion)
            is AnswerViewHolder -> holder.bind(element as UiAnswer)
            else -> throw IllegalArgumentException()
        }
    }

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        BaseViewHolder<UiQuestion>(binding.root) {
        override fun bind(item: UiQuestion) {
            binding.question = item
        }
    }

    inner class AnswerViewHolder(private val binding: ItemAnswerBinding) :
        BaseViewHolder<UiAnswer>(binding.root) {
        override fun bind(item: UiAnswer) {
            binding.answer = item
        }
    }

    class DiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return false
        }
    }
}