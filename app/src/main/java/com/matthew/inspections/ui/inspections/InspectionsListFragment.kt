package com.matthew.inspections.ui.inspections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.matthew.inspections.R
import com.matthew.inspections.databinding.FragmentInspectionsListBinding
import com.matthew.inspections.ui.inspections.viewmodel.InspectionsViewModel
import com.matthew.inspections.util.Resource
import com.matthew.inspections.util.autoCleared
import com.matthew.inspections.util.ifNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InspectionsListFragment : Fragment(), SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener,
    InspectionsAdapter.InspectionListener {

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

    private val sortArrayAdapter by lazy {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.filter_array,
                android.R.layout.simple_spinner_item
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
    }

    private val searchArrayAdapter by lazy {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.search_array,
                android.R.layout.simple_spinner_item
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val status = arguments?.getInt(ARG_STATUS) ?: 0

        binding = FragmentInspectionsListBinding.inflate(inflater, container, false).apply {

            viewModel = this@InspectionsListFragment.viewModel.apply {
                launchDetailActivity.observe(
                    viewLifecycleOwner,
                    Observer { value -> (activity as InspectionsActivity).launchDetailActivity(value) })
                adapter = InspectionsAdapter(this@InspectionsListFragment)

            }

            searchBy.apply {
                adapter = searchArrayAdapter
                onItemSelectedListener = this@InspectionsListFragment

            }
            sortBy.apply {
                adapter = sortArrayAdapter
                onItemSelectedListener = this@InspectionsListFragment
            }

            onQueryTextListener = this@InspectionsListFragment

            //Override the search view close button to reset values
            (search.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView).setOnClickListener {
                if (search.query.isEmpty()) {
                    search.isIconified = true
                } else {

                    this@InspectionsListFragment.viewModel.apply {
                        initialiseSearchParameters(status = InspectionListStatus.getByValue(status))
                        hideSearchSpinner()
                    }

                    searchBy.setSelection(0)
                    search.setQuery("", false)
                }
            }

            lifecycleOwner = this@InspectionsListFragment.viewLifecycleOwner

        }

        return binding.root
    }

    override fun onClickedInspection(inspectionId: Int) {
        viewModel.updateLaunchId(inspectionId)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query.ifNull { viewModel.hideSearchSpinner() }
        return viewModel.updateSearchParameterSearchString(query)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.searchParameters.value?.copy()?.apply {
            if (parent.id == R.id.sort_by) {
                this.sort = when (parent.getItemAtPosition(pos)) {
                    "Area" -> InspectionsFilter.ByArea
                    else -> InspectionsFilter.ByType
                }
            } else if (parent.id == R.id.search_by) {
                this.search = when (parent.getItemAtPosition(pos)) {
                    "Name" -> InspectionsSearch.ByName
                    "Id" -> {
                        binding.search.setQuery("", false)
                        InspectionsSearch.ById
                    }
                    "Area" -> InspectionsSearch.ByArea
                    else -> InspectionsSearch.ByType
                }
            }

            viewModel.updateSearchParameters(this)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        viewModel.searchParameters.value?.copy()?.apply {
            if (parent.id == R.id.sort_by) {
                this.sort = InspectionsFilter.ByArea
            } else if (parent.id == R.id.search_by) {
                this.search = InspectionsSearch.ByName
            }

            viewModel.updateSearchParameters(this)
        }
    }


}