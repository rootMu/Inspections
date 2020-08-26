package com.matthew.inspections.ui.inspections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityInspectionsBinding
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel

class InspectionsActivity : AppCompatActivity() {

    private val viewPager = InspectionsPagerAdapter(this@InspectionsActivity)
    private val viewModel: InspectionsViewModel by viewModels()
    private lateinit var binding: ActivityInspectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_inspections
        )
        binding.viewModel = viewModel
        binding.pagerAdapter = viewPager

    }

}