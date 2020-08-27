package com.matthew.inspections.ui.inspectionDetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityInspectionDetailBinding
import com.matthew.inspections.ui.inspectionDetail.InspectionDetailViewModel.Companion.INSPECTION_ID

class InspectionDetailActivity : AppCompatActivity() {

    private val viewModel: InspectionDetailViewModel by viewModels()
    private lateinit var binding: ActivityInspectionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_inspection_detail
        )
        binding.viewModel = viewModel.apply{
            binding.inspectionDetail =  retrieveInspection(intent.getIntExtra(INSPECTION_ID, -1))
        }
    }
}