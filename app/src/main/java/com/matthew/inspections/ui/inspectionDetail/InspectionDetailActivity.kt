package com.matthew.inspections.ui.inspectionDetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityInspectionDetailBinding
import com.matthew.inspections.ui.inspectionDetail.viewmodel.InspectionDetailViewModel
import com.matthew.inspections.ui.inspectionDetail.viewmodel.InspectionDetailViewModel.Companion.INSPECTION_ID
import com.matthew.inspections.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionDetailActivity : AppCompatActivity() {

    private val viewModel: InspectionDetailViewModel by viewModels()
    private lateinit var binding: ActivityInspectionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_inspection_detail
        )
        binding.viewModel = viewModel.apply {
            retrieveInspection(intent.getIntExtra(INSPECTION_ID, -1))
            errorMessage.observe(this@InspectionDetailActivity, Observer {
                Toast.makeText(this@InspectionDetailActivity, it, Toast.LENGTH_SHORT).show()
            })
        }
        binding.lifecycleOwner = this@InspectionDetailActivity
    }
}