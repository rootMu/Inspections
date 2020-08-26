package com.matthew.inspections.util

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter

@BindingAdapter("doOnTextChanged")
fun setOnTextChanged(view: EditText, listener: (
    text: CharSequence?,
    start: Int,
    before: Int,
    count: Int
) -> Unit) {
    view.doOnTextChanged(listener)
}