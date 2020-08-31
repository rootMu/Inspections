package com.matthew.inspections.ui.inspections

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.matthew.inspections.R
import com.matthew.inspections.databinding.ActivityInspectionsBinding
import com.matthew.inspections.ui.inspectionDetail.InspectionDetailActivity
import com.matthew.inspections.ui.inspectionDetail.viewmodel.InspectionDetailViewModel.Companion.INSPECTION_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityInspectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_inspections
        )
        binding.pagerAdapter = InspectionsPagerAdapter(this@InspectionsActivity)
    }

    fun launchDetailActivity(inspectionId: Int) {
        startActivity(Intent(applicationContext, InspectionDetailActivity::class.java).apply{
            putExtra(INSPECTION_ID, inspectionId)
        })
    }

}