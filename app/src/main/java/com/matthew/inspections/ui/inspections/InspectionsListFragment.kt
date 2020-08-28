package com.matthew.inspections.ui.inspections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.matthew.inspections.databinding.FragmentInspectionsListBinding
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel
import com.matthew.inspections.util.autoCleared
import androidx.lifecycle.Observer
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionsListFragment : Fragment() {

    companion object {
        const val ARG_STATUS = "status"

        fun getInstance(status: Int): Fragment {
            return InspectionsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_STATUS, status)
                }
            }
        }
    }

    private var binding: FragmentInspectionsListBinding by autoCleared()
    private val viewModel: InspectionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInspectionsListBinding.inflate(inflater, container, false)
        binding.status = arguments?.getInt(ARG_STATUS,0)!!
        binding.viewModel = viewModel.apply{
            launchDetailActivity.observe(viewLifecycleOwner, Observer { value -> (activity as InspectionsActivity).launchDetailActivity(value) })
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}