package com.matthew.inspections.ui.inspections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.matthew.inspections.databinding.FragmentInspectionsListBinding
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel
import com.matthew.inspections.util.autoCleared
import androidx.lifecycle.Observer

class InspectionsListFragment : Fragment() {

    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(position: Int): Fragment {
            return InspectionsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
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
        arguments?.getInt(ARG_POSITION)?.let{
            binding.position = it
        }
        binding.viewModel = viewModel.apply{
            launchDetailActivity.observe(viewLifecycleOwner, Observer { value -> (activity as InspectionsActivity).launchDetailActivity(value) })
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}