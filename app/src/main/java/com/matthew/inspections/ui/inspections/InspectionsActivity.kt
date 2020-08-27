package com.matthew.inspections.ui.inspections

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityInspectionsBinding
import com.matthew.inspections.ui.inspectionDetail.InspectionDetailActivity
import com.matthew.inspections.ui.inspectionDetail.InspectionDetailViewModel.Companion.INSPECTION_ID
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel

class InspectionsActivity : AppCompatActivity()  {

    private val viewModel: InspectionsViewModel by viewModels()

    private lateinit var binding: ActivityInspectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_inspections
        )
        binding.viewModel = viewModel.apply{
            launchDetailActivity.observe(this@InspectionsActivity, Observer { value -> launchDetailActivity(value) })
        }
        binding.lifecycleOwner = this
        binding.pagerAdapter = InspectionsPagerAdapter(this@InspectionsActivity)
    }

    fun launchDetailActivity(inspectionId: Int) {
        startActivity(Intent(applicationContext, InspectionDetailActivity::class.java).apply{
            putExtra(INSPECTION_ID, inspectionId)
        })
    }

}