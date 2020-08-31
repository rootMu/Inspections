package com.matthew.inspections.ui.inspections.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.matthew.inspections.data.inspections.InspectionsRepository
import com.matthew.inspections.room.data.InspectionWithQuestions
import com.matthew.inspections.ui.inspections.InspectionListStatus
import com.matthew.inspections.ui.inspections.InspectionsAdapter
import com.matthew.inspections.ui.inspections.InspectionsFilter
import com.matthew.inspections.ui.inspections.InspectionsSearch
import com.matthew.inspections.ui.inspections.uiModel.InspectionUiModel
import com.matthew.inspections.ui.inspections.uiModel.UiEmpty
import com.matthew.inspections.ui.inspections.uiModel.UiInspection
import com.matthew.inspections.ui.inspections.uiModel.UiTitle
import com.matthew.inspections.util.Resource

class InspectionsViewModel @ViewModelInject constructor(
    private val repository: InspectionsRepository
) : ViewModel() {

    companion object {
        val DEFAULT_SORT = InspectionsFilter.ByArea
        val DEFAULT_SEARCH = InspectionsSearch.ByName
        val DEFAULT_STATUS = InspectionListStatus.Past
    }

    lateinit var adapter: InspectionsAdapter

    private val _searchSpinnerVisibility = MutableLiveData<Boolean>(false)
    val searchSpinnerVisibility get() = _searchSpinnerVisibility

    fun hideSearchSpinner() {
        _searchSpinnerVisibility.value = false
    }

    private val _launchDetailActivity = MutableLiveData<Int>()
    val launchDetailActivity get() = _launchDetailActivity

    private val _searchParameters: MutableLiveData<SearchParameters> =
        MutableLiveData(SearchParameters())
    val searchParameters: LiveData<SearchParameters> get() = _searchParameters

    val filter = Transformations.map(_searchParameters, ::mapSearchParametersToFilter)
    private fun mapSearchParametersToFilter(searchParameters: SearchParameters) =
        when (searchParameters.sort) {
            InspectionsFilter.ByType -> "Type"
            else -> "Area"
        }

    val search = Transformations.map(_searchParameters, ::mapSearchParametersToSearch)
    private fun mapSearchParametersToSearch(searchParameters: SearchParameters) =
        when (searchParameters.search) {
            InspectionsSearch.ByType -> "Type"
            InspectionsSearch.ById -> "Id"
            InspectionsSearch.ByArea -> "Area"
            else -> "Name"
        }

    val loading = MutableLiveData<Boolean>(false)

    private val loadTrigger = MutableLiveData(Unit)
    val inspectionsLiveData: LiveData<MutableList<InspectionUiModel>> = loadTrigger.switchMap {
        searchParameters.value!!.let {
            loadData(
                it.status,
                it.searchString,
                it.search
            )
        }
    }

    private fun loadData(
        status: InspectionListStatus?,
        search: String?,
        searchType: InspectionsSearch?
    ) =
        Transformations.map(
            repository.getAllInspections(
                status,
                search,
                searchType
            )
        ) { resource ->

            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    mutableListOf<InspectionUiModel>().apply{

                        if(searchParameters.value?.sort == InspectionsFilter.ByArea){
                            resource.data?.groupBy { it.area.areaId }
                        }else{
                            resource.data?.groupBy { it.type.id }
                        }?.forEach {
                            map ->
                            val title = if(searchParameters.value?.sort == InspectionsFilter.ByArea){
                                repository.getAreaNameById(map.key?:0)
                            }else{
                                repository.getTypeNameById(map.key?:0)
                            }
                            add(UiTitle(title))
                            map.value.map { inspection ->
                                add(inspection.mapToUi())
                            }
                        }

                        if(isEmpty()){
                            add(UiEmpty())
                        }
                        loading.value = false
                    }
                }

                Resource.Status.LOADING -> {
                    loading.value = true
                    mutableListOf()
                }
            }
        }


    fun updateSearchParameters(searchParameters: SearchParameters) {
        _searchParameters.value = searchParameters
    }

    fun initialiseSearchParameters(status: InspectionListStatus): Boolean {
        _searchParameters.value = SearchParameters(status = status)
        loadTrigger.value = Unit
        return true
    }

    fun updateSearchParameterSearchString(s: String?): Boolean {
        return searchParameters.value?.copy()?.let { searchParameters ->
            return s?.let {
                _searchSpinnerVisibility.value = true
                searchParameters.searchString = it
                _searchParameters.value = searchParameters
                loadTrigger.value = Unit
                true
            } ?: false
        } ?: false
    }

    fun updateLaunchId(inspectionId: Int) {
        _launchDetailActivity.value = inspectionId
    }

    data class SearchParameters(
        var searchString: String? = null,
        var status: InspectionListStatus = DEFAULT_STATUS,
        var sort: InspectionsFilter = DEFAULT_SORT,
        var search: InspectionsSearch = DEFAULT_SEARCH
    )

    private fun InspectionWithQuestions.mapToUi() =
        UiInspection(inspection.name, questions.size, inspection.date, inspection.inspectionId)

    private val TEMP_AREAS = arrayListOf(
        "Accident and emergency",
        "Anaesthetics",
        "Breast screening",
        "Cardiology",
        "Chaplaincy",
        "Critical care",
        "Diagnostic imaging",
        "Discharge lounge"
    )

}