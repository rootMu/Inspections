package com.matthew.inspections.ui.inspections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityInspectionsBinding

class InspectionsActivity : AppCompatActivity() {

    private val viewModel: InspectionsViewModel by viewModels()
    private lateinit var binding: ActivityInspectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_inspections
        )
        binding.viewModel = viewModel
    }
}